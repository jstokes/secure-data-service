package org.slc.sli.api.client.impl;

import java.util.Map;

import org.slc.sli.api.client.Query;

/**
 * 
 * Simple class used to define a set of query parameters.
 * 
 * @author asaarela
 */
public class BasicQuery implements Query {
    
    private static final String SORT_ORDER_KEY = "sort-order";
    private static final String SORT_ASCENDING = "ascending";
    private static final String SORT_DESCENDING = "descending";
    private static final String START_INDEX_KEY = "start-index";
    private static final String MAX_RESULTS_KEY = "max-results";
    
    /** Represents an empty query with no query parameters */
    public static final Query EMPTY_QUERY = new BasicQuery();
    
    private Map<String, Object> params;
    
    /**
     * Build a query, specifying optional values for sorting, field searching, and pagination.
     */
    public class Builder {
        private Map<String, Object> params;
        
        /**
         * Instantiate a new builder
         * 
         * @return Builder instance.
         */
        public Builder create() {
            return new Builder();
        }
        
        /**
         * Indicate the results should be returned in ascending order.
         * 
         * @return Updated Builder instance.
         */
        public Builder sortAscending() {
            params.put(SORT_ORDER_KEY, SORT_ASCENDING);
            return this;
        }
        
        /**
         * Indicate the results should be returned in descending order.
         * 
         * @return Updated Builder instance.
         */
        public Builder sortDescending() {
            params.put(SORT_ORDER_KEY, SORT_DESCENDING);
            return this;
        }
        
        /**
         * Filter results where fieldName is equal to value.
         * 
         * @param fieldName
         *            Field to filter on.
         * @param value
         *            The value to look for.
         * @return Updated Builder instance.
         */
        public Builder filterEqual(final String fieldName, final String value) {
            params.put(fieldName, value);
            return this;
        }
        
        /**
         * Apply pagination to the request results.
         * 
         * @param startIndex
         *            Start of the result window.
         * @param maxResults
         *            Maximum number of results to return.
         * @return Updated Builder instance.
         */
        public Builder paginate(final int startIndex, final int maxResults) {
            params.put(START_INDEX_KEY, startIndex);
            params.put(MAX_RESULTS_KEY, maxResults);
            return this;
        }
        
        /**
         * Construct a new BasicQuery instance.
         * 
         * @return BasicQuery representing the values set on this builder.
         */
        public Query build() {
            BasicQuery rval = new BasicQuery();
            rval.params = params;
            return rval;
        }
    }
    
    @Override
    public Map<String, Object> getParameters() {
        return params;
    }
    
}
