package org.slc.sli.view;

import org.slc.sli.config.Field;

/**
 * Used to calculate a ratio, such as for tardiness rate.
 * 
 * It aggregates two values, subcount and total count, and returns their ratio, ie
 * subcount / totalcount
 * 
 * @author pwolf
 *
 */
public interface AggregateRatioResolver {
    
    /**
     * The sub count for a given field, ie. the numerator
     * @param configField
     * @return
     */
    public int getSubCountForPath(Field configField);
    
    /**
     * The total count for a given field, ie. the denominator
     * @param configField
     * @return
     */
    public int getTotalCountForPath(Field configField);

}
