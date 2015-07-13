#!/bin/bash
VIRTUSER="dba"
VIRTPASS="dba"
VIRTPWD=$PWD

sleep 5

#Backup VirtuosoDB
./virtuoso7/bin/isql -U $VIRTUSER -P $VIRTPASS <<ScriptDelimit
	backup_context_clear(); 
	checkpoint; 
	backup_online('BACKUP_INTEGRATION_TEST#',150);
	exit;
ScriptDelimit

./virtuoso7/bin/isql -U $VIRTUSER -P $VIRTPASS <<ScriptDelimit
	DELETE FROM DB.DBA.RDF_QUAD;
	exit;
ScriptDelimit

exit 0

