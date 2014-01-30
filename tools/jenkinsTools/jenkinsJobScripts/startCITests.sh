#!/bin/bash
source /usr/local/rvm/environments/default
hostname=`hostname -s`
WORKSPACE=/data/jenkins/workspace/Full_Build_Master/
noTableScan()
{
    mongo --eval "db.adminCommand( { setParameter: 1, notablescan: false } )"
    echo "Table Scanning Disabled"
}
cleanTomcat()
{
    sudo /etc/init.d/tomcat stop
    for i in `ls /opt/apache-tomcat-7.0.47/webapps |grep -v "manager" |grep -v "ROOT" |grep -v "docs"`; do
       sudo rm -r -f /opt/apache-tomcat-7.0.47/webapps/$i
    done
    sudo rm -r -f /opt/apache-tomcat-7.0.47/temp/*
    sudo rm -r -f /opt/apache-tomcat-7.0.47/Catalina/localhost/*
    sudo /etc/init.d/tomcat start
    echo "Cleaned up tomcat"
}
resetDatabases()
{
  cd $WORKSPACE/sli/config/scripts
  sh resetAllDbs.sh
  echo "Dropped Databases"
}
#this function needs to be refactored to actually work with our URL schema
generateFixtureData()
{
    echo "Altering fixture data for applications to match..."
    sed -i "s/https:\/\/ci.slidev.org/https:\/\/$hostname.dev.inbloom.org/g" acceptance-tests/test/data/application_fixture.json
    sed -i "s/https:\/\/ci.slidev.org/https:\/\/$hostname.dev.inbloom.org/g" acceptance-tests/test/data/realm_fixture.json
    sed -i "s/http:\/\/local.slidev.org:8082/https:\/\/$hostname.dev.inbloom.org/g" acceptance-tests/test/data/realm_fixture.json
    sed -i "s/https:\/\/ci.slidev.org/https:\/\/$hostname.dev.inbloom.org/g" acceptance-tests/test/data/application_denial_fixture.json
}
adminUnitTests()
{
  echo "Executing admin unit tests"
  cd $WORKSPACE/sli/admin-tools/admin-rails
  bundle install --path $WORKSPACE/../vendors/
  bundle exec rake ci:setup:testunit test
  code=$?
  if [ "$code" != "0" ]; then
    exit $code
  fi
  echo "Finished running admin unit tests."
}

noTableScan
cleanTomcat
resetDatabases
adminUnitTests
