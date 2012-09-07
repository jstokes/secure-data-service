package org.slc.sli.modeling.wadl.helpers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import junit.framework.TestCase;

import org.junit.Test;
import org.slc.sli.modeling.rest.Method;
import org.slc.sli.modeling.rest.Param;
import org.slc.sli.modeling.rest.Request;
import org.slc.sli.modeling.rest.Resource;

/**
 * JUnit test for WadlHelper class.
 * 
 * User: wscott
 */
public class WadlHelperTest extends TestCase {
    @Test
    public void testGetRequestParams() throws Exception {
        Param mockParam = mock(Param.class);
        Request mockRequest = mock(Request.class);
        List<Param> mockParamList = new ArrayList<Param>(1);
        mockParamList.add(mockParam);
        when(mockRequest.getParams()).thenReturn(mockParamList);
        Method mockMethod = mock(Method.class);
        when(mockMethod.getRequest()).thenReturn(mockRequest);
        
        List<Param> paramList = WadlHelper.getRequestParams(mockMethod);
        assertEquals(1, paramList.size());
    }
    
    @Test
    public void testComputeId() throws Exception {
        
    }
    
    @Test
    public void testTitleCase() throws Exception {
        String camelCase = "randomName";
        String titleCase = "RandomName";
        assertEquals(titleCase, WadlHelper.titleCase(camelCase));
        
    }
    
    @Test
    public void testReverse() throws Exception {
        @SuppressWarnings({ "serial" })
        List<Integer> strList = new ArrayList<Integer>(3) {
            {
                add(1);
                add(2);
                add(3);
            }
        };
        
        List<Integer> revList = WadlHelper.reverse(strList);
        assertEquals(3, revList.size());
        assertEquals(3, (int) revList.get(0));
        assertEquals(2, (int) revList.get(1));
        assertEquals(1, (int) revList.get(2));
    }
    
    @Test
    public void testIsVersion() throws Exception {
        String versionString = "v1";
        String versionStringUpperCase = "V1";
        String nonVersionString = "blah";
        
        assertTrue(WadlHelper.isVersion(versionString));
        assertTrue(WadlHelper.isVersion(versionStringUpperCase));
        assertFalse(WadlHelper.isVersion(nonVersionString));
        
    }
    
    @Test
    public void testParseTemplateParam() throws Exception {
        String templateParam = "{paramName}";
        
        assertEquals("paramName", WadlHelper.parseTemplateParam(templateParam));
    }
    
    @Test
    public void testToSteps() throws Exception {
        Resource mockResource = mock(Resource.class);
        when(mockResource.getPath()).thenReturn("students/{id}");
        
        Resource mockAncestorResource = mock(Resource.class);
        when(mockAncestorResource.getPath()).thenReturn("api/v1");
        
        Stack<Resource> resources = new Stack<Resource>();
        resources.push(mockAncestorResource);
        
        List<String> steps = WadlHelper.toSteps(mockResource, resources);
        assertEquals(4, steps.size());
        assertEquals("api", steps.get(0));
        assertEquals("v1", steps.get(1));
        assertEquals("students", steps.get(2));
        assertEquals("{id}", steps.get(3));
    }
    
    @Test
    public void testIsTemplateParam() throws Exception {
        String templateParam = "{paramName}";
        String nonTemplateParam = "blah";
        
        assertTrue(WadlHelper.isTemplateParam(templateParam));
        assertFalse(WadlHelper.isTemplateParam(nonTemplateParam));
    }
}
