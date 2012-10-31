#!/bin/bash
#
# This script resets SLIRP Mongo and ActiveMQ
# For use between Day 1 ingestion tests
#

if [ $# -gt 0 ];
then
  echo "Usage: scripts/slirp_reset (run from the config/ directory)"
  echo "This script uses scripts in the indexes/ and shards/ folders"
  echo "NOTICE: ALL Sharding jobs will execute with the following configuration, it can be modified in the $0 script."
  echo "     \"var num_years=1, tenant='Hyrule'\" "
  exit 1
fi

######################
#   Primary Config   #
######################

PRIMARIES="slirpmongo03.slidev.org slirpmongo05.slidev.org slirpmongo09.slidev.org slirpmongo11.slidev.org"
ISDB="slirpmongo99.slidev.org"
SHARD_VARS="var num_years=1, tenant='Hyrule'"

### The script!
echo "*********************************************************************************************"
echo "**  WARNING  :  you must use deploy.slidev.org to PUSH updated code/configuration to SLIRP **"
echo "*********************************************************************************************"
sleep 4


echo " ***** Stopping Tomcat!"
service tomcat stop

echo " ***** Clearing LZ!"

rm -r -f /opt/lz/inbound/*

echo " ***** Identifying Collections and dropping each one"

# Identify collections
COLLECTIONS=`mongo sli<<END
show collections
END`
COLLECTIONS=`echo $COLLECTIONS| sed s/bye// |sed s/MongoDB\ shell\ version:\ .*\ connecting\ to:\ sli//|sed s/system.indexes//`
for i in $COLLECTIONS;
do
  mongo sli <<END
db.$i.drop()
END
done

echo " ***** Attempting to drop databases (SLI and Hyrule.NYC)"
mongo <<END
use sli
db.dropDatabase();
use d36f43474916ad310100c9711f21b65bd8231cc6
db.dropDatabase();
END

echo " ***** Ensuring Primary servers no longer have the SLI or Kyrule.NYC Database."

for i in $PRIMARIES;
do
  mongo $i <<END
use sli
db.dropDatabase();
use d36f43474916ad310100c9711f21b65bd8231cc6
db.dropDatabase();
END
done

echo " ***** Adding Indexes to sli db"
mongo sli < indexes/sli_indexes.js
######echo " ***** Applying Sharding Script"
######mongo admin --eval " $SHARD_VARS " shards/sli-shard-presplit.js
#######mongo admin --eval "var num_years=1, tenant='Hyrule'" sli-shard-presplit.js
######echo " ***** Reapplying Indexes to sli db"
######mongo sli < indexes/sli_indexes.js

echo " ***** Clearing databases off $ISDB"
mongo $ISDB/is <<END
db.dropDatabase();
END
mongo $ISDB/ingestion_batch_job <<END
db.dropDatabase();
END
echo " ***** Setting up indexes on $ISDB"
mongo $ISDB/is < indexes/is_indexes.js
mongo $ISDB/ingestion_batch_job < indexes/ingestion_batch_job_indexes.js

echo " ***** Restarting Mongos"
killall mongos
service mongos start

echo " ***** Attempting to clear ActiveMQ"
CURLCMD="curl -c /tmp/cookiejar -b /tmp/cookiejar"
ACTIVEMQ_SECRET=`$CURLCMD -s http://slirpingest01.slidev.org:8161/admin/queues.jsp |grep secret|cut -d = -f 5|cut -d \" -f 1|tail -n 1`
echo " ***** Using ActiveMQ Secret $ACTIVEMQ_SECRET"
$CURLCMD "http://slirpingest01.slidev.org:8161/admin/deleteDestination.action?JMSDestination=ingestion.maestro&JMSDestinationType=queue&secret="$ACTIVEMQ_SECRET >>/dev/null
$CURLCMD "http://slirpingest01.slidev.org:8161/admin/deleteDestination.action?JMSDestination=ingestion.pit&JMSDestinationType=queue&secret="$ACTIVEMQ_SECRET >>/dev/null
$CURLCMD "http://slirpingest01.slidev.org:8161/admin/deleteDestination.action?JMSDestination=ingestion.workItem&JMSDestinationType=queue&secret="$ACTIVEMQ_SECRET  >>/dev/null

echo " ***** Restarting Tomcat"
service tomcat start


