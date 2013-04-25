#!/bin/sh
set -e
sh checkoutAndBuild.sh $2
cd /opt/megatron/sli/sli/acceptance-tests/teamFiles/
sh ingestDataset.sh $1 1
sh ingestDataset.sh $1 2
sh log_durations.sh $1
sh ingestDataset.sh $3 3
sh log_delete_duration.sh $3
sh ingestDataset.sh purge.zip 4
sh log_purge_duration.sh
sh resetEnvironment.sh
sh ingestDataset.sh $1 1
sh ingestDataset.sh $1 2
sh log_durations.sh $1
sh ingestDataset.sh $3 3
sh log_delete_duration.sh $3
sh ingestDataset.sh purge.zip 4
sh log_purge_duration.sh

# No longer used
# tail -16 megtomcat01_logs/auto_perf_results.log| ingestion-mailx  -s "Megatron Mini Slirp Performance Testing" Sliders-MegatronDev@wgen.net

# Send PDF report
./ingestion-report.py < megtomcat01_logs/auto_perf_results.log > message.txt
/usr/local/bin/ingestion-mailx \
    -s "Megatron Mini Slirp Performance Testing" \
    -a IngestionPerformanceDaily.pdf \
    -a raw_data.txt \
    Sliders-MegatronDev@wgen.net \
    okrook@wgen.net \
    < message.txt

echo "Done!"

