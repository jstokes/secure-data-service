package org.slc.sli.aggregation;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import com.mongodb.hadoop.util.MongoConfigUtil;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.slc.sli.aggregation.mapreduce.io.MongoAggFormatter;
import org.slc.sli.aggregation.mapreduce.io.MongoIdInputFormat;
import org.slc.sli.aggregation.mapreduce.map.ConfigurableMapper;
import org.slc.sli.aggregation.mapreduce.map.key.TenantAndIdEmittableKey;


/**
 * Aggregate the highest ever score
 */
public class HighestEver extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new HighestEver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {

        String assmtIDCode = args.length == 0 ? "Grade 7 2011 State Math" : args[0];
        String assmtId = getAssessmentId(assmtIDCode);

        Configuration conf = new Configuration();

        conf.set(ScoreMapper.SCORE_TYPE, "Scale score");
        conf.set(MongoAggFormatter.UPDATE_FIELD, "calculatedValues.assessments." + assmtIDCode + ".HighestEver.ScaleScore");

        InputStream s = getClass().getClassLoader().getResourceAsStream("HighestEverForAssessment.json");
        String singleMapperConf = new Scanner(s, "UTF-8").useDelimiter("\\A").next();
        conf.set(ConfigurableMapper.CHAIN_CONF, singleMapperConf);
        conf.set("@ID@", assmtId);

        JobConf jobConf = ConfigurableMapper.parseMapper(conf);

        // we must specify an input and output collection to calculate splits.
        MongoURI input = new MongoURI("mongodb://localhost/sli.studentAssessmentAssociation");
        MongoURI output = new MongoURI("mongodb://localhost/sli.student");
        MongoConfigUtil.setInputURI(jobConf, input);
        MongoConfigUtil.setOutputURI(jobConf, output);

        Job job = new Job(jobConf);

        job.setCombinerClass(Highest.class);
        job.setReducerClass(Highest.class);

        job.setInputFormatClass(MongoIdInputFormat.class);
        job.setOutputKeyClass(TenantAndIdEmittableKey.class);
        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputFormatClass(MongoAggFormatter.class);

        boolean success = job.waitForCompletion(true);

        return success ? 0 : 1;
    }

    protected String getAssessmentId(String assmtIDCode) throws UnknownHostException {
        Mongo m = new Mongo("localhost");
        DB db = m.getDB("sli");
        DBCollection assessments = db.getCollection("assessment");
        DBObject assmt = assessments.findOne(new BasicDBObject("body.assessmentIdentificationCode.ID", assmtIDCode));
        String assmtId = (String) assmt.get("_id");
        return assmtId;
    }
}
