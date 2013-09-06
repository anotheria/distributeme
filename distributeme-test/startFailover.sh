#!/bin/bash
export VERSION=1.2.1-SNAPSHOT

CLASSPATH=test/appdata:target/distributeme-test-$VERSION-jar-with-dependencies.jar
echo CLASSPATH: $CLASSPATH
java -Xmx256M -Xms64M -classpath $CLASSPATH -DdimeRegistrationNameProvider=org.distributeme.core.failing.Failover -Dmsk.config.prefix=dev-  -Dconfigureme.defaultEnvironment=dev $*
