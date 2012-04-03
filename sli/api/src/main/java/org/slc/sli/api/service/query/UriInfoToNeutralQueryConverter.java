package org.slc.sli.api.service.query;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.QueryParseException;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.api.resources.v1.ParameterConstants;

import javax.ws.rs.core.UriInfo;

/**
 * Converts a String into a database independent NeutralQuery object.
 * It is up to the specific database implementation to turn the NeutralQuery
 * into the appropriate type of parameter for the associated DB engine.
 * 
 * @author kmyers
 *
 */
public class UriInfoToNeutralQueryConverter {


    private static final Logger LOG = LoggerFactory.getLogger(UriInfoToNeutralQueryConverter.class);
    
    /**
     * Keywords that the API handles through specific bindings in a NeutralQuery.
     * 
     * @author kmyers
     *
     */
    protected interface NeutralCriteriaImplementation {
        public void convert(NeutralQuery neutralQuery, Object value);
    }
    
    /* Criteria keywords and what to do with them when encountered */
    private Map<String, NeutralCriteriaImplementation> reservedQueryKeywordImplementations;
    
    public UriInfoToNeutralQueryConverter() {
        this.reservedQueryKeywordImplementations = new HashMap<String, NeutralCriteriaImplementation>();

        //limit
        this.reservedQueryKeywordImplementations.put(ParameterConstants.LIMIT, new NeutralCriteriaImplementation() {
            public void convert(NeutralQuery neutralQuery, Object value) {
                neutralQuery.setLimit(Integer.parseInt((String) value));
            }
        });
        
        //skip
        this.reservedQueryKeywordImplementations.put(ParameterConstants.OFFSET, new NeutralCriteriaImplementation() {
            public void convert(NeutralQuery neutralQuery, Object value) {
                neutralQuery.setOffset(Integer.parseInt((String) value));
            }
        });
        
        //includeFields
        this.reservedQueryKeywordImplementations.put(ParameterConstants.INCLUDE_FIELDS, new NeutralCriteriaImplementation() {
            public void convert(NeutralQuery neutralQuery, Object value) {
                neutralQuery.setIncludeFields((String) value);
            }
        });
        
        //excludeFields
        this.reservedQueryKeywordImplementations.put(ParameterConstants.EXCLUDE_FIELDS, new NeutralCriteriaImplementation() {
            public void convert(NeutralQuery neutralQuery, Object value) {
                neutralQuery.setExcludeFields((String) value);
            }
        });
        
        //sortBy
        this.reservedQueryKeywordImplementations.put(ParameterConstants.SORT_BY, new NeutralCriteriaImplementation() {
            public void convert(NeutralQuery neutralQuery, Object value) {
                neutralQuery.setSortBy((String) value);
            }
        });
        
        //sortOrder
        this.reservedQueryKeywordImplementations.put(ParameterConstants.SORT_ORDER, new NeutralCriteriaImplementation() {
            public void convert(NeutralQuery neutralQuery, Object value) {
                if (value.equals(ParameterConstants.SORT_DESCENDING)) {
                    neutralQuery.setSortOrder(NeutralQuery.SortOrder.descending);
                } else {
                    neutralQuery.setSortOrder(NeutralQuery.SortOrder.ascending);
                }
            }
        });
    }
    
    /**
     * Converts a & separated list of criteria into a neutral criteria object. Adds all
     * criteria to the provided neutralQuery.
     * 
     * @param neutralQuery object to add criteria to
     * @return a non-null neutral query containing any specified criteria
     */
    public NeutralQuery convert(NeutralQuery neutralQuery, UriInfo uriInfo) {
        if (neutralQuery != null && uriInfo != null) {
            String queryString = uriInfo.getRequestUri().getQuery();
            if (queryString != null) {
                try {
                    for (String criteriaString : queryString.split("&")) {
                        NeutralCriteria neutralCriteria = new NeutralCriteria(criteriaString);
                        NeutralCriteriaImplementation nci = this.reservedQueryKeywordImplementations.get(neutralCriteria.getKey());
                        if (nci == null) {
                            if (!neutralCriteria.getKey().equals("full-entities")
                                    && (!ParameterConstants.OPTIONAL_FIELDS.equals(neutralCriteria.getKey()))
                                    && (!ParameterConstants.INCLUDE_CUSTOM.equals(neutralCriteria.getKey()))) {
                                neutralQuery.addCriteria(neutralCriteria);
                            }
                        } else {
                            nci.convert(neutralQuery, neutralCriteria.getValue());
                        }
                    }
                } catch (RuntimeException re) {
                    LOG.error("error parsing query String {} {}", re.getMessage(), queryString);
                    throw new QueryParseException(re.getMessage(), queryString);
                }
            }
        }
        
        return neutralQuery;
    }
    
    /**
     * Converts a & separated list of criteria into a neutral criteria object. Creates a new
     * NeutralQuery with all associated criteria.
     *
     * @return a neutral implementation of the query string
     */
    public NeutralQuery convert(UriInfo uriInfo) {
        return this.convert(new NeutralQuery(), uriInfo);
    }
}


