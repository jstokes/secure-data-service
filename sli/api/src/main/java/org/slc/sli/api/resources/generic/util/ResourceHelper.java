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

package org.slc.sli.api.resources.generic.util;

import javax.ws.rs.core.UriInfo;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.resources.generic.representation.Resource;

/**
 * @author jstokes
 */
public interface ResourceHelper {
    public Resource getResourceName(UriInfo uriInfo, ResourceTemplate template);
    public Resource getBaseName(UriInfo uriInfo, ResourceTemplate template);
    public String getResourcePath(UriInfo uriInfo, ResourceTemplate template);
    public Resource getAssociationName(UriInfo uriInfo, ResourceTemplate template);
    public EntityDefinition getEntityDefinition(final Resource resource);
    public EntityDefinition getEntityDefinition(final String resource);
    public EntityDefinition getEntityDefinitionByType(String type);
    public boolean resolveResourcePath(final String uri, final ResourceTemplate template);
}
