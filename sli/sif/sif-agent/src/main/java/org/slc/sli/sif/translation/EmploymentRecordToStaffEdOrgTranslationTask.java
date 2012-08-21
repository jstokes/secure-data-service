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
package org.slc.sli.sif.translation;

import java.util.ArrayList;
import java.util.List;

import openadk.library.hrfin.EmploymentRecord;

import org.springframework.beans.factory.annotation.Autowired;

import org.slc.sli.api.client.Entity;
import org.slc.sli.sif.domain.converter.DateConverter;
import org.slc.sli.sif.domain.slientity.StaffEducationOrganizationAssociationEntity;
import org.slc.sli.sif.slcinterface.SifIdResolver;

public class EmploymentRecordToStaffEdOrgTranslationTask extends
        AbstractTranslationTask<EmploymentRecord, StaffEducationOrganizationAssociationEntity> {

    @Autowired
    SifIdResolver sifIdResolver;

    @Autowired
    DateConverter dateConverter;

    private static final String TEACHER_TYPE = "teacher";
    private static final String SCHOOL_TYPE = "school";


    public EmploymentRecordToStaffEdOrgTranslationTask() {
        super(EmploymentRecord.class);
    }

    @Override
    public List<StaffEducationOrganizationAssociationEntity> doTranslate(EmploymentRecord sifData, String zoneId) {
        List<StaffEducationOrganizationAssociationEntity> result = new ArrayList<StaffEducationOrganizationAssociationEntity>();

        if (sifData == null) {
            return result;
        }

        Entity staff = sifIdResolver.getSliEntity(sifData.getSIF_RefId(), zoneId);
        Entity edOrg = sifIdResolver.getSliEntity(sifData.getLEAInfoRefId(), zoneId);

        if( TEACHER_TYPE.equals(staff.getEntityType()) && SCHOOL_TYPE.equals(edOrg.getEntityType())){
            // not handled by this translator
            return result;
        }

        StaffEducationOrganizationAssociationEntity e = new StaffEducationOrganizationAssociationEntity();

        String leaId = sifData.getLEAInfoRefId();
        e.setEducationOrganizationReference(sifIdResolver.getSliGuid(leaId, zoneId));
        e.setPositionTitle(sifData.getPositionTitle());
        e.setBeginDate(dateConverter.convert(sifData.getHireDate()));
        e.setEndDate(dateConverter.convert(sifData.getTerminationDate()));

        // This is a default value, missing field in SIF
        e.setStaffClassification("Other");


        result.add(e);
        return result;
    }

}
