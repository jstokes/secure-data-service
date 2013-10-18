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

import org.slc.sli.bulk.extract.extractor.EntityExtractor;
import org.slc.sli.bulk.extract.files.ExtractFile;
import org.slc.sli.bulk.extract.util.PublicEntityDefinition;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;

/**
 * PublicData Extractor which extracts all entities of belonging to a tenant.
 * @author tshewchuk
 */
public class UnfilteredPublicDataExtractor implements PublicDataExtractor{

    private EntityExtractor extractor;

    /**
     * Constructor.
     *
     * @param extractor - the entity extractor
     */
    public UnfilteredPublicDataExtractor(EntityExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public void extract(ExtractFile file) {
        for (PublicEntityDefinition entity : PublicEntityDefinition.unFilteredEntities()) {
            extractor.extractEntities(file, entity.getEntityName());
        }
     }
}
