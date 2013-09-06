#!/bin/bash
export VERSION=1.0.7-SNAPSHOT

CLASSPATH=test/appdata:target/distributeme-agents-$VERSION-jar-with-dependencies.jar
echo CLASSPATH: $CLASSPATH
java -Xmx256M -Xms64M -classpath $CLASSPATH -Dmsk.config.prefix=dev-  -Dconfigureme.defaultEnvironment=dev $*
