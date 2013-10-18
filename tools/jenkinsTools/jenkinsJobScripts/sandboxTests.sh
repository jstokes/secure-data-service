#!/bin/bash

noTableScanAndCleanTomcat

resetDatabases

profileSwapAndPropGenSB

startSearchIndexer

unDeployAdmin

profileSwap

deployAdminSB

deployDatabrowser

processApps $APPSTODEPLOY

Xvfb :6 -screen 0 1024x768x24 >/dev/null 2>&1 &
export DISPLAY=:6.0
cd $WORKSPACE/sli/acceptance-tests/
export LANG=en_US.UTF-8
bundle install --deployment
bundle exec rake FORCE_COLOR=true databrowser_server_url=https://${NODE_NAME}.slidev.org:2000 api_server_url=https://$NODE_NAME.slidev.org api_ssl_server_url=https://$NODE_NAME.slidev.org:8443 admintools_server_url=https://${NODE_NAME}.slidev.org:2001 ldap_base=ou=${NODE_NAME},dc=slidev,dc=org sampleApp_server_address=https://$NODE_NAME.slidev.org/ sandboxTests TOGGLE_TABLESCANS=1

EXITCODE=$?

mongo --eval "db.adminCommand( { setParameter: 1, notablescan: false } )"
unDeployAdmin

exit $EXITCODE
