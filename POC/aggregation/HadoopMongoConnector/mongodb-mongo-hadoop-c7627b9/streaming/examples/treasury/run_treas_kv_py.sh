hadoop jar target/mongo-hadoop-streaming-assembly*.jar -mapper examples/treasury/mapper_kv.py -reducer examples/treasury/reducer_kv.py -inputformat com.mongodb.hadoop.mapred.MongoInputFormat -outputformat com.mongodb.hadoop.mapred.MongoOutputFormat -inputURI mongodb://127.0.0.1/demo.yield_historical.in -outputURI mongodb://127.0.0.1/demo.yield_historical.streaming.kv.out