#!/bin/bash

ant jar

SYS=`uname -s`
if [ $SYS = Linux ]; then
	./copierlibserrarelinux.sh
else
	if [ $SYS = "Darwin" ]; then
		./copierlibserraremac.sh
	else
		echo "OS not supported"
		exit 1
	fi
fi