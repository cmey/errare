#!/bin/sh

#sign all jars
./sign.sh

cd dist
#send the main jar
scp Errare.jar cyberchrist.hd.free.fr:/home/christophe/www/errare-webstart/errare2/
echo Errare.jar envoyé.
