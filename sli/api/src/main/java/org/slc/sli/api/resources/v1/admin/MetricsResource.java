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

package org.slc.sli.api.resources.v1.admin;

//
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;

/**
 * Provides system usage statistics.
 */
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
@Path(PathConstants.V1 + "/" + "metrics")
public class MetricsResource {

    @Autowired
    protected Repository<Entity> repo;

    /**
     * Calculates a usage statistic that matches the provided criteria.  Only accessible to SLC Operators.
     */
    @GET
    public Response getMetrics(@QueryParam("key") final String fieldKey, @QueryParam("value") final String fieldValue,
            @Context final UriInfo uriInfo) {

        SecurityUtil.ensureAuthenticated();
        if (!SecurityUtil.hasRight(Right.ADMIN_ACCESS)) {
            EntityBody body = new EntityBody();
            body.put("message", "You are not authorized to view collection metrics.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }

        CollectionMetrics metrics = MetricsResourceHelper.getAllCollectionMetrics(repo, fieldKey, fieldValue);
        metrics.put("== Totals ==", metrics.getTotals());

        return Response.ok(metrics).build();
    }
}
