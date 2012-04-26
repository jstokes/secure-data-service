package org.slc.sli.ingestion.transformation.assessment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import org.slc.sli.ingestion.Job;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;
import org.slc.sli.ingestion.dal.NeutralRecordRepository;

/**
 * Tests for LearningObjectiveTransform
 *
 * @author Ryan Farris <rfarris@wgen.net>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class LearningObjectiveTransformTest {

    @InjectMocks
    LearningObjectiveTransform transform = new LearningObjectiveTransform();

    @Mock
    NeutralRecordMongoAccess mongoAccess;

    @Mock
    Job job;

    @Mock
    NeutralRecordRepository repo;

    String transformCollection = LearningObjectiveTransform.LEARNING_OBJ_COLLECTION + "_transformed";

    @Before
    public void init() {

    }

    @Test
    public void testPerform() {
        Mockito.when(job.getId()).thenReturn("JOB_ID");
        Mockito.when(mongoAccess.getRecordRepository()).thenReturn(repo);

        NeutralRecord root = createNeutralRecord("root", "csn-0");
        NeutralRecord child1 = createNeutralRecord("child1", "csn-1");
        NeutralRecord child2 = createNeutralRecord("child2", "csn-2");
        NeutralRecord grandChild1 = createNeutralRecord("grandChild1", null);
        addChild(root, "child1", "csn-1");
        addChild(root, "child2", "csn-2");
        addChild(child1, "grandChild1", null);
        List<NeutralRecord> nrList = Arrays.asList(root, child1, child2, grandChild1);
        Mockito.when(repo.findAll("learningObjective")).thenReturn(nrList);

        transform.perform(job);

        Mockito.verify(repo).create(root);
        Mockito.verify(repo).create(child1);
        Mockito.verify(repo).create(child2);
        Mockito.verify(repo).create(grandChild1);

        Assert.assertEquals(null, root.getAttributes().get(LearningObjectiveTransform.SYNTHETIC_PARENT_ID));
        Assert.assertEquals("root", child1.getAttributes().get(LearningObjectiveTransform.SYNTHETIC_PARENT_ID));
        Assert.assertEquals("root", child2.getAttributes().get(LearningObjectiveTransform.SYNTHETIC_PARENT_ID));
        Assert.assertEquals("child1", grandChild1.getAttributes().get(LearningObjectiveTransform.SYNTHETIC_PARENT_ID));

        Assert.assertEquals(transformCollection, root.getRecordType());
        Assert.assertEquals(transformCollection, child1.getRecordType());
        Assert.assertEquals(transformCollection, child2.getRecordType());
        Assert.assertEquals(transformCollection, grandChild1.getRecordType());
    }

    private static NeutralRecord createNeutralRecord(String objectiveId, String contentStandardName) {
        NeutralRecord nr = new NeutralRecord();
        nr.setRecordType(LearningObjectiveTransform.LEARNING_OBJ_COLLECTION);
        nr.setAttributes(new HashMap<String, Object>());
        setAtPath(nr.getAttributes(), LearningObjectiveTransform.ID_CODE_PATH, objectiveId);
        setAtPath(nr.getAttributes(), LearningObjectiveTransform.CONTENT_STANDARD_NAME_PATH, contentStandardName);
        setAtPath(nr.getAttributes(), LearningObjectiveTransform.LEARNING_OBJ_REFS,
                new ArrayList<Map<String, Object>>());
        return nr;
    }

    @SuppressWarnings("unchecked")
    private static void addChild(NeutralRecord parent, String objectiveId, String contentStandardName) {
        List<Map<String, Object>> childRefs = (List<Map<String, Object>>) parent.getAttributes().get(
                LearningObjectiveTransform.LEARNING_OBJ_REFS);
        Map<String,Object> map = new HashMap<String,Object>();
        setAtPath(map, LearningObjectiveTransform.ID_CODE_PATH, objectiveId);
        setAtPath(map, LearningObjectiveTransform.CONTENT_STANDARD_NAME_PATH, contentStandardName);
        childRefs.add(map);
    }

    @SuppressWarnings("unchecked")
    private static void setAtPath(Map<String, Object> map, String path, Object value) {
        String[] fields = path.split("\\.");
        for (int i = 0; i < fields.length - 1; i++) {
            Map<String, Object> subMap = (Map<String, Object>) map.get(fields[i]);
            if (subMap == null) {
                subMap = new HashMap<String, Object>();
                map.put(fields[i], subMap);
            }
            map = subMap;
        }
        map.put(fields[fields.length - 1], value);
    }
}
