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


package org.slc.sli.api.security.resolve;

import java.util.List;

/**
 * Manager for mappings between client roles and SLI roles.
 *
 */
public interface ClientRoleResolver {

    /**
     * Given a list of client role names this returns the the corresponding SLI roles.
     * @param realmId
     * @param clientRoleNames
     * @return
     */
    public List<String> resolveRoles(String realmId, List<String> clientRoleNames);

}
