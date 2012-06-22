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


package org.slc.sli.lander;

import com.jcraft.jsch.UserInfo;

public class SimpleUserInfo implements UserInfo {

    private String password = null;

    public SimpleUserInfo(String password) {
        this.password = password;
    }

    public String getPassphrase() {
        return password;
    }

    public String getPassword() {
        return password;
    }

    public boolean promptPassphrase(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean promptPassword(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean promptYesNo(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void showMessage(String arg0) {
        // TODO Auto-generated method stub

    }

}
