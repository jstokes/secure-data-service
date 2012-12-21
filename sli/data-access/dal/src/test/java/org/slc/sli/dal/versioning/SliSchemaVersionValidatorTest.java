/*
 * Copyright 2012 Shared Learning Collaborative, LLC
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

package org.slc.sli.dal.versioning;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slc.sli.dal.migration.strategy.MigrationStrategy;
import org.slc.sli.dal.migration.strategy.impl.AddStrategy;
import org.slc.sli.dal.repository.ValidationWithoutNaturalKeys;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.AppInfo;
import org.slc.sli.validation.schema.NeutralSchema;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Tests for schema version checking logic.
 * 
 * 
 * @author kmyers
 * 
 */
public class SliSchemaVersionValidatorTest {

    public static final String STUDENT = "student";
    public static final String SECTION = "section";
    public static final String TEACHER = "teacher";
    @InjectMocks
    private SliSchemaVersionValidator sliSchemaVersionValidator;

    @Mock
    private SchemaRepository entitySchemaRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() throws Exception {
        sliSchemaVersionValidator = new SliSchemaVersionValidator();
        sliSchemaVersionValidator.migrationConfigResource = new ClassPathResource(
                "migration/test-migration-config.json");
        MockitoAnnotations.initMocks(this);
    }

    private AppInfo createAppInfo(int version) {
        AppInfo appInfo = new AppInfo(null);
        appInfo.put(AppInfo.SCHEMA_VERSION, "" + version);
        return appInfo;
    }

    private NeutralSchema createMockSchema(String type, int version) {
        NeutralSchema mockNeutralSchema = mock(NeutralSchema.class);
        when(mockNeutralSchema.getAppInfo()).thenReturn(this.createAppInfo(version));
        when(mockNeutralSchema.getType()).thenReturn(type);
        return mockNeutralSchema;
    }

    @Test
    public void test() {

        List<EntityVersionData> data = Arrays.asList(new EntityVersionData[] {
                new EntityVersionData(STUDENT, 1, new CollectionVersionData(1, 0)),
                new EntityVersionData(SECTION, 2, new CollectionVersionData(1, 0)),
                new EntityVersionData(TEACHER, 1, null) });

        setupMockVersions(data);

        this.sliSchemaVersionValidator.initMigration();

        Mockito.verify(mongoTemplate, Mockito.times(1)).updateFirst(Mockito.any(Query.class),
                Mockito.any(Update.class), Mockito.any(String.class));
        Mockito.verify(mongoTemplate, Mockito.times(1)).insert(Mockito.any(Object.class), Mockito.any(String.class));

    }

    @Test
    public void shouldCreateAddStrategy() {
        this.sliSchemaVersionValidator.initMigration();

        List<MigrationStrategy> transforms = this.sliSchemaVersionValidator.getMigrationStrategies("student", 2);

        Assert.assertEquals(1, transforms.size());
        MigrationStrategy strategy = transforms.get(0);
        Assert.assertTrue("Expected AddStrategy", strategy instanceof AddStrategy);
    }

    @Test
    public void shouldAddVersion() {
        List<NeutralSchema> neutralSchemas = new ArrayList<NeutralSchema>();
        neutralSchemas.add(this.createMockSchema(STUDENT, 2));

        List<EntityVersionData> data = Arrays.asList(new EntityVersionData[] { new EntityVersionData(STUDENT, 2,
                new CollectionVersionData(1, 0)) });

        setupMockVersions(data);

        this.sliSchemaVersionValidator.initMigration();

        Entity student = new MongoEntity("student", new HashMap<String, Object>());
        this.sliSchemaVersionValidator.insertVersionInformation(student);

        Assert.assertEquals(2, student.getMetaData().get("version"));
    }

    @Test
    public void shouldMigrateEntities() {

        ValidationWithoutNaturalKeys mockRepo = Mockito.mock(ValidationWithoutNaturalKeys.class);

        List<NeutralSchema> neutralSchemas = new ArrayList<NeutralSchema>();
        neutralSchemas.add(this.createMockSchema(STUDENT, 2));

        List<EntityVersionData> data = Arrays.asList(new EntityVersionData[] { new EntityVersionData(STUDENT, 2,
                new CollectionVersionData(1, 0)) });

        setupMockVersions(data);

        this.sliSchemaVersionValidator.initMigration();

        List<Entity> students = new ArrayList<Entity>();
        students.add(new MongoEntity("student", new HashMap<String, Object>()));
        students.add(new MongoEntity("student", new HashMap<String, Object>()));
        students.add(new MongoEntity("student", new HashMap<String, Object>()));

        Iterable<Entity> result = sliSchemaVersionValidator.migrate("student", students, mockRepo);

        for (Entity e : result) {
            Assert.assertEquals("yellow", e.getBody().get("favoriteColor"));
        }

    }

    private void setupMockVersions(List<EntityVersionData> enitityData) {

        List<NeutralSchema> neutralSchemas = new ArrayList<NeutralSchema>();

        for (EntityVersionData data : enitityData) {
            neutralSchemas.add(this.createMockSchema(data.entityType, data.schemaVersion));
        }

        when(entitySchemaRepository.getSchemas()).thenReturn(neutralSchemas);

        final List<DBObject> findOnes = new ArrayList<DBObject>();
        for (EntityVersionData data : enitityData) {

            BasicDBObject dbObject = null;
            if (data.collectionVersion != null) {
                dbObject = new BasicDBObject();
                dbObject.put(SliSchemaVersionValidator.ID, data.entityType);
                dbObject.put(SliSchemaVersionValidator.DAL_SV, new Double(data.collectionVersion.dalVersion));
                dbObject.put(SliSchemaVersionValidator.MONGO_SV, new Double(data.collectionVersion.mongoVersion));
            }

            findOnes.add(dbObject);
        }

        when(
                mongoTemplate.findOne(Mockito.any(Query.class), Mockito.eq(BasicDBObject.class),
                        Mockito.eq(SliSchemaVersionValidator.METADATA_COLLECTION))).thenAnswer(new Answer<DBObject>() {
            @Override
            public DBObject answer(InvocationOnMock invocation) throws Throwable {
                return findOnes.remove(0);
            }
        });
    }

    private class EntityVersionData {
        public final String entityType;
        public final int schemaVersion;
        public final CollectionVersionData collectionVersion;

        public EntityVersionData(String entityType, int schemaVersion, CollectionVersionData collectionVersion) {
            super();
            this.entityType = entityType;
            this.schemaVersion = schemaVersion;
            this.collectionVersion = collectionVersion;
        }
    }

    private class CollectionVersionData {
        public final int dalVersion;
        public final int mongoVersion;

        public CollectionVersionData(int dalVersion, int mongoVersion) {
            super();
            this.dalVersion = dalVersion;
            this.mongoVersion = mongoVersion;
        }
    }

}
