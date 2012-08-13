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

package org.slc.sli.sif.domain.slientity;

/**
 * Corresponding to the (personal) telephone defined in SLI schema.
 *
 * @author slee
 *
 */
public class PersonalTelephone {
    private String telephoneNumber;
    private String telephoneNumberType;

    public PersonalTelephone() {

    }

    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getTelephoneNumberType() {
        return this.telephoneNumberType;
    }

    public void setTelephoneNumberType(
            String telephoneNumberType) {
        this.telephoneNumberType = telephoneNumberType;
    }
}

