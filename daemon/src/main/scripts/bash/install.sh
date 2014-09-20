export cNetBillingHome=/usr/sbin/cNetBilling
export JAVA_HOME=/opt/java/

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

unzip -o -d /usr/sbin *.zip
chmod -R 755 $cNetBillingHome
$JAVA_HOME/bin/java -cp $classpath:$cNetBillingHome/lib/derbytools.jar org.apache.derby.tools.ij my_file.sql

