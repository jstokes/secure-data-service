#! /bin/bash
#set -x

if [ -z $1 ]; then
  mongos_port=27017
else
  mongos_port=$1
fi
if [ -z $2 ]; then
  num_shards=2
else
  num_shards=$2
fi


M=~/mongo/shard

rm -rf $M/data $M/logs $M/pids

mkdir -p $M/data/db/config
mkdir -p $M/logs

wait_for_mongo() {
    local port="$1"
    printf "  waiting for mongo process on port $port...";
    result=1
    while [ $result -ne 0 ]
    do
        printf ".";
        sleep 1s
        mongo --port $port --eval "1" > /dev/null 2>&1
        result=$?
    done
    echo "done!";
}

shard_port_base=10000
shard_port=$shard_port_base
echo "Starting up replica servers..."
for ((i=1; i<=$num_shards; i++))
do
  for r in master slave arbiter; 
  do
  	shard_port=$(( $shard_port + 1 ))
	start_port=$shard_port
	mkdir -p $M/data/db/$i/$r
  	mongod --dbpath $M/data/db/$i/$r --port $shard_port --oplogSize 700 --rest --replSet "set${i}" --nojournal > $M/logs/shard_${shard_port}.log &
  	echo $! >> $M/pids
  	wait_for_mongo $shard_port

  	mongo config --port $shard_port --eval 'db.settings.save({"_id":"chunksize", "value":1});'
  done
  master_port=$(( $shard_port - 2 ))
  slave_port=$(( $shard_port - 1))
  arbiter_port=$shard_port
	
  mongo admin --port $master_port --eval 'db.runCommand({"replSetInitiate" : {"_id" : "set'$i'", "members" : [{"_id" : 1, "host" : "localhost:'$master_port'"}, {"_id" : 2, "host" : "localhost:'$slave_port'"}, {"_id" : 3, "host" : "localhost:'$arbiter_port'", "arbiterOnly" : true}]}})'
 
done

echo "Starting up config server..."
mongod --configsvr --dbpath $M/data/db/config --port 20000 --nojournal > $M/logs/configdb.log &
echo $! >> $M/pids

wait_for_mongo 20000
echo "Setting chunk size on config server..."
mongo config --port 20000 --eval 'db.settings.save({"_id":"chunksize", "value":1});'


echo "Starting up mongos router..."
mongos --configdb localhost:20000 --port $mongos_port > $M/logs/mongos.log &
echo $! >> $M/pids

wait_for_mongo $mongos_port

echo "Adding shards to cluster..."
for ((i=0; i<$num_shards; i++))
do
  set_name="set$(($i+1))"
  sleep 15
  mongo admin --port $mongos_port --eval 'db.runCommand( { addshard : "'$set_name'/localhost:'$(($shard_port_base+$i*3+1))',localhost:'$(($shard_port_base+$i*3+2))',localhost:'$(($shard_port_base+$i*3+3))'"})'
done
echo "mongos is running on port $mongos_port"
