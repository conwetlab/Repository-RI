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
* Neither the name of UPM nor the
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

package org.fiware.apps.repository.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.NotFoundException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;


/**
 *
 * @author jortiz
 */
public class MongoUserDAOTest {

    private @Mock MongoDatabase db;
    private @Mock MongoCollection mongoCollection;
    private @InjectMocks MongoUserDAO toTest;

    public MongoUserDAOTest() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {

    }

    @Before
    public void setUp() {
        toTest = new MongoUserDAO(db, mongoCollection);
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    private void mockFind(BasicDBObject userObj, Boolean toReturn, Boolean ... restReturn) {
        FindIterable it = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);

        when(it.iterator()).thenReturn(cursor);
        when(mongoCollection.find(any(BasicDBObject.class))).thenReturn(it);

        when(cursor.hasNext()).thenReturn(toReturn, restReturn);
        when(cursor.next()).thenReturn(userObj);
    }

    @Test
    public void getUserTest() {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        User returned = null;

        User user = createDummyUser(username, displayName, email, password, token);
        this.mockFind(
                this.createDummyDBObject(username, displayName, email, password, token),
                true, false);

        try {
            returned = toTest.getUser(username);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertEquals(user.getUserName(), returned.getUserName());
        assertEquals(user.getDisplayName(), returned.getDisplayName());
        assertEquals(user.getEmail(), returned.getEmail());
        assertEquals(user.getPassword(), returned.getPassword());
        assertEquals(user.getToken(), returned.getToken());
    }

    @Test
    public void getUserNullTest() {
        String username = "userName";
        User returned = null;

        this.mockFind(null, false);

        try {
            returned = toTest.getUser(username);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertNull(returned);
    }

    @Test (expected = DatasourceException.class)
    public void getUserExceptionTest() throws DatasourceException {
        String username = "userName";

        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(Exception.class);
        toTest.getUser(username);

        //fail();
    }

    @Test
    public void createUserTest() throws DatasourceException, SameIdException {
        String username = "userName";
        this.mockFind(null, false);
        
        doNothing().when(mongoCollection).insertOne(any(BasicDBObject.class));
        toTest.createUser(username);
    }

    @Test (expected = DatasourceException.class)
    public void createUserDatasourceExceptionTest() throws DatasourceException, SameIdException {
        String username = "userName";

        this.mockFind(null, false);
        doThrow(DatasourceException.class).when(mongoCollection).insertOne(any(BasicDBObject.class));

        toTest.createUser(username);
    }

    @Test (expected = SameIdException.class)
    public void createUserSameIdExceptionTest() throws DatasourceException, SameIdException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";

        BasicDBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        this.mockFind(userObj, true, false);

        toTest.createUser(username);
    }

    @Test
    public void isUserTest() throws DatasourceException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";

        BasicDBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        this.mockFind(userObj, true, false);

        boolean returned = toTest.isUser(username);

        assertTrue(returned);
    }

    @Test
    public void isNotUserTest() throws DatasourceException {
        String username = "name user";
        this.mockFind(null, false);

        boolean returned = toTest.isUser(username);

        assertFalse(returned);
    }

    @Test (expected = DatasourceException.class)
    public void isUserDatasourceExceptionTest() throws DatasourceException {
        String username = "name user";

        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(Exception.class);

        toTest.isUser(username);
    }

    @Test
    public void updateUserTest() {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        String displayName2 = "displayname";
        String email2 = "email";
        String password2 = "password";
        String token2 = "token";

        BasicDBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        this.mockFind(userObj, true, false, true, false);

        User user = createDummyUser(username, displayName2, email2, password2, token2);

        try {
            toTest.updateUser(user);
        } catch (DatasourceException | NotFoundException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        verify(mongoCollection).updateOne(eq(userObj), isA(BasicDBObject.class));
    }

    @Test (expected = DatasourceException.class)
    public void updateUserDatasourceException1Test() throws DatasourceException, NotFoundException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";

        User user = createDummyUser(username, displayName, email, password, token);

        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(Exception.class);

        toTest.updateUser(user);

        //fail()
    }

    @Test (expected = DatasourceException.class)
    public void updateUserDatasourceException2Test() throws DatasourceException, NotFoundException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        String displayName2 = "displayname";
        String email2 = "email";
        String password2 = "password";
        String token2 = "token";

        BasicDBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        this.mockFind(userObj, true, false, true, false);
        User user = createDummyUser(username, displayName2, email2, password2, token2);
        doThrow(Exception.class).when(mongoCollection).updateOne(any(BasicDBObject.class), any(BasicDBObject.class));

        toTest.updateUser(user);

        //fail()
    }

    @Test (expected = NotFoundException.class)
    public void updateUserNotFoundExceptionTest() throws DatasourceException, NotFoundException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";

        User user = createDummyUser(username, displayName, email, password, token);
        this.mockFind(null, false);

        toTest.updateUser(user);
    }

    @Test
    public void deleteUserTest() {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";

        BasicDBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        this.mockFind(userObj, true, false);

        try {
            toTest.deleteUser(username);
        } catch (DatasourceException | NotFoundException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        verify(mongoCollection).deleteOne(userObj);
    }

    @Test (expected = NotFoundException.class)
    public void deleteUserNotFoundExceptionTest() throws DatasourceException, NotFoundException {
        String username = "name user";
        this.mockFind(null, false);

        toTest.deleteUser(username);
    }

    @Test (expected = DatasourceException.class)
    public void deleteUserDatasourceException1Test() throws DatasourceException, NotFoundException {
        String username = "name user";
        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(Exception.class);
        toTest.deleteUser(username);
    }

    @Test (expected = DatasourceException.class)
    public void deleteUserDatasourceException2Test() throws DatasourceException, NotFoundException {

        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";

        BasicDBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        this.mockFind(userObj, true, false);
        doThrow(Exception.class).when(mongoCollection).deleteOne(eq(userObj));
        toTest.deleteUser(username);
    }

    private BasicDBObject createDummyDBObject(String username, String displayName,
            String email, String password, String token) {

        BasicDBObject userObj = new BasicDBObject();
        userObj.put("_id", "507f1f77bcf86cd799439011");
        userObj.put("userName", username);
        userObj.put("displayName", displayName);
        userObj.put("email", email);
        userObj.put("password", password);
        userObj.put("token", token);

        return userObj;
    }

    private User createDummyUser(String username, String displayName,
            String email, String password, String token) {

        User user = new User(username);
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setPassword(password);
        user.setToken(token);

        return user;
    }
}
