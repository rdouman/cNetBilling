export cNetBillingHome=/usr/sbin/cNetBillingServer
export JAVA_HOME=/opt/java/

cd $cNetBillingHome

export classpath=`find "lib" -name '*.jar' -printf '%p:' | sed 's/:$//'`
$JAVA_HOME/bin/java -cp $classpath:$cNetBillingHome/cNetBilling.jar -Dlogback.configurationFile=$cNetBillingHome/config/logging/logback.xml -Djava.library.path=$cNetBillingHome/lib/jnetpcap org.communinet.billing.Driver
