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


package org.slc.sli.ingestion.processors;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.ingestion.routes.LandingZoneRouteBuilder;
import org.slc.sli.ingestion.tenant.TenantDA;

/**
 * Processor for tenant collection polling
 *
 * @author jtully
 *
 */
@Component
public class TenantProcessor implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(TenantProcessor.class);

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private TenantDA tenantDA;

    private String workItemQueueUri;

    @Autowired
    private ZipFileProcessor zipFileProcessor;

    @Autowired
    private ControlFilePreProcessor controlFilePreProcessor;

    @Autowired
    private NoExtractProcessor noExtractProcessor;
    public static final String TENANT_POLL_HEADER = "TENANT_POLL_STATUS";
    public static final String TENANT_POLL_SUCCESS = "SUCCESS";
    public static final String TENANT_POLL_FAILURE = "FAILURE";

    @Override
    public void process(Exchange exchange) throws Exception {

        //We need to extract the TenantID for each thread, so the DAL has access to it.
//        try {
//            ControlFileDescriptor cfd = exchange.getIn().getBody(ControlFileDescriptor.class);
//            ControlFile cf = cfd.getFileItem();
//            String tenantId = cf.getConfigProperties().getProperty("tenantId");
//            TenantContext.setTenantId(tenantId);
//        } catch (NullPointerException ex) {
//            LOG.error("Could Not find Tenant ID.");
//            TenantContext.setTenantId(null);
//        }

        try {
            updateLzRoutes();

            exchange.getIn().setHeader(TENANT_POLL_HEADER, TENANT_POLL_SUCCESS);

        } catch (Exception e) {
            exchange.getIn().setHeader(TENANT_POLL_HEADER, TENANT_POLL_FAILURE);
            LOG.error("Exception encountered adding tenant", e);
        }
    }

    public void setWorkItemQueueUri(String workItemQueueUri) {
        this.workItemQueueUri = workItemQueueUri;
    }
    /**
     * Update the landing zone routes based on the tenant DB collection.
     * @throws Exception
     */
    private void updateLzRoutes() throws Exception {
        //get the new list of lz paths from the tenant DB collection
        LOG.debug("Localhost is {}", getHostname());
        List<String> newLzPaths = tenantDA.getLzPaths(getHostname());
        Set<String> oldLzPaths = getLzRoutePaths();

        List<String> routesToAdd = new ArrayList<String>();

        for (String lzPath : newLzPaths) {
            if (oldLzPaths.contains(lzPath)) {
                oldLzPaths.remove(lzPath);
            } else {
                routesToAdd.add(lzPath);
            }
        }

        //add new routes
        if (routesToAdd.size() > 0) {
            addRoutes(routesToAdd);
        }

        //remove routes for oldLzPaths that were not found in DB collection
        removeRoutes(oldLzPaths);
    }

    /**
     * Find the landing zones that are currently being monitored by
     * the ingestion engine.
     *
     * @return a set of the landing zone paths being polled
     */
    private Set<String> getLzRoutePaths() {
        Set<String> routePaths = new HashSet<String>();
        List<Route> routes = camelContext.getRoutes();
        for (Route curRoute : routes) {
            String routeId = curRoute.getId();
            if (routeId.contains(LandingZoneRouteBuilder.CTRL_POLLER_PREFIX)) {
                routePaths.add(routeId.replace(LandingZoneRouteBuilder.CTRL_POLLER_PREFIX, ""));
            }
        }
        return routePaths;
    }

    /**
     * Remove routes from camel context.
     * @throws Exception if a route cannot be removed
     */
    private void removeRoutes(Set<String> routesToRemove) throws Exception {
        for (String routePath : routesToRemove) {
            String zipRouteId = LandingZoneRouteBuilder.ZIP_POLLER_PREFIX + routePath;
            String ctrlRouteId = LandingZoneRouteBuilder.CTRL_POLLER_PREFIX + routePath;
            //initiate graceful shutdown of these routes
            camelContext.stopRoute(zipRouteId);
            camelContext.stopRoute(ctrlRouteId);
        }
    }

    /**
     * Add routes to camel context.
     * @throws Exception if a route cannot be resolved
     */
    private void addRoutes(List<String> routesToAdd) throws Exception {
        RouteBuilder landingZoneRouteBuilder = new LandingZoneRouteBuilder(routesToAdd,
                workItemQueueUri, zipFileProcessor, controlFilePreProcessor, noExtractProcessor);
        camelContext.addRoutes(landingZoneRouteBuilder);
    }

    /**
     * Obtain the hostname for the ingestion server running.
     * @throws UnknownHostException
     */
    private String getHostname() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    public boolean preLoad(String landingZone, List<String> preLoadedFiles) {
        return false;
    }

}
