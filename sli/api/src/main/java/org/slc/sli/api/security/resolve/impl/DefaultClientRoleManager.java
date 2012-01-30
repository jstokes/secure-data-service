package org.slc.sli.api.security.resolve.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.api.security.resolve.ClientRoleManager;
import org.slc.sli.api.security.resolve.RealmRoleMappingException;
import org.slc.sli.api.security.roles.Role;
import org.slc.sli.api.security.roles.RoleRightAccess;

/**
 * Default converter for client roles to sli roles
 * Does absolutely nothing but give back exactly same list
 * 
 * @author dkornishev
 * 
 */
@Component
public class DefaultClientRoleManager implements ClientRoleManager {
    
    @Autowired
    private RoleRightAccess roleRightAccess;
    
    @Override
    /**
     */
    public List<String> resolveRoles(String realmId, List<String> clientRoleNames) {
        List<String> result = new ArrayList<String>();
        for (String clientRoleName : clientRoleNames) {
            if (roleRightAccess.findRoleByName(clientRoleName) != null) {
                result.add(clientRoleName);
            } else {
                String sliRoleName = getSliRoleName(realmId, clientRoleName);
                if (sliRoleName != null) {
                    result.add(sliRoleName);
                }
            }
        }
        return result;
    }
    
    @Override
    public boolean addClientRole(String realmId, String sliRoleName, String clientRoleName) throws RealmRoleMappingException {
        Role sliRole = roleRightAccess.findRoleByName(sliRoleName);
        Map<String, List<String>> mappings = sliRole.getRealmRoleMappings();
        List<String> listMappingsForRealm = mappings.get(realmId);
        if (listMappingsForRealm == null) {
            listMappingsForRealm = new ArrayList<String>();
        }
        
        if (!listMappingsForRealm.contains(clientRoleName)) {
            listMappingsForRealm.add(clientRoleName);
        } else {
            throw new RealmRoleMappingException("Role mapping already exists");
        }
        mappings.put(realmId, listMappingsForRealm);
        sliRole.setRealmRoleMappings(mappings);
        return roleRightAccess.updateRole(sliRole);
    }
    
    @Override
    public String getSliRoleName(String realmId, String clientRoleName) {
        List<Role> allSliRoles = roleRightAccess.fetchAllRoles();
        for (Role role : allSliRoles) {
            List<String> clientRoles = role.getRealmRoleMappings().get(realmId);
            if (clientRoles != null) {
                for (String clientRole : clientRoles) {
                    if (clientRole.equals(clientRoleName)) {
                        return role.getName();
                    }
                }
            }
        }
        return null;
    }
    
    public void setRoleRightAccess(RoleRightAccess roleRightAccess) {
        this.roleRightAccess = roleRightAccess;
    }
    
    @Override
    public List<String> getMappings(String realmId, String sliRoleName) {
        Role role = roleRightAccess.findRoleByName(sliRoleName);
        if (role != null) {
            Map<String, List<String>> mappings = role.getRealmRoleMappings();
            return mappings.get(realmId);
        }
        return null;
    }
    
    @Override
    public boolean deleteClientRole(String realmId, String clientRoleName) {
        List<Role> allSliRoles = roleRightAccess.fetchAllRoles();
        for (Role role : allSliRoles) {
            Map<String, List<String>> mappings = role.getRealmRoleMappings();
            List<String> clientRoles = mappings.get(realmId);
            if (clientRoles != null && clientRoles.contains(clientRoleName)) {
                clientRoles.remove(clientRoleName);
                mappings.put(realmId, clientRoles);
                role.setRealmRoleMappings(mappings);
                return roleRightAccess.updateRole(role);
            }
        }
        return false;
    }
    
}
