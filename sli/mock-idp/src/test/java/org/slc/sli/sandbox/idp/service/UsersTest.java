package org.slc.sli.sandbox.idp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.sandbox.idp.service.Users;
import org.slc.sli.sandbox.idp.service.Users.User;

@RunWith(MockitoJUnitRunner.class)
public class UsersTest {
    
    @Mock
    Repository<Entity> repo;
    
    @InjectMocks
    Users userService = new Users();
    
    @Test
    public void testGetAvailableUsers() {
        final Entity staffEntity = new Entity() {
            @Override
            public String getType() {
                return null;
            }
            
            @Override
            public String getEntityId() {
                return null;
            }
            
            @Override
            public Map<String, Object> getBody() {
                Map<String, Object> body = new HashMap<String, Object>();
                Map<String, Object> name = new HashMap<String, Object>();
                name.put("firstName", "a");
                name.put("lastSurname", "b");
                body.put("name", name);
                body.put("staffUniqueStateId", "1");
                return body;
            }
            
            @Override
            public Map<String, Object> getMetaData() {
                return null;
            }
        };
        
        final Entity teacherEntity = new Entity() {
            @Override
            public String getType() {
                return null;
            }
            
            @Override
            public String getEntityId() {
                return null;
            }
            
            @Override
            public Map<String, Object> getBody() {
                Map<String, Object> body = new HashMap<String, Object>();
                Map<String, Object> name = new HashMap<String, Object>();
                name.put("firstName", "c");
                name.put("lastSurname", "d");
                body.put("name", name);
                body.put("staffUniqueStateId", "2");
                return body;
            }
            
            @Override
            public Map<String, Object> getMetaData() {
                return null;
            }
        };
        
        Mockito.when(repo.findAll(Mockito.anyString(), Mockito.any(NeutralQuery.class))).thenAnswer(
                new Answer<Iterable<Entity>>() {
                    
                    @Override
                    public Iterable<Entity> answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        assertTrue("staff".equals(args[0]) || "teacher".equals(args[0]));
                        NeutralQuery query = (NeutralQuery) args[1];
                        assertEquals(1, query.getCriteria().size());
                        NeutralCriteria criteria = query.getCriteria().get(0);
                        
                        assertEquals(false, criteria.canBePrefixed());
                        assertEquals("metaData.tenantId", criteria.getKey());
                        assertEquals("=", criteria.getOperator());
                        assertEquals("TEST", criteria.getValue());
                        
                        if ("staff".equals(args[0])) {
                            return Arrays.asList(staffEntity);
                        } else {
                            return Arrays.asList(teacherEntity);
                        }
                    }
                });
        
        List<User> users = userService.getAvailableUsers("TEST");
        assertEquals(2, users.size());
        assertEquals("a", users.get(0).getFirstName());
        assertEquals("b", users.get(0).getLastName());
        assertEquals("1", users.get(0).getId());
        assertEquals("Staff", users.get(0).getType());
        assertEquals("c", users.get(1).getFirstName());
        assertEquals("d", users.get(1).getLastName());
        assertEquals("2", users.get(1).getId());
        assertEquals("Teacher", users.get(1).getType());
    }
}
