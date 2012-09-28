from aggregatedriver import run_pipeline, write_result
from instrumentation import instrument 
from multiprocessing import Process, Queue 
from Queue import Empty 

import pymongo
import sys 

inst = instrument()

def do_work(assessment_id, db): 
    @inst.lap
    def find_max_score(entry):
        all_scores = [float(x) for x in entry["scores"]]
        entry["highestScore"] = max(all_scores)
        del entry["scores"]
        return entry

    def _worker(q):
        try:
          while True:
              print "Queue size: ", q.qsize() 
              student_batch = q.get(True, 10) 
              print "GOT batch of size %s" % len(student_batch)
              PIPELINE = [
                {
                  "$match" : { 
                    "body.assessmentId" : assessment_id, 
                    "body.scoreResults.assessmentReportingMethod" : "Scale score",
                    "body.studentId" : { "$in" : student_batch }
                  }
                },
                { 
                  "$project" : {
                    "tenantId"  : "$metaData.tenantId",
                    "studentId" : "$body.studentId",
                    "result" : "$body.scoreResults.result"
                  }
                },
                {
                  "$unwind" : "$result",
                },
                {
                  "$group" : {
                    "_id" : "$studentId",
                    "scores" : { "$push" : "$result" }
                  }
                }
              ]

              results = [find_max_score(x) 
                         for x in db.command("aggregate", input_collection, pipeline=PIPELINE)["result"]]
              col = db[target_collection]
              print "Values read !"
              for entry in results:
                col.update({"_id" : entry["_id"]}, {"$set" : { target_var : entry[src_field]}})
              print "%s students processed." % (batch * BATCH_SIZE)
        except Empty, e:
            print "Process finished."

    input_collection = "studentAssessmentAssociation"
    target_collection = "student"
    target_query = {"_id" : "%(_id)s"}
    target_var = "calculatedValues.assessments.%s.HighestEver.ScaleScore" % assessment_id
    src_field = "highestScore"

    with inst.lap(id="read_students"):
      cursor = db[input_collection].find({"body.assessmentId" : assessment_id}, 
                                         {"_id" : 0, "body.studentId" : 1})
      print "Queried students. Found %s documents" % cursor.count()
      student_ids = list(set((x["body"]["studentId"] for x in cursor if x.get("body", {}).get("studentId", None))))
      print "Found %s students." % len(student_ids)

    BATCH_SIZE = 50000
    N_WORKERS = 10

    # fill a queue and start the workers 
    q = Queue() 
    batch = 0
    while (batch * BATCH_SIZE) < len(student_ids):
        student_batch = student_ids[batch * BATCH_SIZE:batch * BATCH_SIZE + BATCH_SIZE]
        print "Batch read !"
        q.put(student_batch)
        batch += 1

    all_processes = [] 
    for i in xrange(N_WORKERS):
      t = Process(target=_worker, args=(q,), name="Thread_%s" % i)
      t.daemon = True
      t.start() 
      all_processes.append(t)

    # wait until everything is processed
    with inst.lap(id="process_student_batch"):
      [p.join() for p in all_processes]

    col = db[target_collection]
    total_count = col.find({}).count()
    written_docs = col.find({ target_var  : { "$exists" : True } }, 
                            { "_id" : True, target_var : True })
    found_count = written_docs.count()
    print "%s of %s records in %s contain values" % (found_count, total_count, target_collection)

def main():
    hostname = "localhost" if len(sys.argv) < 2 else sys.argv[1]
    con = pymongo.Connection(hostname, 27017)
    db = con.sli

    inst.start()
    if len(sys.argv) < 3:

        #result = set((x["body"]["assessmentId"] for x in db["studentAssessmentAssociation"].find({}, ["body.assessmentId"]) if x.get("body", {}).get("assessmentId", None)))
        result = db["studentAssessmentAssociation"].distinct("body.assessmentId")
        print "Available Assessment IDs:"
        for e in result:
          print e
    else:
        assessment_id = sys.argv[2]
        do_work(assessment_id, db)
    inst.stop()
    inst.print_results() 

    # close the connection 
    con.close()

if __name__=="__main__":
    main() 
