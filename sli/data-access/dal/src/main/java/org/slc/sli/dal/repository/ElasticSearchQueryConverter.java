package org.slc.sli.dal.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;

/**
 * Converter from Neutral Query to ES Query DSL
 */
@Component
public class ElasticSearchQueryConverter {
    public static final String Q = "q";

    private interface Operator {
        FilterBuilder getFilter(NeutralCriteria criteria);
        QueryBuilder getQuery(NeutralCriteria criteria);
    }

    private Map<String, Operator> operationMap;

    public ElasticSearchQueryConverter() {
        Map<String, Operator> operationMap = new HashMap<String, ElasticSearchQueryConverter.Operator>();
        operationMap.put(NeutralCriteria.CRITERIA_GT, new Operator() {
            @Override
            public FilterBuilder getFilter(NeutralCriteria criteria) {
                return FilterBuilders.rangeFilter(criteria.getKey()).gt(criteria.getValue());
            }
            @Override
            public QueryBuilder getQuery(NeutralCriteria criteria) {
                return QueryBuilders.rangeQuery(criteria.getKey()).gt(criteria.getValue());
            }
        });
        operationMap.put(NeutralCriteria.CRITERIA_GTE, new Operator() {
            @Override
            public FilterBuilder getFilter(NeutralCriteria criteria) {
                return FilterBuilders.rangeFilter(criteria.getKey()).gte(criteria.getValue());
            }
            @Override
            public QueryBuilder getQuery(NeutralCriteria criteria) {
                return QueryBuilders.rangeQuery(criteria.getKey()).gte(criteria.getValue());
            }
        });
        Operator terms = new Operator() {
            @Override
            public FilterBuilder getFilter(NeutralCriteria criteria) {
                return FilterBuilders.termsFilter(criteria.getKey(), getTermTokens(criteria.getValue()));
            }
            @Override
            public QueryBuilder getQuery(NeutralCriteria criteria) {
                if (Q.equals(criteria.getKey())) {
                    return QueryBuilders.queryString(criteria.getValue().toString().trim().toLowerCase());
                }
                return QueryBuilders.termsQuery(criteria.getKey(), getTermTokens(criteria.getValue()));
            }
        };
        operationMap.put(NeutralCriteria.CRITERIA_IN, terms);
        operationMap.put(NeutralCriteria.OPERATOR_EQUAL, terms);
        operationMap.put("!=", new Operator() {
            @Override
            public FilterBuilder getFilter(NeutralCriteria criteria) {
                return FilterBuilders.notFilter(FilterBuilders.termsFilter(criteria.getKey(), getTermTokens(criteria.getValue())));
            }
            @Override
            public QueryBuilder getQuery(NeutralCriteria criteria) {
                return QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery(criteria.getKey(), getTermTokens(criteria.getValue())));
            }
        });
        operationMap.put(NeutralCriteria.CRITERIA_LT, new Operator() {
            @Override
            public FilterBuilder getFilter(NeutralCriteria criteria) {
                return FilterBuilders.rangeFilter(criteria.getKey()).lt(criteria.getValue());
            }
            @Override
            public QueryBuilder getQuery(NeutralCriteria criteria) {
                return QueryBuilders.rangeQuery(criteria.getKey()).lt(criteria.getValue());
            }
        });
        operationMap.put(NeutralCriteria.CRITERIA_LTE, new Operator() {
            @Override
            public FilterBuilder getFilter(NeutralCriteria criteria) {
                return FilterBuilders.rangeFilter(criteria.getKey()).lte(criteria.getValue());
            }
            @Override
            public QueryBuilder getQuery(NeutralCriteria criteria) {
                return QueryBuilders.rangeQuery(criteria.getKey()).lte(criteria.getValue());
            }
        });

        operationMap.put(NeutralCriteria.CRITERIA_REGEX, new Operator() {
            @Override
            public FilterBuilder getFilter(NeutralCriteria criteria) {
                return FilterBuilders.prefixFilter(criteria.getKey(), ((String)criteria.getValue()).trim().toLowerCase());
            }
            @Override
            public QueryBuilder getQuery(NeutralCriteria criteria) {
                return QueryBuilders.prefixQuery(criteria.getKey(), ((String)criteria.getValue()).trim().toLowerCase());
            }
        });

        this.operationMap = Collections.unmodifiableMap(operationMap);
    }

    public FilterBuilder getFilter(NeutralCriteria criteria) {
        return this.operationMap.get(criteria.getOperator()).getFilter(criteria);
    }

    public QueryBuilder getQuery(NeutralCriteria criteria) {
        return this.operationMap.get(criteria.getOperator()).getQuery(criteria);
    }

    /**
     * Build elasticsearch query
     *
     * @param client
     * @param query
     * @return
     */
    public QueryBuilder getQuery(NeutralQuery query) {

        QueryBuilder qb;

        if (query.getCriteria().size() == 1 && query.getOrQueries().isEmpty()) {
            qb = getQuery(query.getCriteria().get(0));
        } else {
            BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            // set query criteria
            for (NeutralCriteria criteria : query.getCriteria()) {
                bqb.must(getQuery(criteria));
            }
            for (NeutralQuery nq : query.getOrQueries()) {
                bqb.should(getQuery(nq));
            }
            qb = bqb;
        }
        return qb;
    }

    @SuppressWarnings("unchecked")
    private static String[] getTermTokens(Object value) {
        return (value instanceof List) ? ((List<String>) value).toArray(new String[0]) : ((String) value).split(",");
    }
}