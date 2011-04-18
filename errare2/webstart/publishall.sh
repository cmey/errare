#!/bin/sh

#sign all jars
./signall.sh

cd dist
#send the main jar
scp Errare.jar cyberchrist.hd.free.fr:/home/christophe/www/errare-webstart/errare2/
echo Errare.jar envoyé.
#send the lib folder
scp -r lib cyberchrist.hd.free.fr:/home/christophe/www/errare-webstart/errare2/
