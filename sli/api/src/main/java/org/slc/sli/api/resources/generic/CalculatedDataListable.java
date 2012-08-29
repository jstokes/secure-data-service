/*
 *
 *  * Copyright 2012 Shared Learning Collaborative, LLC
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package org.slc.sli.api.resources.generic;

import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.resources.v1.aggregation.CalculatedDataListingResource;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * Jersey resource for calculated data listings
 *
 * @author srupasinghe
 */

public interface CalculatedDataListable {

    @Path("{id}/" + PathConstants.CALCULATED_VALUES)
    public CalculatedDataListingResource<String> getCalculatedValueResource(@PathParam("id") final String id,
                                                                            @Context final UriInfo uriInfo);
}
