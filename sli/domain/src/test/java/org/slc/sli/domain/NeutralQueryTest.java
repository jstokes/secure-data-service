package org.slc.sli.domain;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * Various utility functions for test
 *
 * @author kmyers
 *
 */
public class NeutralQueryTest {

    @Test
    public void testDefaultConstructor() {
        NeutralQuery neutralQuery = new NeutralQuery();
        assertEquals(neutralQuery.getCriteria().size(), 0);
        assertEquals(neutralQuery.getLimit(), 0);
        assertEquals(neutralQuery.getOffset(), 0);
        assertEquals(neutralQuery.getIncludeFields(), null);
        assertEquals(neutralQuery.getExcludeFields(), null);
        assertEquals(neutralQuery.getSortBy(), null);
        assertEquals(neutralQuery.getSortOrder(), null);
    }

    @Test
    public void testEquals() {
        String includeFields = "field1,field2";
        String excludeFields = "field3,field4";
        String sortBy = "field5";
        int offset = 4;
        int limit = 5;
        NeutralQuery.SortOrder sortOrderAscending = NeutralQuery.SortOrder.ascending;
        NeutralQuery.SortOrder sortOrderDescending = NeutralQuery.SortOrder.descending;

        NeutralQuery neutralQuery1 = new NeutralQuery();
        neutralQuery1.setIncludeFields(includeFields);
        neutralQuery1.setExcludeFields(excludeFields);
        neutralQuery1.setLimit(limit);
        neutralQuery1.setOffset(offset);
        neutralQuery1.setSortBy(sortBy);
        neutralQuery1.setSortOrder(sortOrderAscending);

        NeutralQuery neutralQuery2 = new NeutralQuery(neutralQuery1);
        assertTrue(neutralQuery1.equals(neutralQuery2));
        assertTrue(neutralQuery1 != neutralQuery2);

        // change value on query 1, assert two are no longer equal,
        // then change value on query 2 to match, confirm equal again
        neutralQuery1.setIncludeFields("");
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.setIncludeFields("");
        assertTrue(neutralQuery1.equals(neutralQuery2));

        // change value on query 1, assert two are no longer equal,
        // then change value on query 2 to match, confirm equal again
        neutralQuery1.setExcludeFields("");
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.setExcludeFields("");
        assertTrue(neutralQuery1.equals(neutralQuery2));

        // change value on query 1, assert two are no longer equal,
        // then change value on query 2 to match, confirm equal again
        neutralQuery1.setLimit(7);
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.setLimit(7);
        assertTrue(neutralQuery1.equals(neutralQuery2));

        // change value on query 1, assert two are no longer equal,
        // then change value on query 2 to match, confirm equal again
        neutralQuery1.setOffset(9);
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.setOffset(9);
        assertTrue(neutralQuery1.equals(neutralQuery2));

        // change value on query 1, assert two are no longer equal,
        // then change value on query 2 to match, confirm equal again
        neutralQuery1.setSortBy("");
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.setSortBy("");
        assertTrue(neutralQuery1.equals(neutralQuery2));

        // change value on query 1, assert two are no longer equal,
        // then change value on query 2 to match, confirm equal again
        neutralQuery1.setSortOrder(sortOrderDescending);
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.setSortOrder(sortOrderDescending);
        assertTrue(neutralQuery1.equals(neutralQuery2));

        // change value on query 1, assert two are no longer equal,
        // then change value on query 2 to match, confirm equal again
        neutralQuery1.addCriteria(new NeutralCriteria("x=1"));
        neutralQuery2.addCriteria(new NeutralCriteria("x=2"));
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery1.addCriteria(new NeutralCriteria("x=2"));
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.addCriteria(new NeutralCriteria("x=1"));
        assertTrue(neutralQuery1.equals(neutralQuery2));


        NeutralQuery neutralQuery3 = new NeutralQuery(neutralQuery1);
        neutralQuery1.addOrQuery(neutralQuery3);
        neutralQuery2.addOrQuery(new NeutralQuery());
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery2.addOrQuery(neutralQuery3);
        assertFalse(neutralQuery1.equals(neutralQuery2));
        neutralQuery1.addOrQuery(new NeutralQuery());
        assertTrue(neutralQuery1.equals(neutralQuery2));
    }

