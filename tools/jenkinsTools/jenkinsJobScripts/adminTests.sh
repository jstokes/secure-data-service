#!/bin/bash

noTableScanAndCleanTomcat

if [ -d sli/config/applications ]; then cp sli/config/applications/*.json /opt/tomcat/conf/ ; fi

resetDatabases

profileSwapAndPropGen

startSearchIndexer

adminUnitTests

deployAdmin

processApps $APPSTODEPLOY

Xvfb :6 -screen 0 1024x768x24 >/dev/null 2>&1 &
export DISPLAY=:6.0
cd $WORKSPACE/sli/acceptance-tests/
export LANG=en_US.UTF-8
bundle install --deployment
bundle exec rake FORCE_COLOR=true admintools_server_url=https://${NODE_NAME}.slidev.org:2001 ldap_hostname=ldap.slidev.org ldap_port=389 ldap_use_ssl=false ldap_base=ou=DevTest,dc=slidev,dc=org adminToolsTests TOGGLE_TABLESCANS=1

EXITCODE=$?

mongo --eval "db.adminCommand( { setParameter: 1, notablescan: false } )"

exit $EXITCODE
