#!/bin/bash

noTableScanAndCleanTomcat

resetDatabases

profileSwapAndPropGen

startSearchIndexer

processApps $APPSTODEPLOY

# Test multiple contextual roles capabilities.
Xvfb :6 -screen 0 1024x768x24 >/dev/null 2>&1 &
export DISPLAY=:6.0
cd $WORKSPACE/sli/acceptance-tests
export LANG=en_US.UTF-8
bundle install --deployment
bundle exec rake FORCE_COLOR=true app_bootstrap_server=ci api_server_url=https://$NODE_NAME.slidev.org api_server_url=https://$NODE_NAME.slidev.org api_ssl_server_url=https://$NODE_NAME.slidev.org:8443 ci_idp_redirect_url=https://$NODE_NAME.slidev.org/simple-idp?realm=IL-Daybreak ldap_hostname=ldap.slidev.org ldap_port=389 ldap_use_ssl=false ldap_base=ou=LocalNew,ou=DevTest,dc=slidev,dc=org apiContextualRolesTests TOGGLE_TABLESCANS=true

EXITCODE=$?

mongo --eval "db.adminCommand( { setParameter: 1, notablescan: false } )"

exit $EXITCODE





