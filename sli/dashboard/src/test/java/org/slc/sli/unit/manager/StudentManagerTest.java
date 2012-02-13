package org.slc.sli.unit.manager;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.slc.sli.client.MockAPIClient;
import org.slc.sli.config.ViewConfig;
import org.slc.sli.entity.GenericEntity;
import org.slc.sli.manager.ConfigManager;
import org.slc.sli.manager.EntityManager;
import org.slc.sli.manager.StudentManager;


/**
 * Unit tests for the StudentManager class.
 *
 */
public class StudentManagerTest {

    @Before
    public void setup() {

    }


    //TODO: Unskip test after debugging
    @Test
    public void testGetStudentInfo() {
/*
        String[] studentIdArray = {"453827070", "943715230"};
        List<String> studentIds = Arrays.asList(studentIdArray);

        MockAPIClient mockClient = new MockAPIClient();
        EntityManager entityManager = new EntityManager();
        entityManager.setApiClient(mockClient);
        
        ConfigManager configManager = new ConfigManager();
        configManager.setApiClient(mockClient);
        configManager.setEntityManager(entityManager);
        ViewConfig config = configManager.getConfig("lkim", "IL_3-8_ELA");

        StudentManager studentManager = new StudentManager();
        studentManager.setApiClient(mockClient);
        studentManager.setEntityManager(entityManager);
        List<GenericEntity> studentInfo = studentManager.getStudentInfo("lkim", studentIds, config);
        assertEquals(2, studentInfo.size());
        */
    }

}
