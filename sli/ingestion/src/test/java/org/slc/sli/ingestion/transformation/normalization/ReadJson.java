package org.slc.sli.ingestion.transformation.normalization;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

/**
 * Json Configuration Unit Tests.
 *
 * @author npandey
 *
 */
public class ReadJson {
    @Test
    public void testTeacherSectionAssociation() throws Exception {
        File jsonFile = new File("src/test/resources/TeacherSectionAssociation.json");
        ObjectMapper mapper = new ObjectMapper();
        EntityConfig teacherSectionAssociation = mapper.readValue(jsonFile, EntityConfig.class);
        assertEquals("metadata.externalId", teacherSectionAssociation.getReferences().get(0).getRef().getChoiceOfFields().get(0).get(0).getValues().get(0).getValueSource());
        assertEquals("Section" , teacherSectionAssociation.getReferences().get(1).getRef().getCollectionName());
    }
}
