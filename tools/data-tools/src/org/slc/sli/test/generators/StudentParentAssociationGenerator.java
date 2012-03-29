package org.slc.sli.test.generators;

import java.util.Random;

import org.slc.sli.test.edfi.entities.*;

public class StudentParentAssociationGenerator {
    
    public StudentParentAssociation generate(String studentId, String parentId) {
    	StudentParentAssociation studentParentAssociation = new StudentParentAssociation();

    	try {
            Random random = new Random();
            
            StudentIdentityType sit = new StudentIdentityType();
            sit.setStudentUniqueStateId(studentId);
            StudentReferenceType srt = new StudentReferenceType();
            srt.setStudentIdentity(sit);
            studentParentAssociation.setStudentReference(srt);

            ParentIdentityType pit = new ParentIdentityType();
            pit.setParentUniqueStateId(parentId);
            ParentReferenceType prt = new ParentReferenceType();
            prt.setParentIdentity(pit);
            studentParentAssociation.setParentReference(prt);
            
            studentParentAssociation.setRelation(random.nextBoolean() ? RelationType.MOTHER : RelationType.FATHER);
            
            studentParentAssociation.setPrimaryContactStatus(true);
            
            studentParentAssociation.setLivesWith(true);
            
            studentParentAssociation.setEmergencyContactStatus(true);
            
            studentParentAssociation.setContactPriority(1);
  
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return studentParentAssociation;
    }
}
