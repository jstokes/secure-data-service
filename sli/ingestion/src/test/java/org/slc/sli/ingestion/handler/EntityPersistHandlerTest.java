package org.slc.sli.ingestion.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.dal.repository.MongoEntityRepository;
import org.slc.sli.domain.Entity;
import org.slc.sli.ingestion.FaultsReport;
import org.slc.sli.ingestion.NeutralRecordEntity;
import org.slc.sli.ingestion.transformation.SimpleEntity;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.slc.sli.validation.EntityValidationException;
import org.slc.sli.validation.ValidationError;
import org.slc.sli.validation.ValidationError.ErrorType;

/**
 * Tests for EntityPersistHandler
 *
 * @author dduran
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })

public class EntityPersistHandlerTest {

    @Autowired
    private EntityPersistHandler entityPersistHandler;

    private MongoEntityRepository mockedEntityRepository;

    private static final String STUDENT_ID = "765432";
    private static final String SCHOOL_ID = "654321";
    private static final String INTERNAL_STUDENT_ID = "0x" + STUDENT_ID;
    private static final String INTERNAL_SCHOOL_ID = "0x" + SCHOOL_ID;
    private static final String BAD_STUDENT_ID = "234567";
    private static final String REGION_ID = "https://devapp1.slidev.org:443/sp";
    private static final String METADATA_BLOCK = "metaData";
    private static final String REGION_ID_FIELD = "idNamespace";
    private static final String EXTERNAL_ID_FIELD = "externalId";

    private final Map<String, String> schoolFilterFields = new HashMap<String, String>();
    private final Map<String, String> studentSchoolAssociationFilterFields = new HashMap<String, String>();
    private final LinkedList<Entity> studentList = new LinkedList<Entity>();
    private final Iterable<Entity> studentFound = studentList;
    private final LinkedList<Entity> schoolList = new LinkedList<Entity>();
    private final Iterable<Entity> schoolFound = schoolList;
    private final LinkedList<Entity> studentSchoolAssociationList = new LinkedList<Entity>();
    private final Iterable<Entity> studentSchoolAssociationFound = studentSchoolAssociationList;

    @Before
    public void setup() {
        mockedEntityRepository = mock(MongoEntityRepository.class);
        entityPersistHandler.setEntityRepository(mockedEntityRepository);
<<<<<<< HEAD
        EntityConfigFactory entityConfigFactory = new EntityConfigFactory();
        entityConfigFactory.setSearchPath(new ClassPathResource("/smooksEdFi2SLI/"));
        entityPersistHandler.setEntityConfigurations(entityConfigFactory);
=======
>>>>>>> 4310f610b4c271e3bbb761032e633eba4c95d3d4

        when(mockedEntityRepository.findByPaths("student", new HashMap<String, String>())).thenReturn(studentFound);

        // School search.
        schoolFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        schoolFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, SCHOOL_ID);
        when(mockedEntityRepository.findByPaths("school", schoolFilterFields)).thenReturn(schoolFound);

        // Student-School Association search.
        studentSchoolAssociationFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        studentSchoolAssociationFilterFields.put("body.studentId", INTERNAL_STUDENT_ID);
        studentSchoolAssociationFilterFields.put("body.schoolId", INTERNAL_SCHOOL_ID);
        when(mockedEntityRepository.findByPaths("studentSchoolAssociation", studentSchoolAssociationFilterFields))
                .thenReturn(studentSchoolAssociationFound);

    }

    /*
     * @Test
     * public void testDoHandling() {
     * Entity mockedEntity = mock(Entity.class);
     * ErrorReport mockedErrorReport = mock(ErrorReport.class);
     *
     * String expectedType = "test_entity";
     * when(mockedEntity.getType()).thenReturn(expectedType);
     *
     * Map<String, Object> expectedMap = new HashMap<String, Object>();
     * when(mockedEntity.getBody()).thenReturn(expectedMap);
     *
     * entityPersistHandler.doHandling((NeutralRecordEntity)mockedEntity, mockedErrorReport);
     *
     * verify(mockedEntityRepository).create(expectedType, expectedMap);
     * }
     */

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     * @author tke 3/15/2012, modified be consistent with the new IdNormalization strategy.
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    @Test
    public void testCreateStudentEntity() {
        MongoEntityRepository entityRepository = mock(MongoEntityRepository.class);
        FaultsReport fr = new FaultsReport();

        // Student search.
        Map<String, String> studentFilterFields = new HashMap<String, String>();
        studentFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        studentFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, STUDENT_ID);

        // Create a new student entity with entity ID, and test creating it in the data store.
        SimpleEntity studentEntity = createStudentEntity(true);

        List<Entity> le = new ArrayList<Entity>();
        le.add(studentEntity);
        when(entityRepository.findByPaths("student", studentFilterFields)).thenReturn(le);
        when(entityRepository.update(studentEntity.getType(), studentEntity)).thenReturn(true);

        entityPersistHandler.setEntityRepository(entityRepository);
        entityPersistHandler.doHandling(studentEntity, fr);

        verify(entityRepository).update(studentEntity.getType(), studentEntity);

        //Test student entity without entity ID, so that repository will create a new one
        le.clear();
        SimpleEntity studentEntity2 = createStudentEntity(false);
        le.add(studentEntity2);

        entityPersistHandler.doHandling(studentEntity2, fr);

        verify(entityRepository).create(studentEntity.getType(), studentEntity.getBody(), studentEntity.getMetaData(),
                "student");

        Assert.assertFalse("Error report should not contain errors", fr.hasErrors());
    }

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    @Test
    public void testUpdateStudentEntity() {
        MongoEntityRepository entityRepository = mock(MongoEntityRepository.class);
        FaultsReport fr = new FaultsReport();

        SimpleEntity studentEntity = createStudentEntity(true);
        SimpleEntity existingStudentEntity = createStudentEntity(true);

        existingStudentEntity.setEntityId(UUID.randomUUID().toString());

        // Student search.
        Map<String, String> studentFilterFields = new HashMap<String, String>();
        studentFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        studentFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, STUDENT_ID);
        when(entityRepository.findByPaths("student", studentFilterFields)).thenReturn(
                Arrays.asList((Entity) existingStudentEntity));

        when(entityRepository.update("student", studentEntity)).thenReturn(true);

        entityPersistHandler.setEntityRepository(entityRepository);
        entityPersistHandler.doHandling(studentEntity, fr);

        verify(entityRepository).update("student", studentEntity);
        Assert.assertFalse("Error report should not contain errors", fr.hasErrors());
    }

    @Test
    public void testPersistanceExceptionHandling() {
        MongoEntityRepository entityRepository = mock(MongoEntityRepository.class);
        FaultsReport fr = new FaultsReport();

        SimpleEntity studentEntity = createStudentEntity(true);
        SimpleEntity existingStudentEntity = createStudentEntity(true);

        existingStudentEntity.setEntityId(UUID.randomUUID().toString());

        // Student search.
        Map<String, String> studentFilterFields = new HashMap<String, String>();
        studentFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        studentFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, STUDENT_ID);
        when(entityRepository.findByPaths("student", studentFilterFields)).thenReturn(
                Arrays.asList((Entity) existingStudentEntity));

        ValidationError error = new ValidationError(ErrorType.REQUIRED_FIELD_MISSING, "field", null,
                new String[] { "String" });
        when(entityRepository.update("student", studentEntity)).thenThrow(
                new EntityValidationException(existingStudentEntity.getEntityId(), "student", Arrays.asList(error)));

        entityPersistHandler.setEntityRepository(entityRepository);
        entityPersistHandler.doHandling(studentEntity, fr);

        Assert.assertTrue("Error report should contain errors", fr.hasErrors());
        Assert.assertEquals("Entity student - Record 0: Missing or empty field <field>", fr.getFaults().get(0).getMessage());
    }

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    @Test
    public void testCreateStudentSchoolAssociationEntity() {
        MongoEntityRepository entityRepository = mock(MongoEntityRepository.class);
        FaultsReport fr = new FaultsReport();

        // Create a new student-school association entity, and test creating it in the data store.
        SimpleEntity foundStudent = new SimpleEntity();
        foundStudent.setEntityId(INTERNAL_STUDENT_ID);

        LinkedList<Entity> studentList = new LinkedList<Entity>();
        studentList.add(foundStudent);

        // Student search.
        Map<String, String> studentFilterFields = new HashMap<String, String>();
        studentFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        studentFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, STUDENT_ID);
        when(entityRepository.findByPaths("student", studentFilterFields)).thenReturn(studentList);

        // School search.
        SimpleEntity foundSchool = new SimpleEntity();
        foundSchool.setEntityId(INTERNAL_SCHOOL_ID);

        LinkedList<Entity> schoolList = new LinkedList<Entity>();
        schoolList.add(foundSchool);
        when(entityRepository.findByPaths("school", schoolFilterFields)).thenReturn(schoolList);

        SimpleEntity studentSchoolAssociationEntity = createStudentSchoolAssociationEntity(STUDENT_ID);
        entityPersistHandler.setEntityRepository(entityRepository);
        entityPersistHandler.doHandling(studentSchoolAssociationEntity, fr);
        verify(entityRepository).create(studentSchoolAssociationEntity.getType(),
                studentSchoolAssociationEntity.getBody(), studentSchoolAssociationEntity.getMetaData(),
                studentSchoolAssociationEntity.getType());
        Assert.assertFalse("Error report should not contain errors", fr.hasErrors());
    }

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    @Test
    public void testUpdateStudentSchoolAssociationEntity() {
        MongoEntityRepository entityRepository = mock(MongoEntityRepository.class);
        FaultsReport fr = new FaultsReport();

        // Create a new student-school association entity, and test creating it in the data store.
        NeutralRecordEntity foundStudent = new NeutralRecordEntity(null);
        foundStudent.setEntityId(INTERNAL_STUDENT_ID);

        LinkedList<Entity> studentList = new LinkedList<Entity>();
        studentList.add(foundStudent);

        // Student search.
        Map<String, String> studentFilterFields = new HashMap<String, String>();
        studentFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        studentFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, STUDENT_ID);
        when(entityRepository.findByPaths("student", studentFilterFields)).thenReturn(studentList);

        // School search.
        NeutralRecordEntity foundSchool = new NeutralRecordEntity(null);
        foundSchool.setEntityId(INTERNAL_SCHOOL_ID);

        LinkedList<Entity> schoolList = new LinkedList<Entity>();
        schoolList.add(foundSchool);
        when(entityRepository.findByPaths("school", schoolFilterFields)).thenReturn(schoolList);

        SimpleEntity studentSchoolAssociationEntity = createStudentSchoolAssociationEntity(STUDENT_ID);
        SimpleEntity existingStudentSchoolAssociationEntity = createStudentSchoolAssociationEntity(STUDENT_ID);

        existingStudentSchoolAssociationEntity.setEntityId(UUID.randomUUID().toString());

        when(entityRepository.findByPaths("studentSchoolAssociation", studentSchoolAssociationFilterFields))
                .thenReturn(Arrays.asList((Entity) existingStudentSchoolAssociationEntity));

        when(entityRepository.update("studentSchoolAssociation", studentSchoolAssociationEntity)).thenReturn(true);

        entityPersistHandler.setEntityRepository(entityRepository);
        entityPersistHandler.doHandling(studentSchoolAssociationEntity, fr);

        verify(entityRepository).update("studentSchoolAssociation", studentSchoolAssociationEntity);
        Assert.assertFalse("Error report should not contain errors", fr.hasErrors());
    }

    @Test
    public void testInvalidUpdateStudentSchoolAssociationEntity() {
        MongoEntityRepository entityRepository = mock(MongoEntityRepository.class);
        FaultsReport fr = new FaultsReport();

        // Create a new student-school association entity, and test creating it in the data store.
        NeutralRecordEntity foundStudent = new NeutralRecordEntity(null);
        foundStudent.setEntityId(INTERNAL_STUDENT_ID);

        LinkedList<Entity> studentList = new LinkedList<Entity>();
        studentList.add(foundStudent);

        // Student search.
        Map<String, String> studentFilterFields = new HashMap<String, String>();
        studentFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        studentFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, BAD_STUDENT_ID);
        when(entityRepository.findByPaths("student", studentFilterFields)).thenReturn(Collections.<Entity>emptyList());

        // School search.
        NeutralRecordEntity foundSchool = new NeutralRecordEntity(null);
        foundSchool.setEntityId(INTERNAL_SCHOOL_ID);

        LinkedList<Entity> schoolList = new LinkedList<Entity>();
        schoolList.add(foundSchool);
        when(entityRepository.findByPaths("school", schoolFilterFields)).thenReturn(schoolList);

        SimpleEntity studentSchoolAssociationEntity = createStudentSchoolAssociationEntity(BAD_STUDENT_ID);
        when(entityRepository.findByPaths("studentSchoolAssociation", studentSchoolAssociationFilterFields))
                .thenReturn(Arrays.asList((Entity) studentSchoolAssociationEntity));

        when(entityRepository.update("studentSchoolAssociation", studentSchoolAssociationEntity)).thenReturn(true);

        entityPersistHandler.setEntityRepository(entityRepository);
        entityPersistHandler.doHandling(studentSchoolAssociationEntity, fr);

        verify(entityRepository, never()).update("studentSchoolAssociation", studentSchoolAssociationEntity);
        Assert.assertTrue("Error report should contain errors", fr.hasErrors());
    }

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    @Test
    public void testInvalidateStudentSchoolAssociationEntity() {
        SimpleEntity studentSchoolAssociationEntity = createStudentSchoolAssociationEntity(BAD_STUDENT_ID);
        studentSchoolAssociationEntity.getBody().put("studentId", BAD_STUDENT_ID);

        FaultsReport fr = new FaultsReport();
        entityPersistHandler.doHandling(studentSchoolAssociationEntity, fr);
        verify(mockedEntityRepository, never()).create(studentSchoolAssociationEntity.getType(),
                studentSchoolAssociationEntity.getBody(), studentSchoolAssociationEntity.getMetaData(),
                studentSchoolAssociationEntity.getType());
        verify(mockedEntityRepository, never()).update(studentSchoolAssociationEntity.getType(),
                studentSchoolAssociationEntity);
        Assert.assertTrue("Error report should contain errors", fr.hasErrors());
    }

    @Test
    public void testHandleFailedValidation() {
        /*
         * when validation fails for an entity, we should not try to persist
         */

        SimpleEntity mockedEntity = mock(SimpleEntity.class);
        ErrorReport mockedErrorReport = mock(ErrorReport.class);

        String expectedType = "student";
        when(mockedEntity.getType()).thenReturn(expectedType);

        Map<String, Object> expectedMap = new HashMap<String, Object>();
        when(mockedEntity.getBody()).thenReturn(expectedMap);

        Map<String, Object> expectedMetaData = new HashMap<String, Object>();
        expectedMetaData.put(REGION_ID_FIELD, REGION_ID);
        expectedMetaData.put(EXTERNAL_ID_FIELD, STUDENT_ID);
        when(mockedEntity.getMetaData()).thenReturn(expectedMetaData);

        when(mockedErrorReport.hasErrors()).thenReturn(true);

        entityPersistHandler.handle(mockedEntity, mockedErrorReport);

        verify(mockedEntityRepository, never()).create(expectedType, expectedMap, expectedMetaData, expectedType);
    }

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     * @author tke 3/15/2012   modified to test the new id normalization strategy
     * @param setId : set entity ID if it is true.
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    public SimpleEntity createStudentEntity(boolean setId) {
        SimpleEntity entity = new SimpleEntity();

        if (setId)
            entity.setEntityId(STUDENT_ID);
        entity.setType("student");
        Map<String, Object> field = new HashMap<String, Object>();
        field.put("studentUniqueStateId", STUDENT_ID);
        field.put("Sex", "Male");

        entity.setBody(field);
        entity.setMetaData(new HashMap<String, Object>());

        return entity;
    }

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    public SimpleEntity createSchoolEntity() {
        // Create neutral record for entity.
        SimpleEntity entity = new SimpleEntity();
        entity.setEntityId(SCHOOL_ID);
        entity.setType("school");

        // Create new entity from neutral record.
        return entity;
    }

    /**
     * @author tshewchuk 2/6/2010 (PI3 US811)
     *         Added testing of record DB lookup and update, and support for association entities.
     */
    public SimpleEntity createStudentSchoolAssociationEntity(String studentId) {
        SimpleEntity entity = new SimpleEntity();

        entity.setType("studentSchoolAssociation");
        Map<String, Object> localParentIds = new HashMap<String, Object>();
        localParentIds.put("Student", studentId);
        localParentIds.put("School", SCHOOL_ID);
        entity.setMetaData(localParentIds);
        Map<String, Object> field = new HashMap<String, Object>();
        field.put("studentId", studentId);
        field.put("schoolId", SCHOOL_ID);
        field.put("ClassOf", "2014");
        entity.setBody(field);

        // Create and return new entity from neutral record.
        return entity;
    }

    public SimpleEntity createTeacherSchoolAssociationEntity(String teacherId) {
        // Create neutral record for entity.
        SimpleEntity entity = new SimpleEntity();
        entity.setType("teacherSchoolAssociation");
        Map<String, Object> field = new HashMap<String, Object>();
        field.put("teacherId", teacherId);
        field.put("schoolId", SCHOOL_ID);
        entity.setBody(field);

        // Create and return new entity from neutral record.
        return entity;
    }

    @Test
    public void testCreateTeacherSchoolAssociationEntity() {
        MongoEntityRepository entityRepository = mock(MongoEntityRepository.class);
        FaultsReport fr = new FaultsReport();

        // Create a new student-school association entity, and test creating it in the data store.
        SimpleEntity foundTeacher = new SimpleEntity();
        foundTeacher.setEntityId(INTERNAL_STUDENT_ID);

        LinkedList<Entity> teacherList = new LinkedList<Entity>();
        teacherList.add(foundTeacher);

        // Teacher search.
        Map<String, String> teacherFilterFields = new HashMap<String, String>();
        teacherFilterFields.put(METADATA_BLOCK + "." + REGION_ID_FIELD, REGION_ID);
        teacherFilterFields.put(METADATA_BLOCK + "." + EXTERNAL_ID_FIELD, STUDENT_ID);
        when(entityRepository.findByPaths("teacher", teacherFilterFields)).thenReturn(teacherList);

        // School search.
        SimpleEntity foundSchool = new SimpleEntity();
        foundSchool.setEntityId(INTERNAL_SCHOOL_ID);

        LinkedList<Entity> schoolList = new LinkedList<Entity>();
        schoolList.add(foundSchool);
        when(entityRepository.findByPaths("school", schoolFilterFields)).thenReturn(schoolList);

        SimpleEntity teacherSchoolAssociationEntity = createTeacherSchoolAssociationEntity(STUDENT_ID);
        entityPersistHandler.setEntityRepository(entityRepository);
        entityPersistHandler.doHandling(teacherSchoolAssociationEntity, fr);
        verify(entityRepository).create(teacherSchoolAssociationEntity.getType(),
                teacherSchoolAssociationEntity.getBody(), teacherSchoolAssociationEntity.getMetaData(),
                teacherSchoolAssociationEntity.getType());
        Assert.assertFalse("Error report should not contain errors", fr.hasErrors());
    }

    /**
     * @author tke
     */
