export cNetBillingHome=/bin/cNetBilling
export JAVA_HOME=/opt/jdk1.7.0_02/

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

unzip -o -d /sbin *.zip

cd $cNetBillingHome/resources
unzip -o -j *jnet*.jar native/linux/amd64/*.so


$JAVA_HOME/bin/java -jar $cNetBillingHome/third_party/db-derby-10.8.2.2-bin/lib/derbyrun.jar server start &
 
$JAVA_HOME/bin/java -cp $cNetBillingHome/third_party/db-derby-10.8.2.2-bin/lib/derbyclient.jar:$cNetBillingHome/third_party/db-derby-10.8.2.2-bin/lib/derbytools.jar org.apache.derby.tools.ij $cNetBillingHome/scripts/sql/create.sql

