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

import java.io.IOException;
import java.net.URI;

/**
 * @author jstokes
 */
public interface Level0Client {
    String get(final String token, final URI uri, final String mediaType) throws IOException, StatusCodeException;
    
    void delete(final String token, final URI uri, final String mediaType) throws IOException, StatusCodeException;
    
    URI post(final String token, final String data, final URI uri, final String mediaType) throws IOException,
            StatusCodeException;
    
    void put(final String token, final String data, final URI uri, final String mediaType) throws IOException,
            StatusCodeException;

    void patch(final String token, final String data, final URI uri, final String mediaType) throws IOException,
            StatusCodeException;
}
