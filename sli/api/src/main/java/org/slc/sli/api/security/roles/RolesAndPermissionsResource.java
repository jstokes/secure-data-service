package org.slc.sli.api.security.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * A RESTful class to return the roles and their configured rights.
 * 
 * This is meant to be a read-only operation, but contains a convenience post
 * method to create new roles.
 * 
 * @see org.slc.sli.api.security.enums.Right
 * @see org.slc.sli.api.security.roles.Role
 */
@Path("/admin/roles")
@Component
@Scope("request")
@Produces("application/json")
public class RolesAndPermissionsResource {
    
    public static final int NUM_RESULTS = 100;
    @Autowired
    private RoleRightAccess roleAccessor;
    
    private static final Logger LOG = LoggerFactory.getLogger(RolesAndPermissionsResource.class);
    
    /**
     * Fetches the first 100 roles listed in the system to be serialized to json
     * This is intended to be a restful API call.
     * 
     * @return an object that is technically a list of maps that are the roles
     */
    @GET
    @Path("/")
    @PreAuthorize("hasRole('READ_ROLES')")
    public List<Map<String, Object>> getRolesAndPermissions() {
        List<Map<String, Object>> roleList = new ArrayList<Map<String, Object>>();
        List<Role> roles = roleAccessor.fetchAllRoles();
        for (Role role : roles) {
            roleList.add(role.getRoleAsEntityBody());
        }
        return roleList;
    }
    
    /**
     * A simple method to add a new role to the database.
     * 
     * @param name
     *            the name of the new role (eg: Educator)
     * @param rights
     *            some list of rights to be added
     * @see org.slc.sli.api.security.enums.Right
     */
    @POST
    @Path("/")
    public boolean createRoleWithPermission(String name, List<String> rights) {
        // TODO prevent default role manipulation
        return roleAccessor.addRole(RoleBuilder.makeRole(name).addRights(rights).build());
    }
    
    // Injection method
    public void setRoleAccessor(RoleRightAccess roleRights) {
        this.roleAccessor = roleRights;
    }
    
}
