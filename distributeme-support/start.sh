#!/bin/bash
lib_path=lib
dist_path=dist
for file in $(ls $lib_path); do
 lib=$lib:$lib_path/$file
done

echo lib: $lib
CLASSPATH=target/classes:etc/appdata:$lib
echo CLASSPATH: $CLASSPATH
java -Xmx256M -Xms64M -classpath $CLASSPATH -Dmsk.config.prefix=dev-  -Dconfigureme.defaultEnvironment=dev $*
