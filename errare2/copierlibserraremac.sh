#!/bin/bash

#JOGL runtime libs
cp dist-macosx-universal/* dist/lib/
cp dist-macosx-universal/lib/* dist/lib/

#Bluetooth runtime libs
cp libs/libavetanaBT.so dist/lib/
