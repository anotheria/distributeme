#!/bin/bash
export VERSION=1.2.1-SNAPSHOT

CLASSPATH=target/distributeme-test-$VERSION-jar-with-dependencies.jar
echo CLASSPATH: $CLASSPATH
java -Xmx256M -Xms64M -classpath $CLASSPATH -Dconfigureme.defaultEnvironment=test $*
