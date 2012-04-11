package org.slc.sli.test.mappingGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.slc.sli.test.edfi.entities.relations.AssessmentFamilyMeta;
import org.slc.sli.test.edfi.entities.relations.AssessmentItemMeta;
import org.slc.sli.test.edfi.entities.relations.AssessmentMeta;
import org.slc.sli.test.edfi.entities.relations.AssessmentPeriodDescriptorMeta;
import org.slc.sli.test.edfi.entities.relations.LearningObjectiveMeta;
import org.slc.sli.test.edfi.entities.relations.LearningStandardMeta;
import org.slc.sli.test.edfi.entities.relations.ObjectiveAssessmentMeta;
import org.slc.sli.test.edfi.entities.relations.PerformanceLevelDescriptorMeta;

public class AssessmentMetaRelations {

    // knobs to control number of entities to create
    public static final int ASSESSMENTS = 1;
    public static final int OBJ_ASSESS_PER_DEPENDANT = 2;
    public static final int LEARN_OBJ_PER_OBJ_ASSES = 2;
    public static final int ASSESS_ITEM_PER_DEPENDANT = 2;
    public static final int LEARN_STANDARD_PER_DEPENDANT = 2;
    public static final int PERF_LEVEL_DESC_PER_DEPENDANT = 2;
    public static final int ASSESS_PERIOD_DESC_PER_ASSESS_FAMILY = 2;

    // publicly accessible structures for the "meta-skeleton" entities
    public static final Map<String, AssessmentMeta> ASSESSMENT_MAP = new TreeMap<String, AssessmentMeta>();
    public static final Map<String, ObjectiveAssessmentMeta> OBJECTIVE_ASSESSMENT_MAP = new TreeMap<String, ObjectiveAssessmentMeta>();
    public static final Map<String, LearningObjectiveMeta> LEARNING_OBJECTIVE_MAP = new TreeMap<String, LearningObjectiveMeta>();
    public static final Map<String, AssessmentItemMeta> ASSESSMENT_ITEM_MAP = new TreeMap<String, AssessmentItemMeta>();
    public static final Map<String, LearningStandardMeta> LEARN_STD_MAP = new TreeMap<String, LearningStandardMeta>();
    public static final Map<String, PerformanceLevelDescriptorMeta> PERF_LEVEL_DESC_MAP = new TreeMap<String, PerformanceLevelDescriptorMeta>();
    public static final Map<String, AssessmentFamilyMeta> ASSESSMENT_FAMILY_MAP = new TreeMap<String, AssessmentFamilyMeta>();
    public static final Map<String, AssessmentPeriodDescriptorMeta> ASSESS_PERIOD_DESC_MAP = new TreeMap<String, AssessmentPeriodDescriptorMeta>();

    // private constants
    private static final Random RANDOM = new Random();
    private static final String ASSESS_PREFIX = "assess";
    private static final String OBJ_ASSESS_PREFIX = "objAssess";
    private static final String LEARN_OBJ_PREFIX = "learnObj";
    private static final String ASSESS_ITEM_PREFIX = "assessItem";
    private static final String LEARN_STD_PREFIX = "learnStd";
    private static final String PERF_LEVEL_DESC_PREFIX = "pld";
    private static final String ASSESS_FAMILY_PREFIX = "assessFam";
    private static final String ASSESS_PERIOD_DESC_PREFIX = "apd";

    public static void buildStandaloneAssessments() {
        long startTime = System.currentTimeMillis();

        buildAssessment();

        System.out.println("Time taken to build Assessment entity relations: "
                + (System.currentTimeMillis() - startTime));
    }

    private static void buildAssessment() {

        for (int idNum = 0; idNum < ASSESSMENTS; idNum++) {

            AssessmentMeta assessmentMeta = AssessmentMeta.create(ASSESS_PREFIX + idNum);

            // getSections

            for (ObjectiveAssessmentMeta objAssessMeta : aggregateObjectiveAssessments()) {
                assessmentMeta.objectiveAssessmentIds.add(objAssessMeta.id);
            }

            for (AssessmentItemMeta assessmentItemMeta : aggregateAssessmentItems()) {
                assessmentMeta.assessmentItemIds.add(assessmentItemMeta.id);
            }

            for (PerformanceLevelDescriptorMeta perfLevelDescMeta : aggregatePerfLevelDesc()) {
                assessmentMeta.performanceLevelDescriptorIds.add(perfLevelDescMeta.id);
            }

            assessmentMeta.assessmentFamilyId = getOrCreateAssessmentFamily().id;

            assessmentMeta.assessmentPeriodDescriptorId = getOrCreateAssessmentPeriodDescriptor().id;

            ASSESSMENT_MAP.put(assessmentMeta.id, assessmentMeta);
        }
    }

