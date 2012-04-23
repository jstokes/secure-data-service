package org.slc.sli.domain.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * A simple enum describing our basic rights that are required.
 * ADMIN_ACCESS -> allows operations on entities in Admin Sphere
 * FULL_ACCESS -> allows operations on all entities everywhere without regard for associations
 */
public enum Right implements GrantedAuthority {
    ANONYMOUS_ACCESS, READ_GENERAL, WRITE_GENERAL, READ_RESTRICTED, WRITE_RESTRICTED, AGGREGATE_READ, AGGREGATE_WRITE, ADMIN_ACCESS, FULL_ACCESS, APP_REGISTER, APP_EDORG_SELECT;

    @Override
    public String getAuthority() {
        return this.toString();
    }

}
