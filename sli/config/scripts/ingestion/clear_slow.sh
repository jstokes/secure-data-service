#!/bin/bash
#
# This script clears the slow query logs
#
#set -x

if [ $# -ne 0 ];
then
e echo "Usage: clear_slow [SLOW_QUERY_TIME]"
  echo "This script clears the slow query logs"
  exit 1
fi

NAME=$1

if [ $# -eq 1 ] ; then
  SLOW_QUERY=$1
else
  SLOW_QUERY=100
  # There is a bug with the slow query log see https://jira.mongodb.org/browse/CS-5365
  SLOW_QUERY=0
fi
if [ $SLOW_QUERY -gt 0 ] ; then
  SLOW_QUERY_PARAMS="1,$SLOW_QUERY"
else
  SLOW_QUERY_PARAMS="0"
fi

if [ -n "`hostname | grep slirp`" ] ; then
  # SLIRP
  PRIMARIES="slirpmongo03.slidev.org slirpmongo05.slidev.org slirpmongo09.slidev.org slirpmongo11.slidev.org"
  STAGING="slirpmongo99.slidev.org"
else
  # Local
  PRIMARIES="localhost"
  STAGING="localhost"
fi

if [ -z "$SCRIPTS" ] ; then
  SCRIPTS=.
fi
if [ -z "$ING_LOG_DIR" ] ; then
  ING_LOG_DIR=/opt/logs
fi
if [ -z "$LZ" ] ; then
  LZ=/opt/lz
fi

echo "******************************************************************************"
echo "**  Clearing slow query logs at `date`"
echo "******************************************************************************"

#
# Get slow query logs
#
echo "Slow query logs, ingestion_batch_job..."
mongo --quiet $STAGING/ingestion_batch_job << END
  db.setProfilingLevel(0);
  db.system.profile.drop();
  db.setProfilingLevel($SLOW_QUERY_PARAMS);
END

for i in $PRIMARIES;
do
  echo "Slow query logs, ${i}/sli..."
  mongo --quiet $i/sli << END
    db.setProfilingLevel(0);
    db.system.profile.drop();
    db.setProfilingLevel($SLOW_QUERY_PARAMS);
END
  echo "Slow query logs, $i/Hyrule..."
  mongo --quiet $i/d36f43474916ad310100c9711f21b65bd8231cc6 << END
    db.setProfilingLevel(0);
    db.system.profile.drop();
    db.setProfilingLevel($SLOW_QUERY_PARAMS);
END
  echo "Slow query logs, $i/02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a..."
  mongo --quiet $i/02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a << END
    db.setProfilingLevel(0);
    db.system.profile.drop();
    db.setProfilingLevel($SLOW_QUERY_PARAMS);
END
  echo "Slow query logs, $i/ff501cb38db19529bc3eb7fd5759f3844626fdf6..."
  mongo --quiet $i/ff501cb38db19529bc3eb7fd5759f3844626fdf6 << END
    db.setProfilingLevel(0);
    db.system.profile.drop();
    db.setProfilingLevel($SLOW_QUERY_PARAMS);
END
done

echo " ***** Truncating ingestion log"
echo " " > $ING_LOG_DIR/ingestion.log

if [ -n "`hostname | grep slirp`" ] ; then
  echo " ***** Truncating GC log"
  echo " " > /opt/logs/gc.out
fi

echo "slirp_clear_slow.sh finished at `date`"
