#!/bin/sh

cd dist
#jarsign the main jar
	jarsigner -keystore ../errare.keys -storepass my3boxB -keypass my3boxBB Errare.jar http://errare.sourceforge.net/
echo Errare.jar jarsigné.
