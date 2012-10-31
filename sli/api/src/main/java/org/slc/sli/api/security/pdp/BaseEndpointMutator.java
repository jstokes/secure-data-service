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

package org.slc.sli.api.security.pdp;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.PathSegment;

import org.slc.sli.api.security.SLIPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.container.ContainerRequest;

/**
 * @author dkornishev
 */
@Component
public class BaseEndpointMutator {

	@Resource
	private InferedURIHelper inferedURIHelper;

	public void mutateURI(Authentication auth, ContainerRequest request) {

		if (request.getMethod() == "POST") {
			return;
		}

		// TODO: Need to clean up this hack before turning on the path based context resolvers
		/* (Temporarily) Allow root calls with query parameters through */
		if (null != request.getRequestUri().getQuery()) {
			String[] queries = request.getRequestUri().getQuery().split("&");
			for (String query : queries) {
				if (!query.matches("(limit|offset|expandDepth|includeFields|excludeFields|sortBy|sortOrder|views|includeCustom|selector)=.+")) {
					return; // Escape if someone is attempting to query on entity fields
				}
			}
		}

		List<PathSegment> segs = request.getPathSegments();

		// remove empty segments because a trailing slash causes a segment, e.g. /v1/students/ products
		// ["v1","students", ""]
		for (Iterator<PathSegment> i = segs.iterator(); i.hasNext();) {
			if (i.next().getPath().isEmpty()) {
				i.remove();
			}
		}
		SLIPrincipal user = (SLIPrincipal) auth.getPrincipal();
		if (segs.get(0).getPath().equals("v1")) {
			if (segs.size() < 3) {
				request.getProperties().put("requestedPath", request.getPath());
				String newPath = inferedURIHelper.getInferredUri(segs.get(1).getPath(), user.getEntity());

				if (newPath != null) {
					String parameters = request.getRequestUri().getQuery();
					info("URI Rewrite from->to: {} -> {}", request.getPath(), newPath);
					request.setUris(request.getBaseUri(), request.getBaseUriBuilder().path(segs.get(0).getPath()).path(newPath).replaceQuery(parameters).build());
				}
			}
		}
	}

}
