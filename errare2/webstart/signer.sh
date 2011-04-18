#!/bin/sh

#for all jars
for file in *.jar ; do
jarsigner -keystore errare.keys -storepass my3boxB $file http://errare.sourceforge.net/
echo $file jarsigné.
done

