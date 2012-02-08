package org.slc.sli.validation.schema;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;

import org.slc.sli.domain.Entity;
import org.slc.sli.validation.EntityValidationRepository;
import org.slc.sli.validation.ValidationError;

/**
 * Tests the ReferenceSchema methods
 * @author srupasinghe
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class ReferenceSchemaTest {
    @Autowired
    ReferenceSchema schema;
    
    private ReferenceSchema spySchema;
    private ValidationRepo repo = new ValidationRepo();
    private static final String UUID = "123456789";
    private static final String COLLECTION = "section";
    private static final String FIELDNAME = "courseId";
    
    @Before
    public void setup() {
        Entity entity = mock(Entity.class);
        when(entity.getEntityId()).thenReturn(UUID);
        
        repo.addEntity(COLLECTION, entity);
        
        AppInfo info = mock(AppInfo.class);
        when(info.getReferenceType()).thenReturn(COLLECTION);
        
        spySchema = spy(schema);
        when(spySchema.getAppInfo()).thenReturn(info);
    }
    
    @Test
    public void testReferenceValidation() throws IllegalArgumentException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        
        assertTrue("Reference entity validation failed", spySchema.validate(FIELDNAME, UUID, errors, repo));
    }
    
    @Test
    public void testInvalidReferenceValidation() throws IllegalArgumentException {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        
        assertFalse("Invalid Reference entity validation failed", spySchema.validate(FIELDNAME, "45679", errors, repo));
    }
    
    /**
     * Mock repo class
     * @author srupasinghe
     *
     */
    class ValidationRepo implements EntityValidationRepository {
        Map<String, List<Entity>> data = new HashMap<String, List<Entity>>();
        
        public void addEntity(String collectionName, Entity entity) {
            if (data.get(collectionName) != null) {
               data.get(collectionName).add(entity); 
            } else {
                List<Entity> list = new ArrayList<Entity>();
                list.add(entity);
                
                data.put(collectionName, list);
            }
        }

        @Override
        public Entity find(String collectioName, String id) {
            List<Entity> list = data.get(collectioName);
            
            for (Entity e : list) {
                if (e.getEntityId().equals(id)) {
                    return e;
                }
            }
            
            return null;
        }

        @Override
        public Iterable<Entity> findAll(String collectionName, int skip, int max) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterable<Entity> findAll(String collectioName) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterable<Entity> findByFields(String collectionName, Map<String, String> fields, int skip, int max) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Iterable<Entity> findByFields(String collectionName, Map<String, String> fields) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
}
