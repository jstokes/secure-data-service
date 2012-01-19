package org.slc.sli.unit.controller;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import com.google.gson.Gson;

import org.slc.sli.client.MockAPIClient;
import org.slc.sli.controller.StudentListController;
import org.slc.sli.entity.School;
import org.slc.sli.manager.SchoolManager;
import org.slc.sli.security.SLIPrincipal;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

/**
 * TODO: Write Javadoc
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StudentListController.class, MockAPIClient.class })
public class StudentListControllerTest {

    private StudentListController studentListController;
    private String testUser;
    
    @Before
    public void setup() {
        studentListController = new StudentListController();
        testUser = "sravan";
    }
    
    
    @Test
    public void testStudentListNotEmpty() throws Exception {
        
        MockAPIClient mockClient = PowerMockito.spy(new MockAPIClient());
        PowerMockito.doReturn("src/test/resources/mock_data/common/school.json").when(mockClient, "getFilename", "mock_data/common/school.json");
        School[] schools = mockClient.getSchools("common");
        
        ModelMap model = new ModelMap();
        StudentListController partiallyMocked = PowerMockito.spy(new StudentListController());
        SchoolManager schoolManager = PowerMockito.spy(new SchoolManager());
        PowerMockito.doReturn(schools).when(schoolManager, "getSchools");
        SLIPrincipal principal = new SLIPrincipal("demo", "demo", "active");
        PowerMockito.doReturn(principal).when(partiallyMocked, "getPrincipal");
        
        ModelAndView result;   
        partiallyMocked.setSchoolManager(schoolManager);
        result = partiallyMocked.retrieveStudentList(model);
        assertEquals(result.getViewName(), "studentList");
        String schoolListJson = (String) model.get("schoolList");
        Gson gson = new Gson();
        School[] retrievedList = gson.fromJson(schoolListJson, School[].class); 
        assertTrue(retrievedList.length > 0);
        int i = 0;
        for (School school : schools) {
            assertEquals(school.getNameOfInstitution(), retrievedList[i++].getNameOfInstitution());
        }

    }
    
    
    @Test
    public void testStudentListNullReturn() throws Exception {
        StudentListController mocked = PowerMockito.spy(new StudentListController());
        SchoolManager schoolManager = PowerMockito.spy(new SchoolManager());
        PowerMockito.doReturn(null).when(schoolManager, "getSchools");
        mocked.setSchoolManager(schoolManager);
        SLIPrincipal principal = new SLIPrincipal("demo", "demo", "active");
        PowerMockito.doReturn(principal).when(mocked, "getPrincipal");
        ModelMap model = new ModelMap();
        mocked.retrieveStudentList(model);
        assertTrue(model.get("schoolList").equals("null"));

    }
 
    /*
    @Test
    public void testApiClient() {
        Assert.assertNull(studentListController.getApiClient());
        MockAPIClient mock = new MockAPIClient();
        studentListController.setApiClient(mock);
        Assert.assertNotNull(studentListController.getApiClient());
        
        
    }
    */
}
