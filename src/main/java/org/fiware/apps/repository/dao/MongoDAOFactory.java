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

import java.util.Properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PreDestroy;

import org.fiware.apps.repository.dao.impl.MongoCollectionDAO;
import org.fiware.apps.repository.dao.impl.MongoResourceDAO;
import org.fiware.apps.repository.dao.impl.MongoUserDAO;
import org.fiware.apps.repository.settings.DefaultProperties;
import org.springframework.beans.factory.annotation.Autowired;


@Component
@Scope("singleton")
public class MongoDAOFactory {

    @Autowired
    private VirtuosoDAOFactory virtuosoDAOFactory;

    private MongoCollectionDAO collectionDao;
    private MongoResourceDAO resourceDao;
    private MongoUserDAO userDao;

    private MongoDatabase db;
    private MongoClient client;

    public synchronized void createConnection (Properties properties) {

        // Build connection and DAOs if not created
        if (this.db == null) {
            this.client = new MongoClient(properties.getProperty(DefaultProperties.MONGO_HOST.getPropertyName()),
                Integer.parseInt(properties.getProperty(DefaultProperties.MONGO_PORT.getPropertyName())));

            this.db = client.getDatabase(properties.getProperty(DefaultProperties.MONGO_DB.getPropertyName()));

            this.collectionDao = new MongoCollectionDAO(this.db, this.virtuosoDAOFactory.getVirtuosoResourceDAO(properties));
            this.resourceDao = new MongoResourceDAO(this.db);
            this.userDao = new MongoUserDAO(this.db);
        }
    }

    @PreDestroy
    public void closeConnection() {
        client.close();
    }

    public ResourceDAO getResourceDAO() {
        return this.resourceDao;
    }

    public CollectionDAO getCollectionDAO() {
        return this.collectionDao;
    }

    public UserDAO getUserDao() {
        return this.userDao;
    }
}
