curlStop()
{
  APP=$1
  curl "http://tomcat:s3cret@localhost:8080/manager/text/stop?path=/$APP"
}

curlUndeploy()
{
  APP=$1
  curl "http://tomcat:s3cret@localhost:8080/manager/text/undeploy?path=/$APP"
}

curlDeploy()
{
  APP=$1
  APPFILEPATH=$2
  echo "Deploy app $APP to path $APPFILEPATH"
  curl "http://tomcat:s3cret@localhost:8080/manager/text/deploy?path=/$APP&war=file:$APPFILEPATH"
}

processApps()
{
  apps=$@
  echo "apps are $apps"
  for var in $apps
  do
    echo $var
    curlStop $var
    curlUndeploy $var
    curlDeploy $var ${deployHash[$var]}
  done
}

profileSwapAndPropGen()
{
  cd $WORKSPACE/sli
  sh profile_swap.sh $NODE_NAME
  cd config/scripts
  ruby webapp-provision.rb ../config.in/canonical_config.yml team /opt/tomcat/conf/sli.properties
  cp $WORKSPACE/sli/data-access/dal/keyStore/ci* /opt/tomcat/encryption/ 
  cp $WORKSPACE/sli/common/common-encrypt/trust/* /opt/tomcat/trust/
}

resetDatabases()
{
  cd $WORKSPACE/config/scripts
  sh resetAllDbs.sh
}

startSearchIndexer()
{
  cd $WORKSPACE/sli/search-indexer
  scripts/local_search_indexer.sh restart target/search_indexer.tar.gz -Dsli.conf=/opt/tomcat/conf/sli.properties -Dsli.encryption.keyStore=/opt/tomcat/encryption/ciKeyStore.jks -Dlock.dir=data/

}