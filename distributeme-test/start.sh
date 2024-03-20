#!/bin/bash
export VERSION=2.5.4-SNAPSHOT

CLASSPATH=src/test/resources:target/distributeme-test-$VERSION-jar-with-dependencies.jar
echo CLASSPATH: $CLASSPATH
java -Xmx256M -Xms64M -classpath $CLASSPATH -Djava.rmi.server.logCalls -Dconfigureme.defaultEnvironment=test $*
