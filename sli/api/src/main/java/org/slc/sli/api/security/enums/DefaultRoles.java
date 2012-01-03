package org.slc.sli.api.security.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple enum that describes the default roles in terms of their rights.
 * 
 * Also has a few utility functions to see if a role contains a right.
 */
public enum DefaultRoles {
    EDUCATOR("Educator", new Rights[] { Rights.AGGREGATE_READ, Rights.READ_GENERAL }), LEADER("Leader", new Rights[] { Rights.AGGREGATE_READ, Rights.READ_GENERAL, Rights.READ_RESTRICTED }), AGGREGATOR("Aggregate Viewer",
            new Rights[] { Rights.AGGREGATE_READ }), ADMINISTRATOR("IT Administrator", new Rights[] { Rights.AGGREGATE_READ, Rights.READ_GENERAL, Rights.READ_RESTRICTED, Rights.WRITE_GENERAL, Rights.WRITE_RESTRICTED }), NONE("None",
            new Rights[] {});
    private final String   name;
    private final Rights[] rights;
    
    public String getRoleName() {
        return name;
    }

    public String getSpringRoleName() {
        return "ROLE_" + name.toUpperCase().replace(' ', '_');
    }

    public List<Rights> getRights() {
        return Arrays.asList(rights);
    }

    public boolean hasRight(Rights right) {
        for (Rights checkedRight : rights) {
            if (checkedRight == right) {
                return true;
            }
        }
        return false;
    }

    private DefaultRoles(String role, Rights[] rights) {
        name = role;
        this.rights = rights;
    }

    public static List<String> getDefaultRoleNames() {
        List<String> names = new ArrayList<String>();
        for (DefaultRoles role : DefaultRoles.values()) {
            names.add(role.getRoleName());
        }
        return names;
    }
}
