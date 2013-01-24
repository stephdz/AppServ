#!/bin/sh

# début du récapitulatif des variables
echo '------------------------------------------------------------------------'

# constantes
appserv_home=$(readlink -f $(dirname ${0})'/..')
echo 'AppServ home : '${appserv_home}

# fin du récapitulatif des variables
echo '------------------------------------------------------------------------'

# démarrage du serveur
cd ${appserv_home}
java -jar appserv-test-client.jar org.dz.appserv.client.EJBTest
