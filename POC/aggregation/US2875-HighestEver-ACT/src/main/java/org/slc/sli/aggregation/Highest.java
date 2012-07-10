package org.slc.sli.aggregation;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.mongodb.hadoop.io.BSONWritable;

/**
 * Reducer to get the highest value
 * 
 * @author nbrown
 *
 */
public class Highest extends Reducer<BSONWritable, DoubleWritable, BSONWritable, DoubleWritable>{

    @Override
    protected void reduce(BSONWritable id, Iterable<DoubleWritable> scoreResults, Context context)
            throws IOException, InterruptedException {
        DoubleWritable highest = null;
        for(DoubleWritable scoreResult: scoreResults){
            double score = scoreResult.get();
            if(highest == null || score > highest.get()){
                highest = scoreResult;
            }
        }
        context.write(id, highest);
    }
    
}
