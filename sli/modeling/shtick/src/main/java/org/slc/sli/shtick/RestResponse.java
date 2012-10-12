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

package org.slc.sli.shtick;

import java.util.List;
import java.util.Map;

/**
 * @author jstokes
 * 
 *         Intentionally package-protected.
 */
final class RestResponse {
    private String body;
    private int statusCode;
    private Map<String, List<String>> headers;
    
    RestResponse(final String body, final int statusCode, final Map<String, List<String>> headers) {
        this.body = body;
        this.statusCode = statusCode;
        this.headers = headers;
    }
    
    RestResponse(String body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }
    
    public String getBody() {
        return body;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public Map<String, List<String>> getHeaders() {
        return headers;
    }
    
    public String getHeader(String key) {
        if (this.headers.containsKey(key)) {
            // TODO : handle list case
            if (this.headers.get(key).size() >= 1) {
                return this.headers.get(key).get(0);
            }
        }
        
        throw new AssertionError();
    }
}
