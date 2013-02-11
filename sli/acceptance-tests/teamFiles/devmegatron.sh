#!/bin/sh
sh checkoutAndBuild_devmegatron.sh $1
cd /opt/megatron/sli/sli/acceptance-tests/
export LANG=en_US.UTF-8
bundle install --deployment
Xvfb :4 -screen 0 1024x768x24 >/dev/null 2>&1 &
export DISPLAY=:4.0
#smoke
#bundle exec rake FORCE_COLOR=true sampleApp_server_address=http://devmegatron.slidev.org/ dashboard_server_address=http://devmegatron.slidev.org dashboard_api_server_uri=http://devmegatron.slidev.org realm_page_url=http://devmegatron.slidev.org/api/oauth/authorize admintools_server_url=http://devmegatron.slidev.org:3001 api_server_url=http://devmegatron.slidev.org databrowser_server_url=http://devmegatron.slidev.org:3000 ingestion_landing_zone=/opt/ingestion/lz/inbound sif_zis_address_trigger=http://devmegatron.slidev.org:8080/mock-zis/trigger elastic_search_address=http://devmegatron.slidev.org:9200 smokeTests
#integration
#sh /opt/megatron/sli/sli/acceptance-tests/teamFiles/resetEnvironment.sh
#bundle exec rake FORCE_COLOR=true sampleApp_server_address=http://devmegatron.slidev.org/ dashboard_server_address=http://devmegatron.slidev.org dashboard_api_server_uri=http://devmegatron.slidev.org realm_page_url="http://devmegatron.slidev.org/api/oauth/authorize" admintools_server_url=http://devmegatron.slidev.org:3001 api_server_url=http://devmegatron.slidev.org databrowser_server_url=http://devmegatron.slidev.org:3000 ingestion_landing_zone=/opt/ingestion/lz/inbound integrationTests
#ingestion
#sh /opt/megatron/sli/sli/acceptance-tests/teamFiles/resetEnvironment.sh
#bundle exec rake FORCE_COLOR=true ingestion_log_directory=/opt/ingestion/logs ingestion_landing_zone=/opt/ingestion/lz/inbound ingestion_healthcheck_url=http://devmegatron.slidev.org/ingestion-service/healthcheck ingestionTests
#dashboard
#sh /opt/megatron/sli/sli/acceptance-tests/teamFiles/resetEnvironment.sh
bundle exec rake FORCE_COLOR=true sampleApp_server_address=http://devmegatron.slidev.org/ dashboard_server_address=http://devmegatron.slidev.org dashboard_api_server_uri=http://devmegatron.slidev.org realm_page_url="http://devmegatron.slidev.org/api/oauth/authorize" admintools_server_url=http://devmegatron.slidev.org:3001 api_server_url=http://devmegatron.slidev.org databrowser_server_url=http://devmegatron.slidev.org:3000 ingestion_landing_zone=/opt/ingestion/lz/inbound localDashboardTests
#odin
#sh /opt/megatron/sli/sli/acceptance-tests/teamFiles/resetEnvironment.sh
#bundle exec rake FORCE_COLOR=true ingestion_log_directory=/opt/ingestion/logs ingestion_landing_zone=/opt/ingestion/lz/inbound ingestion_healthcheck_url=http://devmegatron.slidev.org/ingestion-service/healthcheck odinTests
#api
#bundle exec rake FORCE_COLOR=true api_server_url=http://devmegatron.slidev.org apiAndSecurityTests
