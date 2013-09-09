export cNetBillingHome=/sbin/cNetBilling
export JAVA_HOME=/opt/jdk1.7.0_02
$JAVA_HOME/bin/java -cp $cNetBillingHome/third_party/db-derby-10.8.2.2-bin/lib/derbytools.jar:$cNetBillingHome/third_party/db-derby-10.8.2.2-bin/lib/derby.jar:$cNetBillingHome/third_party/db-derby-10.8.2.2-bin/lib/derbyclient.jar org.apache.derby.tools.ij 

#connect 'jdbc:derby://localhost:1527/cNetBilling;create=true';

#-java -Dij.protocol=jdbc:derby: -Dij.database=sample derbyrun.jar
