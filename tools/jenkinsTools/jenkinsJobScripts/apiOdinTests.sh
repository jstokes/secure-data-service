#!/bin/bash

# Generate and Ingest Odin data
bundle exec rake FORCE_COLOR=true app_bootstrap_server=ci api_server_url=https://$NODE_NAME.slidev.org apiSuperAssessmentTests TOGGLE_TABLESCANS=true

EXITCODE=$?

mongo --eval "db.adminCommand( { setParameter: 1, notablescan: false } )"

exit $EXITCODE





