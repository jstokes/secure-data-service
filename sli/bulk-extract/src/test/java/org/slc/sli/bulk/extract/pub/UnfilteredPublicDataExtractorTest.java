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

package org.slc.sli.bulk.extract.pub;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.slc.sli.bulk.extract.extractor.EntityExtractor;
import org.slc.sli.bulk.extract.files.ExtractFile;

/**
 * Test UnfilteredPublicDataExtractor
 * @author tshewchuk
 */
public class UnfilteredPublicDataExtractorTest {

    private UnfilteredPublicDataExtractor publicDataExtractor;

    @Mock
    private EntityExtractor extractor;

    @Mock
    private ExtractFile file;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        publicDataExtractor = new UnfilteredPublicDataExtractor(extractor);
    }

    @Test
    public void testExtractAssessment() {
        publicDataExtractor.extract("SEA", file);
        Mockito.verify(extractor, Mockito.times(1)).extractEntities(file, "assessment");
    }

    @Test
    public void testExtractLearningObjective() {
        publicDataExtractor.extract("SEA", file);
        Mockito.verify(extractor, Mockito.times(1)).extractEntities(file, "learningObjective");
    }

    @Test
    public void testExtractLearningStandard() {
        publicDataExtractor.extract("SEA", file);
        Mockito.verify(extractor, Mockito.times(1)).extractEntities(file, "learningStandard");
    }

    @Test
    public void testExtractCompetencyLevelDescriptor() {
        publicDataExtractor.extract("SEA", file);
        Mockito.verify(extractor, Mockito.times(1)).extractEntities(file, "competencyLevelDescriptor");
    }

    @Test
    public void testExtractStudentCompetencyObjective() {
        publicDataExtractor.extract("SEA", file);
        Mockito.verify(extractor, Mockito.times(1)).extractEntities(file, "studentCompetencyObjective");
    }

    @Test
    public void testExtractProgram() {
        publicDataExtractor.extract("SEA", file);
        Mockito.verify(extractor, Mockito.times(1)).extractEntities(file, "program");
    }

}
