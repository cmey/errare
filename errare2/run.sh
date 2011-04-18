#!/bin/bash

cd dist
java -ea -jar -Djava.library.path=lib Errare.jar $1
