package org.slc.sli.api.service.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.QueryParseException;

/**
 * Neutral Query Converter Test
 * 
 * @author kmyers
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class UriInfoToNeutralQueryConverterTest {
    
    private static final UriInfoToNeutralQueryConverter QUERY_CONVERTER = new UriInfoToNeutralQueryConverter();
    

    private UriInfo uriInfo;
    private static final String URI_STRING = "http://localhost:8080/api/v1/sections";
    
    
    @Before
    public void setup() {
        uriInfo = mock(UriInfo.class);
    }

    @Test
    public void testNonNullForNull() {
        // should always return a null, so callers don't have to worry about null checking
        assertTrue(QUERY_CONVERTER.convert(null) != null);
    }

    @Test
    public void testFullParse() {
        this.testFullParse("ascending", NeutralQuery.SortOrder.ascending);
        this.testFullParse("descending", NeutralQuery.SortOrder.descending);
    }
    
    private void testFullParse(String sortOrderString, NeutralQuery.SortOrder sortOrder) {
        try {
            int offset = 5;
            int limit = 4;
            String includeFields = "field1,field2";
            String excludeFields = "field3,field4";
            String sortBy = "field5";
            
            String queryString = "";
            queryString += (ParameterConstants.OFFSET + "=" + offset);
            queryString += ("&");
            queryString += (ParameterConstants.LIMIT + "=" + limit);
            queryString += ("&");
            queryString += (ParameterConstants.INCLUDE_FIELDS + "=" + includeFields);
            queryString += ("&");
            queryString += (ParameterConstants.EXCLUDE_FIELDS + "=" + excludeFields);
            queryString += ("&");
            queryString += (ParameterConstants.SORT_BY + "=" + sortBy);
            queryString += ("&");
            queryString += (ParameterConstants.SORT_ORDER + "=" + sortOrderString);
            queryString += ("&");
            queryString += ("testKey" + "=" + "testValue");
            
            URI requestUri = new URI(URI_STRING + "?" + queryString);
            
            when(uriInfo.getRequestUri()).thenReturn(requestUri);

            NeutralQuery neutralQuery = QUERY_CONVERTER.convert(uriInfo);
            
            // test that the value was stored in the proper variable
            assertEquals(neutralQuery.getLimit(), limit);
            assertEquals(neutralQuery.getOffset(), offset);
            assertEquals(neutralQuery.getIncludeFields(), includeFields);
            assertEquals(neutralQuery.getExcludeFields(), excludeFields);
            assertEquals(neutralQuery.getSortBy(), sortBy);
            assertEquals(neutralQuery.getSortOrder(), sortOrder);
            assertEquals(neutralQuery.getCriteria().size(), 1);
        } catch (URISyntaxException urise) {
            assertTrue(false);
        }
    }
    
    @Test(expected = QueryParseException.class)
    public void testInvalidOffset() {
        try {
            String queryString = "offset=four";
            URI requestUri = new URI(URI_STRING + "?" + queryString);
            when(uriInfo.getRequestUri()).thenReturn(requestUri);
            //when(uriInfo.getRequestUri().getQuery()).thenReturn(queryString);
        } catch (URISyntaxException urise) {
            assertTrue(false);
        }
        
        QUERY_CONVERTER.convert(uriInfo);
    }

    @Test(expected = QueryParseException.class)
    public void testInvalidLimit() {
        try {
            String queryString = "limit=four";
            URI requestUri = new URI(URI_STRING + "?" + queryString);
            when(uriInfo.getRequestUri()).thenReturn(requestUri);
        } catch (URISyntaxException urise) {
            assertTrue(false);
        }
        
        QUERY_CONVERTER.convert(uriInfo);
    }
    

    @Test(expected = QueryParseException.class)
    public void testQueryStringMissingKey() {
        try {
            String queryString = "=4";
            URI requestUri = new URI(URI_STRING + "?" + queryString);
            when(uriInfo.getRequestUri()).thenReturn(requestUri);
        } catch (URISyntaxException urise) {
            assertTrue(false);
        }
        
        QUERY_CONVERTER.convert(uriInfo);
    }

    @Test(expected = QueryParseException.class)
    public void testQueryStringMissingOperator() {
        try {
            String queryString = "key4";
            URI requestUri = new URI(URI_STRING + "?" + queryString);
            when(uriInfo.getRequestUri()).thenReturn(requestUri);
        } catch (URISyntaxException urise) {
            assertTrue(false);
        }
        
        QUERY_CONVERTER.convert(uriInfo);
    }

    @Test(expected = QueryParseException.class)
    public void testQueryStringMissingValue() {
        try {
            String queryString = "key=";
            URI requestUri = new URI(URI_STRING + "?" + queryString);
            when(uriInfo.getRequestUri()).thenReturn(requestUri);
        } catch (URISyntaxException urise) {
            assertTrue(false);
        }
        
        QUERY_CONVERTER.convert(uriInfo);
    }
}

