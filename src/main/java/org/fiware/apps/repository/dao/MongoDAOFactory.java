/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeT Lab., Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of the UPM nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL UPM BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/
package org.fiware.apps.repository.dao;

import java.net.UnknownHostException;

import org.fiware.apps.repository.dao.impl.MongoCollectionDAO;
import org.fiware.apps.repository.dao.impl.MongoResourceDAO;
import org.fiware.apps.repository.settings.RepositorySettings;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDAOFactory extends DAOFactory {

    static DB db = null;

    public ResourceDAO getResourceDAO() {
        return new MongoResourceDAO();
    }


    public static DB createConnection() {
        if(db !=null){
            return db;
        }

        Mongo m;
        try {
            m = new Mongo(RepositorySettings.getProperty("mongodb.host"),
                    Integer.parseInt(RepositorySettings.getProperty("mongodb.port")));
            db = m.getDB(RepositorySettings.getProperty("mongodb.db"));
        } catch (UnknownHostException | MongoException ex) {
            throw new MongoException(ex.getLocalizedMessage());
        }
        return db;

    }


    @Override
    public CollectionDAO getCollectionDAO() {
        return new MongoCollectionDAO();
    }


}