/*    @Test
    public void testCreateEntityLookupFilterByFields() throws Exception {
        SimpleEntity simpleEntity = createStudentSchoolAssociationEntity(INTERNAL_STUDENT_ID);
        List<String> keyFields = new ArrayList<String>();
        keyFields.add(METADATA_BLOCK + ".localId");
        keyFields.add(METADATA_BLOCK + ".externalId");
        keyFields.add("body.studentId");
        simpleEntity.setMetaData(new HashMap<String, Object>());
        simpleEntity.getMetaData().put("localId", INTERNAL_STUDENT_ID);
        simpleEntity.getMetaData().put("externalId", SCHOOL_ID);

        ErrorReport mockedErrorReport = mock(ErrorReport.class);
        when(mockedErrorReport.hasErrors()).thenReturn(false);

        File jsonFile = new File("src/test/resources/TeacherSectionAssociation.json");
        ObjectMapper mapper = new ObjectMapper();
        EntityConfig teacherSectionAssociation = mapper.readValue(jsonFile, EntityConfig.class);

        Map<String, String> res = entityPersistHandler.createEntityLookupFilter(simpleEntity, teacherSectionAssociation, mockedErrorReport);

        Assert.assertNotNull(res);
        Assert.assertEquals(INTERNAL_STUDENT_ID, res.get(METADATA_BLOCK + ".localId"));
        Assert.assertEquals(SCHOOL_ID, res.get(METADATA_BLOCK + ".externalId"));
        Assert.assertEquals(INTERNAL_STUDENT_ID, res.get("body.studentId"));
    }
*/
}
