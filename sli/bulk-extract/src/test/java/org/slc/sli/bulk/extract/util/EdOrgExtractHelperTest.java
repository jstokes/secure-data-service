package org.slc.sli.bulk.extract.util;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.dal.repository.MongoEntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

public class EdOrgExtractHelperTest {

    @InjectMocks
    EdOrgExtractHelper helper = new EdOrgExtractHelper();

    @Mock
    Repository<Entity> repository;

    @Before
    public void setup() throws Exception {
        repository = Mockito.mock(MongoEntityRepository.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getBulkExtractAppsTest() {
        when(repository.findAll("application", new NeutralQuery())).thenReturn(
                Arrays.asList(
                        buildAppEntity("1", true, true),
                        buildAppEntity("2", false, false),
                        buildAppEntity("3", true, false),
                        buildAppEntity("4", false, true),
                        buildAppEntity("5", true, true)
                )
        );

        Set<String> bulkExtractApps = helper.getBulkExtractApps();
        bulkExtractApps.removeAll(Arrays.asList("1","5"));
        assertTrue(bulkExtractApps.isEmpty());
    }

    @Test
    public void getBulkExtractEdOrgsPerAppTest() {
        getBulkExtractAppsTest(); // mock applications
        Map<String, Object> edorg1 = new HashMap<String, Object>();
        edorg1.put("authorizedEdorg", "edOrg1");
        Map<String, Object> edorg2 = new HashMap<String, Object>();
        edorg2.put("authorizedEdorg", "edOrg2");
        @SuppressWarnings("unchecked")
        Entity authOne = buildAuthEntity("1", Arrays.asList(edorg1, edorg2));
        Entity authTwo = buildAuthEntity("5", new ArrayList<Map<String, Object>>());

        List<Entity> auths = Arrays.asList(authOne, authTwo);
        when(repository.findAll(Mockito.eq("applicationAuthorization"), Mockito.any(NeutralQuery.class))).thenReturn(auths);

        Map<String, Set<String>> bulkExtractEdOrgsPerApp = helper.getBulkExtractEdOrgsPerApp();

        assertTrue(bulkExtractEdOrgsPerApp.size() == 2);
        assertTrue(bulkExtractEdOrgsPerApp.get(authOne.getBody().get("applicationId")).containsAll(
                Arrays.asList("edOrg1","edOrg2")));
        assertTrue(bulkExtractEdOrgsPerApp.get(authTwo.getBody().get("applicationId")).isEmpty());
    }

    private Entity buildAuthEntity(String applicationId, List<Map<String, Object>> edOrgs) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("applicationId", applicationId);
        body.put("edorgs", edOrgs);
        return new MongoEntity("applicationAuthorization", body);
    }


    private Entity buildAppEntity(String id, boolean isBulkExtract, boolean isApproved) {
        Map<String, Object> body = new HashMap<String, Object>();

        if (isBulkExtract) {
            body.put("isBulkExtract", isBulkExtract);
        }

        if (isApproved) {
            Map<String, Object> registration = new HashMap<String, Object>();
            registration.put("status", "APPROVED");
            body.put("registration", registration);
        }

        return new MongoEntity(EntityNames.APPLICATION, id, body, new HashMap<String, Object>());
    }

}
