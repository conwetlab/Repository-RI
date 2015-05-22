/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeTLab, Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of the SAP AG nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SAP AG BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.fiware.apps.repository.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.ObjectId;
import org.fiware.apps.repository.dao.MongoDAOFactory;
import org.fiware.apps.repository.dao.UserDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.NotFoundException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.User;

public class MongoUserDAO implements UserDAO {

    public static final String MONGO_USERS_NAME = "Users";

    private DB db;
    private DBCollection mongoCollection;

    public MongoUserDAO() {
        db = MongoDAOFactory.createConnection();
        mongoCollection = db.getCollection(MONGO_USERS_NAME);
    }

    public MongoUserDAO(DB db, DBCollection mongoCollection) {
        this.db = db;
        this.mongoCollection = mongoCollection;
    }

    @Override
    public User getUser(String username) throws DatasourceException {
        DBObject userObj;
        User user;

        db.requestStart();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userName", username);
            userObj = mongoCollection.findOne(query);
        } catch (Exception ex) {
            db.requestDone();
            throw new DatasourceException(ex.getMessage(), User.class);
        }

        if (userObj == null) {
            db.requestDone();
            return null;
        }

        user = new User(username);
        user.setDisplayName(userObj.get("displayName").toString());
        user.setEmail(userObj.get("email").toString());
        user.setPassword(userObj.get("password").toString());
        user.setToken(userObj.get("token").toString());

        db.requestDone();
        return user;
    }

    @Override
    public void createUser(String username) throws DatasourceException, SameIdException {

        if (!isUser(username)) {

            db.requestStart();

            try {
                BasicDBObject newUserObj = new BasicDBObject();
                newUserObj.put("userName", username);
                newUserObj.put("password", "");
                newUserObj.put("token", "");
                newUserObj.put("displayName", "");
                newUserObj.put("email", "");

                mongoCollection.insert(newUserObj);
            } catch (Exception ex) {
                db.requestDone();
                throw new DatasourceException(ex.getMessage(), User.class);
            }

            db.requestDone();
        }
        else {
            throw new SameIdException(username, User.class);
        }
    }

    @Override
    public boolean isUser(String username) throws DatasourceException{
        User user = getUser(username);

        if (user == null)
            return false;
        else
            return true;
    }

    @Override
    public void updateUser(User user) throws DatasourceException, NotFoundException {
        DBObject userObj;

        db.requestStart();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userName", user.getUserName());
            userObj = mongoCollection.findOne(query);
        } catch (Exception ex) {
            db.requestDone();
            throw new DatasourceException(ex.getMessage(), User.class);
        }

        if (userObj == null) {
            db.requestDone();
            throw new NotFoundException(user.getUserName(), User.class);
        }

        userObj.put("displayName", user.getDisplayName());
        userObj.put("email", user.getEmail());
        userObj.put("password", user.getPassword());
        userObj.put("token", user.getToken());

        try {
            mongoCollection.update(new BasicDBObject().append("_id", new ObjectId(userObj.get("_id").toString())), userObj, false,false);
        } catch (Exception ex) {
            db.requestDone();
            throw new DatasourceException(ex.getMessage(), User.class);
        }

        db.requestDone();
    }

    @Override
    public void deleteUser(String username) throws DatasourceException, NotFoundException {
        DBObject userObj;

        db.requestStart();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userName", username);
            userObj = mongoCollection.findOne(query);
        } catch (Exception ex) {
            db.requestDone();
            throw new DatasourceException(ex.getMessage(), User.class);
        }

        if (userObj == null) {
            db.requestDone();
            throw new NotFoundException(username, User.class);
        }

        try {
            mongoCollection.remove(userObj);
        } catch (Exception ex) {
            db.requestDone();
            throw new DatasourceException(ex.getMessage(), User.class);
        }

        db.requestDone();
    }
}
