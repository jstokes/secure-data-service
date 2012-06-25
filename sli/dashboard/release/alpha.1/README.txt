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

For the purposes of this README, we will use /opt/tomcat as $TOMCAT_HOME

===============================================================================

1. Set up the keystore
    -- Download the Java Cryptography Extension Unlimited Strength 
       Jurisdiction Policy Files from 
       http://www.oracle.com/technetwork/java/javase/downloads/index.html
    -- Place the two files  in the downloaded  <JRE_HOME>/lib/security. If you 
       have JDK installed it will be <JDK_HOME>/jre/lib/security/

    -- Log in with Admin
    -- Go into the App Admin 
    -- Add the Dashboard. 
    -- Upon registering the app, you will receive values for client_id and client_secret. These values must be encrypted. To do so:
    -- Using EncryptionGenerator.jar (included), run the following command:
         java -jar EncyptionGenerator.jar <location_of_keyStore> < keystore_password> <key_alias> <key_password> <string_to_encrypt>
         Output will be the encrypted string.
         Encrypt both client_id, and secret, separately
    -- Copy the encrypted string to dashboard.properties, the relevant client_id and client_secret to the following properties:
            oauth.client.id: <copy the client_id here>
            oauth.client.secret: <copy the client_secret here>
    -- Make sure that dashboard.properties file points correctly to the keyStore file under property called: dashboard.encryption.keyStore
       (see step 3)

===============================================================================

2. Set Default Port
   -- The default port for Tomcat is 8080.  If you are running multiple apps, 
      you may need to change this number.
   -- Open $TOMCAT_HOME/conf/server.xml
   -- Change the port number on the Connector element:
      For example: 

        <Connector port="8080" protocol="HTTP/1.1"
                   connectionTimeout="20000"
                   redirectPort="8443" />

        ==============BECOMES=============
    
        <Connector port="8888" protocol="HTTP/1.1"
                   connectionTimeout="20000"
                   redirectPort="8443" />

===============================================================================

3. Edit dashboard.properties and move the file to the conf directory of tomcat.
    - set dashboard.encryption.keyStore property in dashboard.properties to $YOUR_PATH_TO_KEYSTORE/YOUR_KEY_STORE_NAME.
      For example, we used /opt/tomcat/ecnryption/ciKeyStore.jks but you may want to use
      $JAVA_HOME/ciKeyStore.jks
    - cp dashboard.properties to $TOMCAT_HOME/conf

===============================================================================

4. Set up tomcat JAVA_OPTS  (System Properties) 
   -- Open $TOMCAT_HOME/bin/catalina.sh
   Change JAVA_OPTS in tomcat to the following:

    JAVA_OPTS=" -Xms1024m \
         -Xmx1024m \
         -XX:+CMSClassUnloadingEnabled \
         -XX:+CMSPermGenSweepingEnabled \
         -XX:PermSize=512m \
         -XX:MaxPermSize=512m \
         -Dsli.env=team  \
         -Dsli.encryption.keyStore=/opt/tomcat/encryption/ciKeyStore.jks \
         -Dsli.encryption.properties=/opt/tomcat/encryption/ciEncryption.properties \
         -Dsli.trust.certificates=/opt/tomcat/trust/trustedCertificates \
         -Dsli.conf=/opt/tomcat/conf/dashboard.properties \
         -Dnet.sf.ehcache.pool.sizeof.AgentSizeOf.bypass=true"

    Notes for JAVA_OPTS:
    -- The -Dnet.sf.ehcache.pool.sizeof.AgentSizeOf.bypass=true option is only required for MAC OS X users.
    -- sli.conf should be set to $TOMCAT_HOME/conf/dashboard.properties
    -- sli.trust.certificates should be set to $TOMCAT_HOME/trust/trustedCertificates
    -- sli.encryption.keyStore should be set to $YOUR_PATH_TO_KEYSTORE/YOUR_KEY_STORE_NAME
    -- sli.encryption.properties should be set to $YOUR_PATH_TO_KEYSTORE_PROPS/YOUR_KEY_STORE_PROPS_NAME
