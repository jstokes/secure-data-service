#!/bin/sh
if [ -z "$1" ]; then
    echo "Delete dataset defaulted to BroadStudentDeleteFromInterchangeStudentParent.zip"
    delete="BroadStudentDeleteFromInterchangeStudentParent.zip"
else
    delete=$1
fi
start_date=`mongo ingestion_batch_job < times.js | awk {'print $3'} | awk 'NR==17'`
start_date=${start_date:9: 19}
end_date=`mongo ingestion_batch_job < times.js | awk {'print $3'} | awk 'NR==18'`
end_date=${end_date:9: 19}
let diff1=( `date +%s -d $end_date`-`date +%s -d $start_date` )
status=`mongo ingestion_batch_job < times.js | awk {'print $3'} | awk 'NR==19'`
echo "$delete - delete took $diff1 seconds, starting at $start_date and finishing on $end_date GMT with status $status"
echo "$delete - delete run took $diff1 seconds, starting at $start_date and finishing on $end_date GMT with status $status" >> megtomcat01_logs/auto_perf_results.log
