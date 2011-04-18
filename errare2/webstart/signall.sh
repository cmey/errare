#!/bin/sh

cd dist
#jarsign the main jar
	jarsigner -keystore ../errare.keys -storepass my3boxB -keypass my3boxBB Errare.jar http://errare.sourceforge.net/
echo Errare.jar jarsigné.

cd lib
#jarsign all jar libraries

for file in *.jar ; do
 	jarsigner -keystore ../../errare.keys -storepass my3boxB -keypass my3boxBB $file http://errare.sourceforge.net/
	echo $file jarsigné.
done


