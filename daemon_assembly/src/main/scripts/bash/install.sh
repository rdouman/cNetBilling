export cNetBillingHome=/usr/sbin/cNetBillingServer
export JAVA_HOME=/opt/java/

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

unzip -o -d /usr/sbin *.zip
chmod -R 755 $cNetBillingHome

