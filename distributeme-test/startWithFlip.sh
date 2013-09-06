#!/bin/bash
export VERSION=1.2.1-SNAPSHOT

CLASSPATH=test/appdata:target/distributeme-test-$VERSION-jar-with-dependencies.jar
echo CLASSPATH: $CLASSPATH
echo FLIP: ${flip}
java -Xmx256M -Xms64M -classpath $CLASSPATH -Dconfigureme.defaultEnvironment=test_flip_${flip} $*
