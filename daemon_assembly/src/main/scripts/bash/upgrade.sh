export cNetBillingHome=/sbin/cNetBilling
export JAVA_HOME=/opt/jdk1.7.0_02/

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

unzip -o -d /sbin *.zip

cd $cNetBillingHome/lib
unzip -o -j *jnet*.jar ../resources/native/linux/amd64/*.so

