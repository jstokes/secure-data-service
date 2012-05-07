package org.slc.sli.web.util;

import java.util.UUID;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Safe ID string
 * @author agrebneva
 *
 */
public class SafeUUID {
    @Size(max = 36, message = "Not a valid UUID")
    @Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")
    String uuid;

    public SafeUUID( ) {
    }

    public SafeUUID(String id) {
        setId(id);
    }

    public String getId() {
        return uuid;
    }

    public void setId(String uuid) {
        if (uuid != null) {
            this.uuid = UUID.fromString(uuid).toString();
        }
    }
}
