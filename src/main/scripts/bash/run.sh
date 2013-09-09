export cNetBillingHome=/sbin/cNetBilling
export JAVA_HOME=/opt/jdk1.7.0_02/

$JAVA_HOME/bin/java -Dlogback.configurationFile=$cNetBillingHome/config/logging/logback.xml -Djava.library.path=resources -jar $cNetBillingHome/cNetBilling.jar