    private static List<ObjectiveAssessmentMeta> aggregateObjectiveAssessments() {

        List<ObjectiveAssessmentMeta> objAssessList = new ArrayList<ObjectiveAssessmentMeta>();
        for (int idNum = 0; idNum < OBJ_ASSESS_PER_DEPENDANT; idNum++) {

            objAssessList.add(getOrCreateObjectiveAssessment());
        }
        return objAssessList;
    }

    private static List<LearningObjectiveMeta> aggregateLearningObjectivesForObjAssess() {

        List<LearningObjectiveMeta> learningObjectiveList = new ArrayList<LearningObjectiveMeta>();
        for (int idNum = 0; idNum < LEARN_OBJ_PER_OBJ_ASSES; idNum++) {

            learningObjectiveList.add(getOrCreateLearningObjective());
        }
        return learningObjectiveList;
    }

    private static List<AssessmentItemMeta> aggregateAssessmentItems() {

        List<AssessmentItemMeta> assessmentItemList = new ArrayList<AssessmentItemMeta>();
        for (int idNum = 0; idNum < ASSESS_ITEM_PER_DEPENDANT; idNum++) {

            assessmentItemList.add(getOrCreateAssessmentItem());
        }
        return assessmentItemList;
    }

    private static List<LearningStandardMeta> aggregateLearningStandards() {

        List<LearningStandardMeta> learningStandardList = new ArrayList<LearningStandardMeta>();
        for (int idNum = 0; idNum < LEARN_STANDARD_PER_DEPENDANT; idNum++) {

            learningStandardList.add(getOrCreateLearningStandard());
        }
        return learningStandardList;
    }

    private static List<PerformanceLevelDescriptorMeta> aggregatePerfLevelDesc() {

        List<PerformanceLevelDescriptorMeta> perfLevelDescList = new ArrayList<PerformanceLevelDescriptorMeta>();
        for (int idNum = 0; idNum < PERF_LEVEL_DESC_PER_DEPENDANT; idNum++) {

            perfLevelDescList.add(getOrCreatePerfLevelDesc());
        }
        return perfLevelDescList;
    }

    private static List<AssessmentPeriodDescriptorMeta> aggregateAssessmentPeriodDescriptors() {

        List<AssessmentPeriodDescriptorMeta> assessPeriodDescList = new ArrayList<AssessmentPeriodDescriptorMeta>();
        for (int idNum = 0; idNum < ASSESS_PERIOD_DESC_PER_ASSESS_FAMILY; idNum++) {

            assessPeriodDescList.add(getOrCreateAssessmentPeriodDescriptor());
        }
        return assessPeriodDescList;
    }

    private static ObjectiveAssessmentMeta getOrCreateObjectiveAssessment() {
        ObjectiveAssessmentMeta objAssessMeta = null;

        if (OBJECTIVE_ASSESSMENT_MAP.size() > 0 && RANDOM.nextBoolean()) {
            objAssessMeta = OBJECTIVE_ASSESSMENT_MAP.get(OBJ_ASSESS_PREFIX
                    + RANDOM.nextInt(OBJECTIVE_ASSESSMENT_MAP.size()));
        }

        if (objAssessMeta == null) {
            objAssessMeta = createObjectiveAssessment();
        }
        return objAssessMeta;
    }

    private static LearningObjectiveMeta getOrCreateLearningObjective() {
        LearningObjectiveMeta learningObjectiveMeta = null;

        if (LEARNING_OBJECTIVE_MAP.size() > 0 && RANDOM.nextBoolean()) {
            learningObjectiveMeta = LEARNING_OBJECTIVE_MAP.get(LEARN_OBJ_PREFIX
                    + RANDOM.nextInt(LEARNING_OBJECTIVE_MAP.size()));
        }

        if (learningObjectiveMeta == null) {
            learningObjectiveMeta = createLearningObjective();
        }
        return learningObjectiveMeta;
    }

