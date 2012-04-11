package org.slc.sli.test.generators;

import org.slc.sli.test.edfi.entities.AcademicSubjectType;
import org.slc.sli.test.edfi.entities.GradeLevelType;
import org.slc.sli.test.edfi.entities.LearningObjective;
import org.slc.sli.test.edfi.entities.LearningObjectiveIdentityType;
import org.slc.sli.test.edfi.entities.LearningObjectiveReferenceType;
import org.slc.sli.test.edfi.entities.LearningStandardId;
import org.slc.sli.test.edfi.entities.LearningStandardIdentityType;
import org.slc.sli.test.edfi.entities.LearningStandardReferenceType;
import org.slc.sli.test.edfi.entities.relations.LearningObjectiveMeta;

public class LearningObjectiveGenerator {

    private int loId = 0;
    private LearningStandardReferenceType learningStandardRef;
    private LearningStandardId learningStandardId;

    public LearningObjectiveGenerator() {
        LearningStandardIdentityType lsIdentity = new LearningStandardIdentityType();
        learningStandardId = new LearningStandardId();
        lsIdentity.setLearningStandardId(learningStandardId);
        learningStandardId.setContentStandardName("Learning Standard Content Standard");
        learningStandardId.setIdentificationCode("Learning Standard Content Standard G1");
        learningStandardRef.setLearningStandardIdentity(lsIdentity);
    }

    public LearningObjective getLearningObjective(String learningObjectiveId) {
        loId++;
        LearningObjective lo = new LearningObjective();
        String id = learningObjectiveId == null ? ("LOID" + loId) : learningObjectiveId;
        lo.setId(id);

        LearningStandardId learningStdIdForObjective = new LearningStandardId();
        learningStdIdForObjective.setContentStandardName("Learning Standard Content Standard");
        learningStdIdForObjective.setIdentificationCode("Objective" + loId);

        lo.setLearningObjectiveId(learningStandardId);
        lo.setObjective("Learning Objective " + loId);
        lo.setDescription("Learning Objective Desciption " + loId);
        lo.setAcademicSubject(AcademicSubjectType.AGRICULTURE_FOOD_AND_NATURAL_RESOURCES);
        lo.setObjectiveGradeLevel(GradeLevelType.OTHER);
        lo.getLearningStandardReference().add(learningStandardRef);

        return lo;
    }

    public static LearningObjectiveReferenceType getLearningObjectiveReferenceType(LearningObjective lo) {
        LearningObjectiveIdentityType loId = new LearningObjectiveIdentityType();
        loId.getLearningObjectiveIdOrObjective().add(lo.getObjective());
        LearningObjectiveReferenceType lor = new LearningObjectiveReferenceType();
        lor.setLearningObjectiveIdentity(loId);
        return lor;
    }

    public static LearningObjectiveReferenceType getLearningObjectiveReferenceType(String learningStandardIdString) {
        LearningStandardId learningStandardId = new LearningStandardId();
        learningStandardId.setIdentificationCode(learningStandardIdString);
        learningStandardId.setContentStandardName(learningStandardIdString + "Content Standard");

        LearningObjectiveIdentityType loId = new LearningObjectiveIdentityType();
        loId.getLearningObjectiveIdOrObjective().add(learningStandardId);

        LearningObjectiveReferenceType lor = new LearningObjectiveReferenceType();
        lor.setLearningObjectiveIdentity(loId);
        return lor;
    }

    public static LearningObjective generateLowFi(LearningObjectiveMeta learningObjectiveMeta) {
        LearningObjective lo = new LearningObjective();

        LearningStandardId learningStdIdForObjective = new LearningStandardId();
        learningStdIdForObjective.setContentStandardName("Content Standard Name");
        learningStdIdForObjective.setIdentificationCode(learningObjectiveMeta.id);
        lo.setLearningObjectiveId(learningStdIdForObjective);

        lo.setObjective("Learning Objective ");
        lo.setDescription("Learning Objective Desciption ");
        lo.setAcademicSubject(AcademicSubjectType.AGRICULTURE_FOOD_AND_NATURAL_RESOURCES);
        lo.setObjectiveGradeLevel(GradeLevelType.OTHER);

        for (String learningStandardId : learningObjectiveMeta.learningStandardIds) {
            lo.getLearningStandardReference().add(
                    LearningStandardGenerator.getLearningStandardReferenceType(learningStandardId));
        }

        for (String learningStandardId : learningObjectiveMeta.learningObjectiveMetaIds) {
            lo.getLearningObjectiveReference().add(
                    LearningObjectiveGenerator.getLearningObjectiveReferenceType(learningStandardId));
        }

        return lo;
    }
}
