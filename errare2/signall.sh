#!/bin/sh
echo enter jar key storepass:
read storep
echo enter jar key keypass:
read password
cd dist
#jarsign the main jar
        jarsigner -keystore ../errare.keys -storepass $storep -keypass $password Errare.jar http://errare.sourceforge.net/
echo Errare.jar jarsigné.

cd lib
#jarsign all jar libraries

for file in *.jar ; do
 	jarsigner -keystore ../../errare.keys -storepass $storep -keypass $password $file http://errare.sourceforge.net/
	echo $file jarsigné.
done