    private static AssessmentItemMeta getOrCreateAssessmentItem() {
        AssessmentItemMeta assessmentItemMeta = null;

        if (ASSESSMENT_ITEM_MAP.size() > 0 && RANDOM.nextBoolean()) {
            assessmentItemMeta = ASSESSMENT_ITEM_MAP.get(ASSESS_ITEM_PREFIX
                    + RANDOM.nextInt(ASSESSMENT_ITEM_MAP.size()));
        }

        if (assessmentItemMeta == null) {
            assessmentItemMeta = createAssessmentItem();
        }
        return assessmentItemMeta;
    }

    private static LearningStandardMeta getOrCreateLearningStandard() {
        LearningStandardMeta learningStandardMeta = null;

        if (LEARN_STD_MAP.size() > 0 && RANDOM.nextBoolean()) {
            learningStandardMeta = LEARN_STD_MAP.get(LEARN_STD_PREFIX + RANDOM.nextInt(LEARN_STD_MAP.size()));
        }

        if (learningStandardMeta == null) {
            learningStandardMeta = createLearningStandard();
        }
        return learningStandardMeta;
    }

    private static PerformanceLevelDescriptorMeta getOrCreatePerfLevelDesc() {
        PerformanceLevelDescriptorMeta perfLevelDescMeta = null;

        if (PERF_LEVEL_DESC_MAP.size() > 0 && RANDOM.nextBoolean()) {
            perfLevelDescMeta = PERF_LEVEL_DESC_MAP.get(PERF_LEVEL_DESC_PREFIX
                    + RANDOM.nextInt(PERF_LEVEL_DESC_MAP.size()));
        }

        if (perfLevelDescMeta == null) {
            perfLevelDescMeta = createPerformanceLevelDescriptor();
        }
        return perfLevelDescMeta;
    }

    private static AssessmentFamilyMeta getOrCreateAssessmentFamily() {
        AssessmentFamilyMeta assessmentFamilyMeta = null;

        if (ASSESSMENT_FAMILY_MAP.size() > 0 && RANDOM.nextBoolean()) {
            assessmentFamilyMeta = ASSESSMENT_FAMILY_MAP.get(ASSESS_FAMILY_PREFIX
                    + RANDOM.nextInt(ASSESSMENT_FAMILY_MAP.size()));
        }

        if (assessmentFamilyMeta == null) {
            assessmentFamilyMeta = createAssessmentFamily();
        }
        return assessmentFamilyMeta;
    }

    private static AssessmentPeriodDescriptorMeta getOrCreateAssessmentPeriodDescriptor() {
        AssessmentPeriodDescriptorMeta assessPeriodDescMeta = null;

        if (ASSESS_PERIOD_DESC_MAP.size() != 0 && RANDOM.nextBoolean()) {
            assessPeriodDescMeta = ASSESS_PERIOD_DESC_MAP.get(ASSESS_PERIOD_DESC_PREFIX
                    + RANDOM.nextInt(ASSESS_PERIOD_DESC_MAP.size()));
        }

        if (assessPeriodDescMeta == null) {
            assessPeriodDescMeta = createAssessmentPeriodDescriptor();
        }
        return assessPeriodDescMeta;
    }

    private static ObjectiveAssessmentMeta createObjectiveAssessment() {
        ObjectiveAssessmentMeta objAssessMeta = ObjectiveAssessmentMeta.create(OBJ_ASSESS_PREFIX
                + OBJECTIVE_ASSESSMENT_MAP.size());

        for (LearningObjectiveMeta learningObjectiveMeta : aggregateLearningObjectivesForObjAssess()) {
            objAssessMeta.learningObjectiveIds.add(learningObjectiveMeta.id);
        }

        for (AssessmentItemMeta assessmentItemMeta : aggregateAssessmentItems()) {
            objAssessMeta.assessmentItemIds.add(assessmentItemMeta.id);
        }

        for (LearningStandardMeta learningStandardMeta : aggregateLearningStandards()) {
            objAssessMeta.learningStandardIds.add(learningStandardMeta.id);
        }

        for (PerformanceLevelDescriptorMeta perfLevelDescMeta : aggregatePerfLevelDesc()) {
            objAssessMeta.performanceLevelDescriptorIds.add(perfLevelDescMeta.id);
        }

        OBJECTIVE_ASSESSMENT_MAP.put(objAssessMeta.id, objAssessMeta);
        return objAssessMeta;
    }

