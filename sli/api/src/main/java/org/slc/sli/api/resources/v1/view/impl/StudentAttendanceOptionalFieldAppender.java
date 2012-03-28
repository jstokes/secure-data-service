package org.slc.sli.api.resources.v1.view.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.view.AbstractOptionalFieldAppender;

/**
 * Provides data about students and attendance to construct the custom
 * views returned by the api
 * @author srupasinghe
 *
 */
@Component
public class StudentAttendanceOptionalFieldAppender extends AbstractOptionalFieldAppender {

    public StudentAttendanceOptionalFieldAppender() {
    }
    
    @Override
    public List<EntityBody> applyOptionalField(List<EntityBody> entities) {
        
        EntityBody b = new EntityBody();
        b.put("attendances", "studentattendances");
        
        entities.add(b);
        
        return entities;
    }
    
}
