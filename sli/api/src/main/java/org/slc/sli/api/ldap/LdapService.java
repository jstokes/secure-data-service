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

package org.slc.sli.api.ldap;

import java.util.Collection;

import org.springframework.ldap.NameAlreadyBoundException;

/**
 * define the interface for basic CRUD and search operations on LDAP
 *
 * @author dliu
 *
 */
// TODO move the ldap package out from api to common, so to reduce duplicate code with simple idp

public interface LdapService {

    public static final String CREATE_TIMESTAMP = "createTimestamp";
    public static final String MODIFY_TIMESTAMP = "modifyTimestamp";

    public User getUser(String realm, String uid);

    public Collection<Group> getUserGroups(String realm, String uid);

    public Group getGroup(String realm, String groupName);

    public void removeUser(String realm, String uid);

    public String createUser(String realm, User user) throws NameAlreadyBoundException;

    public boolean updateUser(String realm, User user);

    public boolean updateGroup(String realm, Group group);

    public Collection<User> findUsersByGroups(String realm, Collection<String> groupNames);

    public Collection<User> findUsersByAttributes(String realm, Collection<String> attributes);

    public Collection<User> findUsersByGroups(String realm, Collection<String> groupNames, String tenant);

    public Collection<User> findUsersByGroups(String realm, Collection<String> groupNames, String tenant, Collection<String> edorgs);

    public Collection<User> findUsersByGroups(String realm, Collection<String> allowedGroupNames, Collection<String> disallowedGroupNames, String tenant, Collection<String> edorgs);
}
