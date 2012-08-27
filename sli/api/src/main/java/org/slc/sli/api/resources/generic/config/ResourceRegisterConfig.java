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
package org.slc.sli.api.resources.generic.config;

import com.sun.jersey.api.core.DefaultResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;


/**
 * Custom resource config for registering resources at runtime
 *
 * @author srupasinghe
 * @author jstokes
 *
 */

@Component
public class ResourceRegisterConfig extends DefaultResourceConfig {

    //private static final String WADL = "/wadl/base_wadl.wadl";

    @javax.annotation.Resource(name = "resourceSupportedMethods")
    private Map<String, Set<String>> resourceSupprtedMethods;

    @Autowired
    private ResourceEndPoint resourceEndPoint;

    public ResourceRegisterConfig() {
        super();
    }

    @PostConstruct
    public void init() throws ClassNotFoundException {
        addResources(resourceEndPoint.getResources());
    }

    protected void addResources(Map<String, String> resources) throws ClassNotFoundException {
        for (Map.Entry<String, String> resource : resources.entrySet()) {

            if (resource.getValue() != null && !resource.getValue().isEmpty()) {
                Class resourceClass = Class.forName(resource.getValue());

                getExplicitRootResources().put(resource.getKey(), resourceClass);
            }
        }
    }

//    protected Map<String, Resource> getResources() {
//        ResourceWadlHandler handler = new ResourceWadlHandler();
//        Application app = WadlReader.readApplication(getClass().getResourceAsStream(WADL));
//        WadlWalker walker = new WadlWalker(handler);
//
//        walker.walk(app);
//
//        return handler.getResourceEnds();
//    }
}
