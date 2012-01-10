package org.slc.sli.manager;

import org.slc.sli.entity.Student;
import org.slc.sli.config.ViewConfig;
import org.slc.sli.config.Field;
import org.slc.sli.config.ConfigUtil;
import org.slc.sli.client.MockAPIClient;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * StudentManager supplies student data to the controllers.
 * Given a configuration object, it will source the correct data, apply
 * the necessary business logic, and return the results. 
 * 
 * @author dwu
 *
 */
public class StudentManager {
    
    private static StudentManager instance = new StudentManager();
    
    protected StudentManager() {        
    }
    
    public static StudentManager getInstance() {
        return instance;
    }
    
    public List<Student> getStudentInfo(String username, List<String> studentIds, ViewConfig config) {
        
        // extract the studentInfo data fields
        List<Field> dataFields = ConfigUtil.getDataFields(config, "studentInfo");
        
        // call the api
        // TODO: do we need more logic to grab the correct fields?
        List<Student> studentInfo = new ArrayList<Student>();
        if (dataFields.size() > 0) {
            MockAPIClient apiClient = new MockAPIClient();
            studentInfo.addAll(Arrays.asList(apiClient.getStudents(username, studentIds)));
        }
        
        // return the results
        return studentInfo;
    }
    
    
}
