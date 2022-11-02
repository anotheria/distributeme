#!/bin/bash
export VERSION=2.3.11-SNAPSHOT

CLASSPATH=src/test/resources:target/distributeme-test-$VERSION-jar-with-dependencies.jar
echo CLASSPATH: $CLASSPATH
java -Xmx256M -Xms64M -classpath $CLASSPATH -Dconfigureme.defaultEnvironment=test_interceptors $*
