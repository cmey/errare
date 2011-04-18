#!/bin/bash

./copierlibserrare.sh
cd dist
java -jar -Djava.library.path=lib Errare.jar
