#!/bin/sh

# début du récapitulatif des variables
echo '------------------------------------------------------------------------'

# constantes
appserv_home=$(readlink -f $(dirname ${0})'/..')
echo 'AppServ home : '${appserv_home}

# paramètre 1 : nom du serveur
server=default
if [ -n "$1" ]; then
	server=$1
fi
echo 'Server : '${server}

# calcul du chemin du serveur
server_base=server/${server}
echo 'Server path : '${server_base}

# fin du récapitulatif des variables
echo '------------------------------------------------------------------------'

# démarrage du serveur
cd ${appserv_home}
java -jar appserv-server.jar org.dz.appserv.ApplicationServer ${server_base}
