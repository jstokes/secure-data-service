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

package org.slc.sli.test.generators;

import org.slc.sli.test.edfi.entities.SLCEducationalOrgIdentityType;
import org.slc.sli.test.edfi.entities.SLCEducationalOrgReferenceType;
import org.slc.sli.test.edfi.entities.SLCStaffEducationOrgAssignmentAssociation;
import org.slc.sli.test.edfi.entities.SLCStaffIdentityType;
import org.slc.sli.test.edfi.entities.SLCStaffReferenceType;
import org.slc.sli.test.edfi.entities.StaffClassificationType;
import org.slc.sli.test.edfi.entities.meta.StaffMeta;

public class StaffEdOrgAssignmentAssociationGenerator {

    public static SLCStaffEducationOrgAssignmentAssociation generateLowFi(StaffMeta staffMeta) {
        SLCStaffEducationOrgAssignmentAssociation staffEdOrgAssignmentAssoc = new SLCStaffEducationOrgAssignmentAssociation();

        SLCStaffReferenceType staffReferenceType = new SLCStaffReferenceType();
        SLCStaffIdentityType staffIdentityType = new SLCStaffIdentityType();
        staffIdentityType.setStaffUniqueStateId(staffMeta.id);
        staffReferenceType.setStaffIdentity(staffIdentityType);
        staffEdOrgAssignmentAssoc.setStaffReference(staffReferenceType);

        // staffReferenceType.setStaffIdentity(staffIdentityType);
        // staffEdOrgAssignmentAssoc.setStaffReference(staffReferenceType);

        SLCEducationalOrgIdentityType edOrgIdentity = new SLCEducationalOrgIdentityType();
        // edOrgIdentity.getStateOrganizationIdOrEducationOrgIdentificationCode().add(staffMeta.edOrgId);
        edOrgIdentity.setStateOrganizationId(staffMeta.edOrgId);

        SLCEducationalOrgReferenceType edOrgReferenceType = new SLCEducationalOrgReferenceType();
        edOrgReferenceType.setEducationalOrgIdentity(edOrgIdentity);

        staffEdOrgAssignmentAssoc.setEducationOrganizationReference(edOrgReferenceType);

        staffEdOrgAssignmentAssoc.setStaffClassification(StaffClassificationType.SPECIALIST_CONSULTANT);
        staffEdOrgAssignmentAssoc.setBeginDate("2012-01-01");

        return staffEdOrgAssignmentAssoc;
    }

}
