#!/bin/bash

source utils.sh

mongo --eval "db.adminCommand( { setParameter: 1, notablescan: false } )"
/usr/sbin/cleanup_tomcat

cd sli
sh profile_swap.sh $NODE_NAME
cd config/scripts
sh resetAllDbs.sh
ruby webapp-provision.rb ../config.in/canonical_config.yml team /opt/tomcat/conf/sli.properties
cp $WORKSPACE/sli/data-access/dal/keyStore/ci* /opt/tomcat/encryption/ 
cp $WORKSPACE/sli/common/common-encrypt/trust/* /opt/tomcat/trust/
cd ../../search-indexer
scripts/local_search_indexer.sh restart target/search_indexer.tar.gz -Dsli.conf=/opt/tomcat/conf/sli.properties -Dsli.encryption.keyStore=/opt/tomcat/encryption/ciKeyStore.jks -Dlock.dir=data/

#curl http://tomcat:s3cret@localhost:8080/manager/text/stop?path=/api
curlStop api
#curl http://tomcat:s3cret@localhost:8080/manager/text/undeploy?path=/api
curlUndeploy api
#curl "http://tomcat:s3cret@localhost:8080/manager/text/deploy?path=/api&war=file:$WORKSPACE/sli/api/target/api.war"
curlDeploy api "$WORKSPACE/sli/api/target/api.war"

cd sli/acceptance-tests
export LANG=en_US.UTF-8
bundle install --deployment
bundle exec rake FORCE_COLOR=true api_server_url=https://$NODE_NAME.slidev.org apiAndSecurityTests TOGGLE_TABLESCANS=true

mongo --eval "db.adminCommand( { setParameter: 1, notablescan: false } )"


