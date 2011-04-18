#!/bin/sh
echo enter jar key storepass:
read storep
echo enter jar key keypass:
read password
cd dist
#jarsign the main jar
	jarsigner -keystore ../errare.keys -storepass $storep -keypass $password Errare.jar http://errare.sourceforge.net/
echo Errare.jar jarsigné.
