/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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

package org.slc.sli.dal.convert;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.AppInfo;
import org.slc.sli.validation.schema.NeutralSchema;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.common.domain.ContainerDocument;
import org.slc.sli.common.domain.ContainerDocumentHolder;
import org.slc.sli.common.domain.NaturalKeyDescriptor;
import org.slc.sli.common.util.uuid.UUIDGeneratorStrategy;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.validation.schema.INaturalKeyExtractor;

/**
 * @author jstokes
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class ContainerDocumentAccessorTest {

    private static final String ATTENDANCE = "attendance";
    private static final String GRADE = "grade";
    private static final String ID = "abc123_id";

    @InjectMocks
    private ContainerDocumentAccessor testAccessor;

    @Mock
    private ContainerDocumentHolder mockHolder;

    @Mock
    private UUIDGeneratorStrategy generatorStrategy;

    @Mock
    private INaturalKeyExtractor naturalKeyExtractor;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private SchemaRepository schemaRepo;

    private Entity entity;

    private WriteResult writeResult;

    private CommandResult commandResult;

    private DBCollection mockCollection;

    @Before
    public void setup() {
        mockCollection = Mockito.mock(DBCollection.class);
        writeResult = Mockito.mock(WriteResult.class);
        commandResult = Mockito.mock(CommandResult.class);
        testAccessor = new ContainerDocumentAccessor(generatorStrategy, naturalKeyExtractor, mongoTemplate, schemaRepo);
        MockitoAnnotations.initMocks(this);
        when(mockHolder.getContainerDocument(ATTENDANCE)).thenReturn(createContainerDocAttendance());
        entity = createAttendanceEntity();
        when(writeResult.getLastError()).thenReturn(commandResult);
        when(commandResult.ok()).thenReturn(true);
    }

    @Test
    public void testIsContainerDocument() {
        when(mockHolder.isContainerDocument(ATTENDANCE)).thenReturn(true);

        boolean actual = testAccessor.isContainerDocument(ATTENDANCE);
        assertEquals(true, actual);
    }

    @Test
    public void testCreateParentKey() {
        final ContainerDocument attendance = createContainerDocAttendance();
        final NaturalKeyDescriptor stubKeyDescriptor =
                ContainerDocumentHelper.extractNaturalKeyDescriptor(entity, attendance.getParentNaturalKeys(), attendance.getCollectionToPersist());

        when(generatorStrategy.generateId(stubKeyDescriptor)).thenReturn(ID);

        final String parentUUID = ContainerDocumentHelper.createParentKey(entity, mockHolder, generatorStrategy);

        assertFalse(parentUUID == null);
        assertFalse(parentUUID.isEmpty());
        assertEquals(ID, parentUUID);
    }

    @Test
    public void isContainerSubDoc() {
        final ContainerDocument cDoc2 = createContainerDocGrade();

        when(mockHolder.isContainerDocument(ATTENDANCE)).thenReturn(true);
        when(mockHolder.isContainerDocument(GRADE)).thenReturn(true);
        when(mockHolder.getContainerDocument(GRADE)).thenReturn(cDoc2);

        assertFalse(testAccessor.isContainerSubdoc(ATTENDANCE));
        assertTrue(testAccessor.isContainerSubdoc(GRADE));
    }

    @Test
    public void testInsert() {
        DBObject query = Mockito.mock(DBObject.class);
        DBObject docToPersist = ContainerDocumentHelper.buildDocumentToPersist(mockHolder, entity, generatorStrategy, naturalKeyExtractor);

        when(query.get("_id")).thenReturn(ID);
        when(mockHolder.getContainerDocument(ATTENDANCE)).thenReturn(createContainerDocAttendance());
        when(mongoTemplate.getCollection(ATTENDANCE)).thenReturn(mockCollection);
        when(mockCollection.update(Mockito.any(DBObject.class), Mockito.eq(docToPersist), Mockito.eq(true), Mockito.eq(false), Mockito.eq(WriteConcern.SAFE))).thenReturn(writeResult);

        String result = testAccessor.insertContainerDoc(query, entity);

        assertTrue(result.equals(ID));
        assertFalse("Wrong result returned by insert", result.equals(""));
        Mockito.verify(mockCollection, Mockito.times(1)).update(Mockito.any(DBObject.class), Mockito.eq(docToPersist), Mockito.eq(true), Mockito.eq(false), Mockito.eq(WriteConcern.SAFE));
    }

    private DBObject createResultAttendance(String date) {
        Map<String, Object> attendanceBody = new HashMap<String, Object>();
        List<Map<String, Object>> attendanceEvent = new ArrayList<Map<String, Object>>();
        if (date != null) {
            Map<String, Object> attendanceElement = new HashMap<String, Object>();
            attendanceElement.put("date", date);
            attendanceEvent.add(attendanceElement);
        }
        attendanceBody.put("attendanceEvent", attendanceEvent);
        return BasicDBObjectBuilder.start().add("body", attendanceBody).get();
    }

    @Test
    public void testDeleteEntity() {
        ContainerDocumentAccessor cda = Mockito.spy(testAccessor);
        Map<String, Object> updateDocCriteria = new HashMap<String, Object>();
        updateDocCriteria.put("event", "Tardy");
        DBObject pullObject = BasicDBObjectBuilder.start().push("$pull").add("body.attendanceEvent", updateDocCriteria).get();

        DBObject resultingAttendanceEvent = createResultAttendance("10-04-2013");

        NeutralSchema attendanceSchema = createMockAttendanceSchema();

        when(mongoTemplate.getCollection(ATTENDANCE)).thenReturn(mockCollection);
        when(mockCollection.findAndModify(Mockito.any(DBObject.class), (DBObject) Mockito.isNull(), (DBObject) Mockito.isNull(),
                Mockito.eq(false), Mockito.eq(pullObject),Mockito.eq(true), Mockito.eq(false))).thenReturn(resultingAttendanceEvent);
        when(schemaRepo.getSchema(EntityNames.ATTENDANCE)).thenReturn(attendanceSchema);

        boolean result = cda.deleteContainerNonSubDocs(entity);

        Mockito.verify(mockCollection, Mockito.times(1)).findAndModify(Mockito.any(DBObject.class), (DBObject) Mockito.isNull(), (DBObject) Mockito.isNull(),
                Mockito.eq(false), Mockito.eq(pullObject), Mockito.eq(true), Mockito.eq(false));
        assertTrue(result);
    }

    @Test
    public void testDeleteEntityAndContainerDoc() {
        ContainerDocumentAccessor cda = Mockito.spy(testAccessor);
        Map<String, Object> updateDocCriteria = new HashMap<String, Object>();
        updateDocCriteria.put("event", "Tardy");
        DBObject pullObject = BasicDBObjectBuilder.start().push("$pull").add("body.attendanceEvent", updateDocCriteria).get();
        DBObject resultingAttendanceEvent = createResultAttendance(null);

        NeutralSchema attendanceSchema = createMockAttendanceSchema();

        when(mongoTemplate.getCollection(ATTENDANCE)).thenReturn(mockCollection);
        when(mockCollection.findAndModify(Mockito.any(DBObject.class), (DBObject) Mockito.isNull(), (DBObject) Mockito.isNull(),
                Mockito.eq(false), Mockito.eq(pullObject), Mockito.eq(true), Mockito.eq(false))).thenReturn(resultingAttendanceEvent);
        when(mongoTemplate.findAndRemove(Mockito.any(Query.class), Mockito.eq(Entity.class), Mockito.eq(ATTENDANCE))).thenReturn(entity); // just return something non-null
        when(schemaRepo.getSchema(EntityNames.ATTENDANCE)).thenReturn(attendanceSchema);

        boolean result = cda.deleteContainerNonSubDocs(entity);

        Mockito.verify(mockCollection, Mockito.times(1)).findAndModify(Mockito.any(DBObject.class), (DBObject) Mockito.isNull(), (DBObject) Mockito.isNull(),
                Mockito.eq(false), Mockito.eq(pullObject), Mockito.eq(true), Mockito.eq(false));
        Mockito.verify(mongoTemplate, Mockito.times(1)).findAndRemove(Mockito.any(Query.class), Mockito.eq(Entity.class), Mockito.eq(ATTENDANCE));
        assertTrue(result);
    }

    @Test
    public void testUpdateEntity() {
        ContainerDocumentAccessor cda = Mockito.spy(testAccessor);
        Map<String, Object> updateDocCriteria = new HashMap<String, Object>();
        updateDocCriteria.put("event", "Tardy");
        DBObject pullObject = BasicDBObjectBuilder.start().push("$pull").add("body.attendanceEvent", updateDocCriteria).get();
        DBObject resultingAttendanceEvent = createResultAttendance(null);
        NeutralSchema attendanceSchema = createMockAttendanceSchema();

        when(mongoTemplate.getCollection(ATTENDANCE)).thenReturn(mockCollection);
        when(mockCollection.findAndModify(Mockito.any(DBObject.class), (DBObject) Mockito.isNull(), (DBObject) Mockito.isNull(),
                Mockito.eq(false), Mockito.eq(pullObject), Mockito.eq(true), Mockito.eq(false))).thenReturn(resultingAttendanceEvent);
        when(mongoTemplate.findAndRemove(Mockito.any(Query.class), Mockito.eq(Entity.class), Mockito.eq(ATTENDANCE))).thenReturn(entity); // just return something non-null
        when(mockCollection.update(Mockito.any(DBObject.class), Mockito.any(DBObject.class), Mockito.eq(true), Mockito.eq(false), Mockito.eq(WriteConcern.SAFE))).thenReturn(writeResult);
        when(schemaRepo.getSchema(EntityNames.ATTENDANCE)).thenReturn(attendanceSchema);

        String result = cda.updateContainerDoc(entity);

        Mockito.verify(cda, Mockito.times(1)).deleteContainerNonSubDocs(Mockito.eq(entity));
        Mockito.verify(cda, Mockito.times(1)).insertContainerDoc(Mockito.any(DBObject.class), Mockito.eq(entity));
        assertTrue(result.equals(ID));
    }

    @Test
    public void testUpdateNoContainerFields() {
        Query query = Mockito.mock(Query.class);
        DBObject queryObject = Mockito.mock(DBObject.class);
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("studentId", "student1");
        body.put("schoolId", "school1");
        body.put("schoolYear", "schoolyear1");
        Map<String, Object> fields = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            fields.put("body." + entry.getKey(), entry.getValue());
        }
        DBObject docsToPersist = new BasicDBObject("$set", new BasicDBObject(fields));

        when(query.getQueryObject()).thenReturn(queryObject);
        when(mongoTemplate.getCollection(ATTENDANCE)).thenReturn(mockCollection);
        when(mockCollection.update(Mockito.eq(queryObject), Mockito.eq(docsToPersist), Mockito.eq(true), Mockito.eq(false), Mockito.eq(WriteConcern.SAFE))).thenReturn(writeResult);

        boolean result = testAccessor.updateContainerDoc(query, body, ATTENDANCE, ATTENDANCE);

        assertTrue(result);
        Mockito.verify(mockCollection, Mockito.times(1)).update(Mockito.eq(queryObject), Mockito.eq(docsToPersist), Mockito.eq(true), Mockito.eq(false), Mockito.eq(WriteConcern.SAFE));

    }

    @Test
    public void testUpdateContainerFields() {
        Query query = Mockito.mock(Query.class);
        DBObject queryObject = Mockito.mock(DBObject.class);
        Map<String, Object> body = entity.getBody();

        Map<String, Object> fields = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            if(!entry.getKey().equals("attendanceEvent")) {
                fields.put("body." + entry.getKey(), entry.getValue());
            }
        }
        final Map<String, Object> attendanceEvent = new HashMap<String, Object>();
        final List<Map<String, Object>> attendanceEvents = new ArrayList<Map<String, Object>>();
        attendanceEvent.put("event", "Tardy");
        attendanceEvents.add(attendanceEvent);

        DBObject docsToPersist = BasicDBObjectBuilder.start().push("$pushAll").add("body.attendanceEvent", attendanceEvents).get();
        DBObject bodyFields = new BasicDBObject("$set", new BasicDBObject(fields));
        docsToPersist.putAll(bodyFields);

        when(query.getQueryObject()).thenReturn(queryObject);
        when(mongoTemplate.getCollection(ATTENDANCE)).thenReturn(mockCollection);
        when(mockCollection.update(Mockito.eq(queryObject), Mockito.eq(docsToPersist), Mockito.eq(true), Mockito.eq(false), Mockito.eq(WriteConcern.SAFE))).thenReturn(writeResult);

        boolean result = testAccessor.updateContainerDoc(query, body, ATTENDANCE, ATTENDANCE);

        assertTrue(result);
        Mockito.verify(mockCollection, Mockito.times(1)).update(Mockito.eq(queryObject), Mockito.eq(docsToPersist), Mockito.eq(true), Mockito.eq(false), Mockito.eq(WriteConcern.SAFE));
    }

    private NeutralSchema createMockAttendanceSchema() {
        NeutralSchema mockAttendanceSchema = mock(NeutralSchema.class);
        Map<String, NeutralSchema> fieldSchemas = new HashMap<String, NeutralSchema>();
        fieldSchemas.put("date", createMockFieldSchema(true));
        fieldSchemas.put("event", createMockFieldSchema(true));
        fieldSchemas.put("sectionId", createMockFieldSchema(true));
        when(mockAttendanceSchema.getFields()).thenReturn(fieldSchemas);
        return mockAttendanceSchema;
    }

    private NeutralSchema createMockFieldSchema(boolean isNaturalKey) {
        NeutralSchema mockFieldSchema = mock(NeutralSchema.class);
        when(mockFieldSchema.getAppInfo()).thenReturn(this.createAppInfo(isNaturalKey));
        return mockFieldSchema;
    }

    private AppInfo createAppInfo(boolean isNaturalKey) {
        AppInfo appInfo = new AppInfo(null);
        appInfo.put(AppInfo.NATURAL_KEY, isNaturalKey);
        return appInfo;
    }

    private MongoEntity createAttendanceEntity() {
        final Map<String, Object> body = new HashMap<String, Object>();
        final Map<String, Object> attendanceEvent = new HashMap<String, Object>();
        final List<Map<String, Object>> attendanceEvents = new ArrayList<Map<String, Object>>();
        attendanceEvent.put("event", "Tardy");
        attendanceEvents.add(attendanceEvent);
        body.put("studentId", "student1");
        body.put("schoolId", "school1");
        body.put("schoolYear", "schoolyear1");
        body.put("attendanceEvent", attendanceEvents);
        return new MongoEntity(ATTENDANCE, ID, body, new HashMap<String, Object>());
    }

    private ContainerDocument createContainerDocAttendance() {
        final List<String> parentKeys = Arrays.asList("studentId", "schoolId", "schoolYear");
        final ContainerDocument cDoc = ContainerDocument.builder()
                .forCollection(EntityNames.ATTENDANCE)
                .forField("attendanceEvent")
                .persistAs(EntityNames.ATTENDANCE)
                .asContainerSubdoc(false)
                .withParent(parentKeys).build();
        return cDoc;
    }

    private ContainerDocument createContainerDocGrade() {
        final List<String> parentKeys = Arrays.asList("studentId", "schoolYear");
        final ContainerDocument cDoc = ContainerDocument.builder()
                .forCollection(EntityNames.GRADE)
                .forField(EntityNames.GRADE)
                .withParent(parentKeys)
                .persistAs("yearlyTranscript")
                .asContainerSubdoc(true).build();
        return cDoc;
    }
}
