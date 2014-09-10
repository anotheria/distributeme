#!/bin/bash
export VERSION=2.1.3-SNAPSHOT

CLASSPATH=test/appdata:target/distributeme-test-$VERSION-jar-with-dependencies.jar
echo CLASSPATH: $CLASSPATH
java -Xmx256M -Xms64M -classpath $CLASSPATH -Dconfigureme.defaultEnvironment=test $*
