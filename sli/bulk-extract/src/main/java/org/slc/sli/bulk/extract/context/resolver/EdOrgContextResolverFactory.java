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

package org.slc.sli.bulk.extract.context.resolver;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.bulk.extract.context.resolver.impl.EducationOrganizationContextResolver;
import org.slc.sli.domain.Entity;

@Component
public class EdOrgContextResolverFactory {
    
    @Autowired
    EducationOrganizationContextResolver edOrgContextResolver;

    final ContextResolver noOpResolver = new NullContextResolver();

    public ContextResolver getResolver(String entityType) {
        
        if ("educationOrganization".equals(entityType)) {
            return edOrgContextResolver;
        }

        return noOpResolver;
    }

    /*
     * no op context resolver that will always say your entity does not
     * belong to anybody
     */
    private static class NullContextResolver implements ContextResolver {
      
        // No op null resolver
        @Override
        public Set<String> findGoverningLEA(Entity entity) {
            return Collections.emptySet();
        }
        
    }
}
