#!/bin/sh

#sign all jars
./signall.sh

cd dist
#send the main jar
scp Errare.jar christophe@cyberchrist.hd.free.fr:/home/christophe/www/errare-webstart/errare2/
echo Errare.jar envoyé.
#send the lib folder
scp -r lib christophe@cyberchrist.hd.free.fr:/home/christophe/www/errare-webstart/errare2/
echo libs envoyées.
