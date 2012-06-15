package org.slc.sli.ingestion.transformation.normalization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.domain.Repository;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.NeutralRecordEntity;
import org.slc.sli.ingestion.landingzone.validation.TestErrorReport;
import org.slc.sli.ingestion.tenant.LandingZoneRecord;
import org.slc.sli.ingestion.tenant.TenantMongoDA;
import org.slc.sli.ingestion.tenant.TenantRecord;
import org.slc.sli.ingestion.validation.DummyErrorReport;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Query;

/**
 * ID Normalizer unit tests.
 * 
 * @author okrook
 * 
 */
public class IdNormalizationTest {
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Test
    public void testRefResolution() {
        Ref myCollectionId = new Ref();
        myCollectionId.setCollectionName("MyCollection");
        Field columnField = new Field();
        columnField.setPath("column");
        
        FieldValue columnValue = new FieldValue();
        columnValue.setValueSource("body.field");
        columnField.setValues(Arrays.asList(columnValue));
        
        List<Field> fields = Arrays.asList(columnField);
        List<List<Field>> choice = Arrays.asList(fields);
        myCollectionId.setChoiceOfFields(choice);
        
        IdNormalizer idNorm = new IdNormalizer();
        Repository<Entity> repo = Mockito.mock(Repository.class);
        Repository<Entity> repoNull = Mockito.mock(Repository.class);
        
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("field", 5);
        
        MongoEntity entity = new MongoEntity("test", body);
        
        Entity expectedRecord = Mockito.mock(Entity.class);
        Mockito.when(expectedRecord.getEntityId()).thenReturn("123");
        
        Mockito.when(
                repo.findByQuery(Mockito.eq("MyCollection"), Mockito.any(Query.class), Mockito.eq(0), Mockito.eq(0)))
                .thenReturn(Arrays.asList(expectedRecord));
        Mockito.when(
                repoNull.findByQuery(Mockito.eq("MyCollection"), Mockito.any(Query.class), Mockito.eq(0), Mockito.eq(0)))
                .thenReturn(null);
        
        idNorm.setEntityRepository(repo);
        
        String internalId = idNorm.resolveInternalId(entity, "someNamespace", myCollectionId, "someFieldPath",
                new DummyErrorReport(), "");
        
        Assert.assertEquals("123", internalId);
        
        idNorm.setEntityRepository(repoNull);
        
        // Testing findByQuery returns null
        internalId = idNorm.resolveInternalId(entity, "someNamespace", myCollectionId, "someFieldPath",
                new DummyErrorReport(), "");
        
        Assert.assertEquals(null, internalId);
    }
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Test
    public void testRefResolution2() {
        Ref myCollectionId = new Ref();
        myCollectionId.setCollectionName("MyCollection");
        Field columnField = new Field();
        columnField.setPath("column");
        
        List<RefDef> refDefs = new ArrayList<RefDef>();
        RefDef refDef = new RefDef();
        refDef.setRef(myCollectionId);
        refDef.setFieldPath("body.field");
        refDefs.add(refDef);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setReferences(refDefs);
        EntityConfig entityConfig2 = new EntityConfig();
        entityConfig2.setReferences(null);
        
        FieldValue columnValue = new FieldValue();
        columnValue.setValueSource("body.field");
        columnField.setValues(Arrays.asList(columnValue));
        
        List<Field> fields = Arrays.asList(columnField);
        List<List<Field>> choice = Arrays.asList(fields);
        myCollectionId.setChoiceOfFields(choice);
        
        IdNormalizer idNorm = new IdNormalizer();
        Repository<Entity> repo = Mockito.mock(Repository.class);
        
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("field", 5);
        
        MongoEntity entity = new MongoEntity("test", body);
        
        Entity expectedRecord = Mockito.mock(Entity.class);
        Mockito.when(expectedRecord.getEntityId()).thenReturn("123");
        
        Mockito.when(
                repo.findByQuery(Mockito.eq("MyCollection"), Mockito.any(Query.class), Mockito.eq(0), Mockito.eq(0)))
                .thenReturn(Arrays.asList(expectedRecord));
        
        idNorm.setEntityRepository(repo);
        
        idNorm.resolveInternalIds(entity, "someNamespace", entityConfig, new DummyErrorReport());
        
        Assert.assertEquals("123", entity.getBody().get("field"));
        
        // Testing entityConfig.getReference == null
        idNorm.resolveInternalIds(entity, "someNamespace", entityConfig2, new DummyErrorReport());
        Assert.assertEquals("123", entity.getBody().get("field"));
    }
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Test
    public void testCollectionRefResolution() {
        Ref myCollectionId = new Ref();
        myCollectionId.setCollectionName("MyCollection");
        Field columnField = new Field();
        columnField.setPath("column");
        
        FieldValue columnValue = new FieldValue();
        columnValue.setValueSource("body.field");
        columnField.setValues(Arrays.asList(columnValue));
        
        List<Field> fields = Arrays.asList(columnField);
        List<List<Field>> choice = Arrays.asList(fields);
        myCollectionId.setChoiceOfFields(choice);
        
        IdNormalizer idNorm = new IdNormalizer();
        Repository<Entity> repo = Mockito.mock(Repository.class);
        
        Map<String, Object> body = new HashMap<String, Object>();
        
        List<String> value = new ArrayList<String>();
        
        value.add("5");
        value.add("6");
        value.add("7");
        body.put("field", value);
        
        MongoEntity entity = new MongoEntity("test", body);
        
        Entity expectedRecord = Mockito.mock(Entity.class);
        Mockito.when(expectedRecord.getEntityId()).thenReturn("123");
        
        Entity secondRecord = Mockito.mock(Entity.class);
        Mockito.when(secondRecord.getEntityId()).thenReturn("456");
        
        Entity thirdRecord = Mockito.mock(Entity.class);
        Mockito.when(thirdRecord.getEntityId()).thenReturn("789");
        
        ArrayList<Entity> records = new ArrayList<Entity>();
        records.add(expectedRecord);
        records.add(secondRecord);
        records.add(thirdRecord);
        
        Mockito.when(
                repo.findByQuery(Mockito.eq("MyCollection"), Mockito.any(Query.class), Mockito.eq(0), Mockito.eq(0)))
                .thenReturn(records);
        
        idNorm.setEntityRepository(repo);
        
        String internalId = idNorm.resolveInternalId(entity, "someNamespace", myCollectionId, "someFieldPath",
                new DummyErrorReport(), "");
        
        Assert.assertEquals("123", internalId);
    }
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Test
    public void testMultiRefResolution() {
        
        Ref secondCollection = new Ref();
        secondCollection.setCollectionName("secondCollection");
        Field secondCollectionField = new Field();
        secondCollectionField.setPath("column");
        FieldValue fValue = new FieldValue();
        fValue.setValueSource("body.field");
        secondCollectionField.setValues(Arrays.asList(fValue));
        secondCollection.setChoiceOfFields(Arrays.asList(Arrays.asList(secondCollectionField)));
        
        Ref myCollectionId = new Ref();
        myCollectionId.setCollectionName("MyCollection");
        Field columnField = new Field();
        columnField.setPath("body.secondCollectionId");
        
        FieldValue columnValue = new FieldValue();
        columnValue.setRef(secondCollection);
        columnField.setValues(Arrays.asList(columnValue));
        
        List<Field> fields = Arrays.asList(columnField);
        List<List<Field>> choice = Arrays.asList(fields);
        myCollectionId.setChoiceOfFields(choice);
        
        IdNormalizer idNorm = new IdNormalizer();
        Repository<Entity> repo = Mockito.mock(Repository.class);
        
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("field", 5);
        
        MongoEntity entity = new MongoEntity("test", body);
        
        Entity expectedRecord = Mockito.mock(Entity.class);
        Mockito.when(expectedRecord.getEntityId()).thenReturn("123");
        
        Mockito.when(
                repo.findByQuery(Mockito.eq("MyCollection"), Mockito.any(Query.class), Mockito.eq(0), Mockito.eq(0)))
                .thenReturn(Arrays.asList(expectedRecord));
        
        Entity secondRecord = Mockito.mock(Entity.class);
        Mockito.when(secondRecord.getEntityId()).thenReturn("456");
        Mockito.when(
                repo.findByQuery(Mockito.eq("secondCollection"), Mockito.any(Query.class), Mockito.eq(0), Mockito.eq(0)))
                .thenReturn(Arrays.asList(secondRecord));
        
        idNorm.setEntityRepository(repo);
        
        String internalId = idNorm.resolveInternalId(entity, "someNamespace", myCollectionId, "someFieldPath",
                new DummyErrorReport(), "");
        
        Assert.assertEquals("123", internalId);
        
        String secinternalId = idNorm.resolveInternalId(entity, "someNamespace", secondCollection, "someFieldPath",
                new DummyErrorReport(), "");
        
        Assert.assertEquals("456", secinternalId);
    }
    
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Test
    public void testResolveRefList() {
        final String collectionName = "collectionName";
        
        //create a test refConfig
        Ref refConfig = new Ref();
        refConfig.setCollectionName(collectionName);
        refConfig.setIsRefList(true);
        // the base path of the reference object
        refConfig.setRefObjectPath("body.refField");
        Field field = new Field();
        // the path to query
        field.setPath("body.refIds");
        FieldValue fValue = new FieldValue();
        // the source field for the query value
        fValue.setValueSource("body.refField.idField");
        field.setValues(Arrays.asList(fValue));
        refConfig.setChoiceOfFields(Arrays.asList(Arrays.asList(field)));

        //create an entity config
        List<RefDef> refDefs = new ArrayList<RefDef>();
        RefDef refDef = new RefDef();
        refDef.setRef(refConfig);
        // the field to be put the resolved internal ID into
        refDef.setFieldPath("body.refIds");
        refDefs.add(refDef);
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setReferences(refDefs);

        Entity entity = getTestRefListEntity();

        NeutralRecord nr1 = new NeutralRecord();
        NeutralRecordEntity expectedEntity1 = new NeutralRecordEntity(nr1);
        expectedEntity1.getBody().put("externalId", "externalId1");
        expectedEntity1.setEntityId("GUID_1");
        NeutralRecord nr2 = new NeutralRecord();
        NeutralRecordEntity expectedEntity2 = new NeutralRecordEntity(nr2);
        expectedEntity2.getBody().put("externalId", "externalId2");
        expectedEntity2.setEntityId("GUID_2");
        List<Entity> expectedEntityList = new ArrayList<Entity>();
        expectedEntityList.add(expectedEntity1);
        expectedEntityList.add(expectedEntity2);

        IdNormalizer idNorm = new IdNormalizer();
        
        Repository<Entity> repo = Mockito.mock(Repository.class);

        //mock the repo query note this doesn't test whether the query was constructed correctly
        Mockito.when(repo.findByQuery(Mockito.eq(collectionName), Mockito.any(Query.class), Mockito.eq(0), Mockito.eq(0))).thenReturn(expectedEntityList);

        idNorm.setEntityRepository(repo);
        idNorm.resolveInternalIds(entity, "someNamespace", entityConfig, new DummyErrorReport());

        List<String> refIds = (List<String>) entity.getBody().get("refIds");

        assertNotNull("attribute refIds field should not be null", refIds);
        assertEquals("attribute refIds should have 2 elements", 2, refIds.size());
        assertEquals("attribute refIds first element should be resolved to GUID_1", "GUID_1", refIds.get(0));
        assertEquals("attribute refIds second element should be resolved to GUID_2", "GUID_2", refIds.get(1));
    }
    