    @Test
    public void testCloneConstructor() {
        String includeFields = "field1,field2";
        String excludeFields = "field3,field4";
        String sortBy = "field5";
        int offset = 4;
        int limit = 5;
        NeutralQuery.SortOrder sortOrderAscending = NeutralQuery.SortOrder.ascending;
        NeutralQuery.SortOrder sortOrderDescending = NeutralQuery.SortOrder.descending;


        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setIncludeFields(includeFields);
        neutralQuery.setExcludeFields(excludeFields);
        neutralQuery.setLimit(limit);
        neutralQuery.setOffset(offset);
        neutralQuery.setSortBy(sortBy);
        neutralQuery.setSortOrder(sortOrderAscending);

        //confirm neutralquery was created right
        assertEquals(neutralQuery.getCriteria().size(), 0);
        assertEquals(neutralQuery.getLimit(), limit);
        assertEquals(neutralQuery.getOffset(), offset);
        assertEquals(neutralQuery.getIncludeFields(), includeFields);
        assertEquals(neutralQuery.getExcludeFields(), excludeFields);
        assertEquals(neutralQuery.getSortBy(), sortBy);
        assertEquals(neutralQuery.getSortOrder(), sortOrderAscending);

        //clone neutral query into neutral query 2
        NeutralQuery neutralQuery2 = new NeutralQuery(neutralQuery);

        //confirm all properties were copied
        assertEquals(neutralQuery.getCriteria().size(), neutralQuery2.getCriteria().size());
        assertEquals(neutralQuery.getLimit(), neutralQuery2.getLimit());
        assertEquals(neutralQuery.getOffset(), neutralQuery2.getOffset());
        assertEquals(neutralQuery.getIncludeFields(), neutralQuery2.getIncludeFields());
        assertEquals(neutralQuery.getExcludeFields(), neutralQuery2.getExcludeFields());
        assertEquals(neutralQuery.getSortBy(), neutralQuery2.getSortBy());
        assertEquals(neutralQuery.getSortOrder(), neutralQuery2.getSortOrder());

        //change all properties for neutral query 1
        neutralQuery.setIncludeFields("");
        neutralQuery.setExcludeFields("");
        neutralQuery.setLimit(0);
        neutralQuery.setOffset(0);
        neutralQuery.setSortBy("");
        neutralQuery.setSortOrder(sortOrderDescending);
        neutralQuery.addCriteria(new NeutralCriteria("x=1"));

        //confirm no "entanglement" between query 1 and query 2
        assertFalse(neutralQuery.getCriteria().size() == neutralQuery2.getCriteria().size());
        assertFalse(neutralQuery.getLimit() == neutralQuery2.getLimit());
        assertFalse(neutralQuery.getOffset() == neutralQuery2.getOffset());
        assertFalse(neutralQuery.getIncludeFields().equals(neutralQuery2.getIncludeFields()));
        assertFalse(neutralQuery.getExcludeFields().equals(neutralQuery2.getExcludeFields()));
        assertFalse(neutralQuery.getSortBy().equals(neutralQuery2.getSortBy()));
        assertFalse(neutralQuery.getSortOrder().equals(neutralQuery2.getSortOrder()));
    }

    @Test
    public void testSingleCriteriaConstructor() {
        NeutralQuery neutralQuery = new NeutralQuery(new NeutralCriteria("x=1"));
        assertEquals(neutralQuery.getCriteria().size(), 1);
        assertEquals(neutralQuery.getLimit(), 0);
        assertEquals(neutralQuery.getOffset(), 0);
        assertEquals(neutralQuery.getIncludeFields(), null);
        assertEquals(neutralQuery.getExcludeFields(), null);
        assertEquals(neutralQuery.getSortBy(), null);
        assertEquals(neutralQuery.getSortOrder(), null);
    }

    @Test
    public void testGetterAndSetterMethods() {
        String includeFields = "field1,field2";
        String excludeFields = "field3,field4";
        String sortBy = "field5";
        int offset = 4;
        int limit = 5;
        NeutralQuery.SortOrder sortOrderAscending = NeutralQuery.SortOrder.ascending;
        NeutralQuery.SortOrder sortOrderDescending = NeutralQuery.SortOrder.descending;


        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setIncludeFields(includeFields);
        neutralQuery.setExcludeFields(excludeFields);
        neutralQuery.setLimit(limit);
        neutralQuery.setOffset(offset);
        neutralQuery.setSortBy(sortBy);
        neutralQuery.setSortOrder(sortOrderAscending);

        assertEquals(neutralQuery.getCriteria().size(), 0);
        assertEquals(neutralQuery.getLimit(), limit);
        assertEquals(neutralQuery.getOffset(), offset);
        assertEquals(neutralQuery.getIncludeFields(), includeFields);
        assertEquals(neutralQuery.getExcludeFields(), excludeFields);
        assertEquals(neutralQuery.getSortBy(), sortBy);
        assertEquals(neutralQuery.getSortOrder(), sortOrderAscending);

        neutralQuery.setSortOrder(sortOrderDescending);
        assertEquals(neutralQuery.getSortOrder(), sortOrderDescending);
    }

    @Test
    public void testToString() {
        assertNotNull(new NeutralQuery().toString());
    }

}
