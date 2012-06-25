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


package org.slc.sli.api.security.context.resolver;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * Filters the entity by a given date
 *
 * @author pghosh
 *
 */
@Component
public class StudentSectionAssociationEndDateFilter extends NodeDateFilter {

    private static final String END_DATE = "endDate";

    @Value("${sli.security.gracePeriod}")
    private String gracePeriodVal;

    @PostConstruct
    public void setParameters() {
        setParameters(gracePeriodVal, END_DATE);
    }
}