    private Entity getTestRefListEntity() {
        Map<String, Object> firstRef = new HashMap<String, Object>();
        firstRef.put("idField", "externalId1");
        Map<String, Object> secondRef = new HashMap<String, Object>();
        secondRef.put("idField", "externalId2");
        //Added one more duplicate reference to test fix to DE564
        Map<String, Object> thirdRef = new HashMap<String, Object>();
        thirdRef.put("idField", "externalId2");

        List<Object> refList = new ArrayList<Object>();
        refList.add(firstRef);
        refList.add(secondRef);
        refList.add(thirdRef);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("refField", refList);

        List<Object> idList = new ArrayList<Object>();
        idList.add("");
        idList.add("");
        attributes.put("refIds", idList);

        NeutralRecord nr = new NeutralRecord();
        nr.setAttributes(attributes);
        Entity entity = new NeutralRecordEntity(nr);

        return entity;
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void shouldResolveNestedRef() {
        EntityConfig entityConfig =  createNestedRefConfig();
        
        @SuppressWarnings("unchecked")
        Repository<Entity> repo = Mockito.mock(Repository.class);
        //mock the repo query note this doesn't test whether the query was constructed correctly
        Mockito.when(repo.findByQuery(Mockito.eq("parentCollection"), Mockito.any(Query.class), 
                Mockito.eq(0), Mockito.eq(0))).thenReturn(getParentTargetEntities());
        Mockito.when(repo.findByQuery(Mockito.eq("childCollection"), Mockito.any(Query.class), 
                Mockito.eq(0), Mockito.eq(0))).thenReturn(getChildTargetEntities());

        Entity entity = createNestedSourceEntity(true); 
        ErrorReport errorReport = new TestErrorReport();
        
        IdNormalizer idNorm = new IdNormalizer();

        idNorm.setEntityRepository(repo);
        idNorm.resolveInternalIds(entity, "SLI", entityConfig, errorReport);
        
        assertNotNull("attribute parentId should not be null", entity.getBody().get("parentId"));
        assertEquals("attribute parentId should be resolved to parent_guid", "parent_guid", entity.getBody().get("parentId"));
        assertFalse("no errors should be reported from reference resolution ", errorReport.hasErrors());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void shouldFailWithUnresolvedNonNullChildRef() {
        EntityConfig entityConfig =  createNestedRefConfig();
        
        @SuppressWarnings("unchecked")
        Repository<Entity> repo = Mockito.mock(Repository.class);
        //mock the repo query note this doesn't test whether the query was constructed correctly
        Mockito.when(repo.findByQuery(Mockito.eq("parentCollection"), Mockito.any(Query.class), 
                Mockito.eq(0), Mockito.eq(0))).thenReturn(getParentTargetEntities());
        Mockito.when(repo.findByQuery(Mockito.eq("childCollection"), Mockito.any(Query.class), 
                Mockito.eq(0), Mockito.eq(0))).thenReturn(new ArrayList<Entity>());

        Entity entity = createNestedSourceEntity(true); 
        ErrorReport errorReport = new TestErrorReport();
        
        IdNormalizer idNorm = new IdNormalizer();

        idNorm.setEntityRepository(repo);
        idNorm.resolveInternalIds(entity, "SLI", entityConfig, errorReport);
        
        assertNull("attribute parentId should be null", entity.getBody().get("parentId"));
        assertTrue("errors should be reported from failed reference resolution ", errorReport.hasErrors());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void shouldResolveWithUnresolvedNullChildRef() {
        EntityConfig entityConfig =  createNestedRefConfig();
        
        @SuppressWarnings("unchecked")
        Repository<Entity> repo = Mockito.mock(Repository.class);
        //mock the repo query note this doesn't test whether the query was constructed correctly
        Mockito.when(repo.findByQuery(Mockito.eq("parentCollection"), Mockito.any(Query.class), 
                Mockito.eq(0), Mockito.eq(0))).thenReturn(getParentTargetEntities());
        Mockito.when(repo.findByQuery(Mockito.eq("childCollection"), Mockito.any(Query.class), 
                Mockito.eq(0), Mockito.eq(0))).thenReturn(new ArrayList<Entity>());

        Entity entity = createNestedSourceEntity(false); 
        ErrorReport errorReport = new TestErrorReport();
        
        IdNormalizer idNorm = new IdNormalizer();

        idNorm.setEntityRepository(repo);
        idNorm.resolveInternalIds(entity, "SLI", entityConfig, errorReport);
        
        assertNotNull("attribute parentId should not be null", entity.getBody().get("parentId"));
        assertEquals("attribute parentId should be resolved to parent_guid", "parent_guid", entity.getBody().get("parentId"));
        assertFalse("no errors should be reported from reference resolution ", errorReport.hasErrors());
    }
    
    //create nested ref
    private EntityConfig createNestedRefConfig() {
        Resource jsonFile = new ClassPathResource("idNormalizerTestConfigs/nestedRef.json");
        EntityConfig entityConfig = null;
        
        try {
            entityConfig = EntityConfig.parse(jsonFile.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return entityConfig;
    }
    
    //create nested source Entity
    private Entity createNestedSourceEntity(boolean includeChildRefValue) {
        
        Map<String, Object> parentRef = new HashMap<String, Object>();
        parentRef.put("srcOtherField", "otherFieldVal");
        
        if (includeChildRefValue) {
            parentRef.put("srcChildRefField", "childFieldValue");
        }
        
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("parentRef", parentRef);

        NeutralRecord nr = new NeutralRecord();
        nr.setAttributes(attributes);
        
        Entity entity = new NeutralRecordEntity(nr);
        
        return entity;
    }
    
    private List<Entity> getChildTargetEntities() {
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(createChildTargetEntity());
        
        return entities;
    }
    
    private Entity createChildTargetEntity() {
        
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("childField", "childFieldValue");

        NeutralRecord nr = new NeutralRecord();
        nr.setAttributes(attributes);
        
        NeutralRecordEntity entity = new NeutralRecordEntity(nr);
        entity.setEntityId("child_guid");
        
        return entity;
    }
    
    private List<Entity> getParentTargetEntities() {
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(createParentTargetEntity());
        
        return entities;
    }
    
    private Entity createParentTargetEntity() {
        
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("childId", "child_guid");

        NeutralRecord nr = new NeutralRecord();
        nr.setAttributes(attributes);
        
        NeutralRecordEntity entity = new NeutralRecordEntity(nr);
        entity.setEntityId("parent_guid");
        
        return entity;
    }
}
