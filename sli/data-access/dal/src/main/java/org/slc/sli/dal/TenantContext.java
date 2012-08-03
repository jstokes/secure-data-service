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

package org.slc.sli.dal;

/**
 * Class that provides thread-local context for non-local jump use cases.
 */
public class TenantContext {

    private static ThreadLocal<String> threadLocalTenantId = new ThreadLocal<String>();

    private static ThreadLocal<String> threadLocalJobId = new ThreadLocal<String>();

    /**
     * Get the tenant id local to this thread.
     *
     * @return tenant id.
     */
    public static String getTenantId() {
        return threadLocalTenantId.get();
    }

    /**
     * Set the tenant id local to this thread.
     *
     * @param tenantId
     */
    public static void setTenantId(String tenantId) {
        threadLocalTenantId.set(tenantId);
    }

    /**
     * Set the job id local to this thread.
     *
     * @param jobId
     */
    public static void setJobId(String jobId) {
        threadLocalJobId.set(jobId);
    }

    /**
     * Get the job id local to this thread.
     *
     * @return job id.
     */
    public static String getJobId() {
        return threadLocalJobId.get();
    }

}
