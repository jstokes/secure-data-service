/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.api.security.oauth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.context.resolver.EdOrgHelper;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Determines which applications a given user is authorized to use based on
 * that user's ed-org.
 *
 * @author pwolf
 *
 */
@Component
public class ApplicationAuthorizationValidator {

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repo;
    
    @Autowired
    private EdOrgHelper helper;
    
    @Autowired
    @Value("${sli.sandbox.enabled}")
    private boolean sandboxEnabled;
    
    /**
     * Get the list of authorized apps for the user based on the user's LEA.
     *
     * No additional filtering is done on the results. E.g. if a user is a non-admin,
     * the admin apps will still show up in the list, or if an app is disabled it will
     * still show up.
     *
     * @param principal
     *
     * @return list of app IDs, or null if it couldn't be determined
     */
    @SuppressWarnings("unchecked")
    public List<String> getAuthorizedApps(SLIPrincipal principal) {
        
        //For hosted users (Developer, SLC Operator, SEA/LEA Administrator) they're not associated with a district
        List<Entity> districts = SecurityUtil.isHostedUser(repo, principal) ? new ArrayList<Entity>() : findUsersDistricts(principal);
        
        Set<String> bootstrapApps = getDefaultAllowedApps();
        Set<String> results = getDefaultAuthorizedApps();
                
        for (Entity district : districts) {
            debug("User is in district {}.", district.getEntityId());

            NeutralQuery query = new NeutralQuery();
            query.addCriteria(new NeutralCriteria("authId", "=", district.getBody().get("stateOrganizationId")));
            query.addCriteria(new NeutralCriteria("authType", "=", "EDUCATION_ORGANIZATION"));
            Entity authorizedApps = repo.findOne("applicationAuthorization", query);

            if (authorizedApps != null) {
                
                NeutralQuery districtQuery = new NeutralQuery(0);
                districtQuery.addCriteria(new NeutralCriteria("authorized_ed_orgs", "=", district.getBody().get(
                        "stateOrganizationId")));
                
                Set<String> vendorAppsEnabledForEdorg = new HashSet<String>(bootstrapApps); //bootstrap apps automatically added
                
                for (String id : repo.findAllIds("application", districtQuery)) {
                    vendorAppsEnabledForEdorg.add(id);
                }
                
                //Intersection
                vendorAppsEnabledForEdorg.retainAll((List<String>) authorizedApps.getBody().get("appIds"));
                
                results.addAll(vendorAppsEnabledForEdorg);
            }
        }
 
        return new ArrayList<String>(results);
    }
    
    /**
     * These are the apps that are auto-authorized, i.e. the district admin doesn't
     * need to manually authorize the application. 
     * @return
     */
    private Set<String> getDefaultAuthorizedApps() {
        Set<String> toReturn = new HashSet<String>();
        NeutralQuery autoAuthQuery = new NeutralQuery(0);
        autoAuthQuery.addCriteria(new NeutralCriteria("authorized_for_all_edorgs", "=", true));
        Iterable<Entity> autoAuthApps = repo.findAll("application", autoAuthQuery);
        
        for (Entity currentApp : autoAuthApps) {
            toReturn.add(currentApp.getEntityId());
        }
        return toReturn;
    }
    
    /**
     * These are apps that are auto-allowed, i.e. the app developer doesn't need
     * to select the districts that can use the app.
     * @return
     */
    private Set<String> getDefaultAllowedApps() {
        Set<String> toReturn = new HashSet<String>();
        NeutralQuery bootstrapQuery = new NeutralQuery(0);
        bootstrapQuery.addCriteria(new NeutralCriteria("allowed_for_all_edorgs", "=", true));
        Iterable<Entity> bootstrapApps = repo.findAll("application", bootstrapQuery);
        
        for (Entity currentApp : bootstrapApps) {
            toReturn.add(currentApp.getEntityId());
        }
        return toReturn;
    }
    
    
    private boolean isSandbox() {
        return sandboxEnabled;
    }

    /**
     * Looks up the user's LEA entity.
     *
     * Currently it returns a list of all LEAs the user might be associated with.
     * In the case there's a hierarchy of LEAs, all are returned in no particular order.
     *
     * Don't expect this to work for hosted users (they'll end up resolving to everything).
     * 
     * @param principal
     * @return a list of accessible LEAs
     */
    private List<Entity> findUsersDistricts(SLIPrincipal principal) {
        List<Entity> toReturn = new ArrayList<Entity>();
        
        List<String> leaIds = helper.getDistricts(principal.getEntity());
        
        NeutralQuery query = new NeutralQuery(0);
        query.addCriteria(new NeutralCriteria("_id", "in", leaIds, false));
        for (Entity entity : repo.findAll(EntityNames.EDUCATION_ORGANIZATION, query)) {
            toReturn.add(entity);
        }
       
        return toReturn;
    }


}
