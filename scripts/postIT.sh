#!/bin/bash
VIRTUSER="dba"
VIRTPASS="dba"
VIRTPWD=$PWD

# Stop VirtuosoDB
./virtuoso7/bin/isql -U $VIRTUSER -P $VIRTPASS -K

sleep 2

cd ./virtuoso7/var/lib/virtuoso/db/
rm ./virtuoso.db

$VIRTPWD/virtuoso7/bin/virtuoso-t +configfile ./virtuoso.ini +restore-backup BACKUP_INTEGRATION_TEST# +wait

rm ./BACKUP_INTEGRATION_TEST*

exit 0