    private static LearningObjectiveMeta createLearningObjective() {
        LearningObjectiveMeta learningObjectiveMeta = LearningObjectiveMeta.create(LEARN_OBJ_PREFIX
                + LEARNING_OBJECTIVE_MAP.size());

        for (LearningStandardMeta learningStandardMeta : aggregateLearningStandards()) {
            learningObjectiveMeta.learningStandardIds.add(learningStandardMeta.id);
        }
        LEARNING_OBJECTIVE_MAP.put(learningObjectiveMeta.id, learningObjectiveMeta);
        return learningObjectiveMeta;
    }

    private static AssessmentItemMeta createAssessmentItem() {
        AssessmentItemMeta assessmentItemMeta = AssessmentItemMeta.create(ASSESS_ITEM_PREFIX
                + ASSESSMENT_ITEM_MAP.size());

        for (LearningStandardMeta learningStandardMeta : aggregateLearningStandards()) {
            assessmentItemMeta.learningStandardIds.add(learningStandardMeta.id);
        }
        ASSESSMENT_ITEM_MAP.put(assessmentItemMeta.id, assessmentItemMeta);
        return assessmentItemMeta;
    }

    private static LearningStandardMeta createLearningStandard() {
        LearningStandardMeta learningStandardMeta = LearningStandardMeta
                .create(LEARN_STD_PREFIX + LEARN_STD_MAP.size());
        LEARN_STD_MAP.put(learningStandardMeta.id, learningStandardMeta);
        return learningStandardMeta;
    }

    private static PerformanceLevelDescriptorMeta createPerformanceLevelDescriptor() {
        PerformanceLevelDescriptorMeta perfLevelDescMeta = PerformanceLevelDescriptorMeta.create(PERF_LEVEL_DESC_PREFIX
                + PERF_LEVEL_DESC_MAP.size());
        PERF_LEVEL_DESC_MAP.put(perfLevelDescMeta.id, perfLevelDescMeta);
        return perfLevelDescMeta;
    }

    private static AssessmentFamilyMeta createAssessmentFamily() {
        AssessmentFamilyMeta assessmentFamilyMeta = AssessmentFamilyMeta.create(ASSESS_FAMILY_PREFIX
                + ASSESSMENT_FAMILY_MAP.size());

        for (AssessmentPeriodDescriptorMeta assessPeriodDescMeta : aggregateAssessmentPeriodDescriptors()) {
            assessmentFamilyMeta.assessmentPeriodDescriptorIds.add(assessPeriodDescMeta.id);
        }
        ASSESSMENT_FAMILY_MAP.put(assessmentFamilyMeta.id, assessmentFamilyMeta);
        return assessmentFamilyMeta;
    }

    private static AssessmentPeriodDescriptorMeta createAssessmentPeriodDescriptor() {
        AssessmentPeriodDescriptorMeta assessPeriodDescMeta = AssessmentPeriodDescriptorMeta
                .create(ASSESS_PERIOD_DESC_PREFIX + ASSESS_PERIOD_DESC_MAP.size());
        ASSESS_PERIOD_DESC_MAP.put(assessPeriodDescMeta.id, assessPeriodDescMeta);
        return assessPeriodDescMeta;
    }

    public static void main(String[] args) {

        buildStandaloneAssessments();

        System.out.println(ASSESSMENT_MAP);
        System.out.println(OBJECTIVE_ASSESSMENT_MAP);
        System.out.println(LEARNING_OBJECTIVE_MAP);
        System.out.println(ASSESSMENT_ITEM_MAP);
        System.out.println(LEARN_STD_MAP);
        System.out.println(PERF_LEVEL_DESC_MAP);
        System.out.println(ASSESSMENT_FAMILY_MAP);
        System.out.println(ASSESS_PERIOD_DESC_MAP);

    }
}
