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
package org.slc.sli.ingestion.landingzone;

import java.io.File;

import org.slc.sli.ingestion.Resource;

/**
 * Adapter for a java.util.File to be treated as an ingestion Resource.
 *
 * @author dduran
 *
 */
public class FileResource extends File implements Resource {

    public FileResource(String pathname) {
        super(pathname);
    }

    private static final long serialVersionUID = 301077366599182938L;

    @Override
    public String getResourceId() {
        return getName();
    }

}
