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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.core.PathSegment;

import com.sun.jersey.spi.container.ContainerRequest;

import org.apache.commons.lang3.tuple.Pair;
import org.slc.sli.api.constants.ResourceNames;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.security.SLIPrincipal;

/**
 * Determines whether or not the requested endpoint should be mutated.
 *
 * @author dkornishev
 */
@Component
public class EndpointMutator {

    private static final String POST = "POST";
    private static final String REQUESTED_PATH = "requestedPath";

    @Resource
    private UriMutator uriMutator;

    /**
     * Mutates the URI based on who the user is (provided in Authentication object) and what they're
     * requesting (provided in Container Request object).
     *
     * @param auth
     *            OAuth2Authentication object (contains principal for user).
     * @param request
     *            Container Request (contains path and query parameters).
     */
    public void mutateURI(Authentication auth, ContainerRequest request) {

        /*
         * Don't mutate POSTs.
         */
        if (request.getMethod().equals(POST)) {
            return;
        }

        SLIPrincipal user = (SLIPrincipal) auth.getPrincipal();
        List<PathSegment> segments = sanitizePathSegments(request);
        String parameters = request.getRequestUri().getQuery();

        if (usingV1Api(segments)) {
            request.getProperties().put(REQUESTED_PATH, request.getPath());
            Pair<String, String> mutated = uriMutator.mutate(segments, parameters, user.getEntity());
            String newPath = mutated.getLeft();
            String newParameters = mutated.getRight();

            if (newPath != null) {
                if (newParameters != null) {
                    info("URI Rewrite: {}?{} --> {}?{}", new Object[] { request.getPath(), parameters, newPath,
                            newParameters });
                    request.setUris(request.getBaseUri(),
                            request.getBaseUriBuilder().path(PathConstants.V1).path(newPath)
                                    .replaceQuery(newParameters).build());
                } else {
                    info("URI Rewrite: {} --> {}", new Object[] { request.getPath(), newPath });
                    request.setUris(request.getBaseUri(),
                            request.getBaseUriBuilder().path(PathConstants.V1).path(newPath).build());
                }
            }
        }
    }

    /**
     * Sanitizes the path segments currently contained in the request by removing empty segments.
     * This is required because a trailing slash causes an empty segment to exist, e.g.
     * /v1/students/ produces ["v1","students", ""].
     *
     * @param request
     *            Container Request to get path segments from.
     * @return Sane set of path segments.
     */
    protected List<PathSegment> sanitizePathSegments(ContainerRequest request) {
        List<PathSegment> segments = request.getPathSegments();
        for (Iterator<PathSegment> i = segments.iterator(); i.hasNext();) {
            if (i.next().getPath().isEmpty()) {
                i.remove();
            }
        }
        return segments;
    }

    /**
     * Validates that the list of path segments contains 'v1'.
     *
     * @param segments
     *            List of path segments.
     * @return True if using the v1 API, false otherwise.
     */
    protected boolean usingV1Api(List<PathSegment> segments) {
        return segments.get(0).getPath().equals(PathConstants.V1);
    }



}
