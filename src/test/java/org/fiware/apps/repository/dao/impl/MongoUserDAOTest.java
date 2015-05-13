/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.fiware.apps.repository.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static org.mockito.Mockito.*;


/**
 *
 * @author jortiz
 */
public class MongoUserDAOTest {
    
    private DB db;
    private DBCollection mongoCollection;
    private MongoUserDAO toTest;
    
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
        db = mock(DB.class);
        mongoCollection = mock(DBCollection.class);
        
        toTest = new MongoUserDAO(db, mongoCollection);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void getUserTest() {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        User returned = null;
        
        DBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        
        User user = createDummyUser(username, displayName, email, password, token);
        
        List list = new LinkedList();
        list.add(userObj);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        
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
        
        verify(db).requestStart();
        verify(db).requestDone();
        
    }
    
    @Test
    public void getUserNullTest() {
        String username = "userName";
        User returned = null;
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        
        try {
            returned = toTest.getUser(username);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertNull(returned);
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test (expected = DatasourceException.class)
    public void getUserExceptionTest() throws DatasourceException {
        String username = "userName";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);
        
        toTest.getUser(username);
        
        //fail();
    }
    
    @Test
    public void createUserTest() throws DatasourceException, SameIdException {
        String username = "userName";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        toTest.createUser(username);
        
        verify(db, times(2)).requestStart();
        verify(db, times(2)).requestDone();
    }
    
    @Test (expected = DatasourceException.class)
    public void createUserDatasourceExceptionTest() throws DatasourceException, SameIdException {
        String username = "userName";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        doThrow(DatasourceException.class).when(mongoCollection).insert(any(DBObject.class));
        
        toTest.createUser(username);
    }
    
    @Test (expected = SameIdException.class)
    public void createUserSameIdExceptionTest() throws DatasourceException, SameIdException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        
        DBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        
        List list = new LinkedList();
        list.add(userObj);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        
        toTest.createUser(username);
        
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test
    public void isUserTest() throws DatasourceException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        
        DBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        
        List list = new LinkedList();
        list.add(userObj);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        
        boolean returned = toTest.isUser(username);
        
        assertTrue(returned);
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test
    public void isNotUserTest() throws DatasourceException {
        String username = "name user";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        boolean returned = toTest.isUser(username);
        
        assertFalse(returned);
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test (expected = DatasourceException.class)
    public void isUserDatasourceExceptionTest() throws DatasourceException {
        String username = "name user";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);
        
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
        
        DBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        
        User user = createDummyUser(username, displayName2, email2, password2, token2);
        
        List list = new LinkedList();
        list.add(userObj);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doNothing().when(mongoCollection).update(any(DBObject.class), any(DBObject.class), eq(false), eq(false));
        
        try {
            toTest.updateUser(user);
        } catch (DatasourceException | NotFoundException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test (expected = DatasourceException.class)
    public void updateUserDatasourceException1Test() throws DatasourceException, NotFoundException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        
        User user = createDummyUser(username, displayName, email, password, token);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);
        
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
        
        DBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        
        User user = createDummyUser(username, displayName2, email2, password2, token2);
        
        List list = new LinkedList();
        list.add(userObj);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doThrow(Exception.class).when(mongoCollection).update(any(DBObject.class), any(DBObject.class), eq(false), eq(false));
        
        toTest.updateUser(user);
        
        //fail()
    }
    
    @Test (expected = NotFoundException.class)
    public void updateUserSameIdExceptionTest() throws DatasourceException, NotFoundException {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        
        User user = createDummyUser(username, displayName, email, password, token);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        toTest.updateUser(user);
        //fail()
    }
    
    @Test
    public void deleteUserTest() {
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        
        DBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        
        List list = new LinkedList();
        list.add(userObj);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doNothing().when(mongoCollection).remove(eq(userObj));
        
        try {
            toTest.deleteUser(username);
        } catch (DatasourceException | NotFoundException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test (expected = NotFoundException.class)
    public void deleteUserNotFoundExceptionTest() throws DatasourceException, NotFoundException {
        String username = "name user";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        toTest.deleteUser(username);
    }
    
    @Test (expected = DatasourceException.class)
    public void deleteUserDatasourceException1Test() throws DatasourceException, NotFoundException {
        String username = "name user";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);
        
        toTest.deleteUser(username);
    }
    
    @Test (expected = DatasourceException.class)
    public void deleteUserDatasourceException2Test() throws DatasourceException, NotFoundException {
        
        String username = "name user";
        String displayName = "display name";
        String email = "e-mail";
        String password = "pass word";
        String token = "to ken";
        
        DBObject userObj = createDummyDBObject(username, displayName, email, password, token);
        
        List list = new LinkedList();
        list.add(userObj);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doThrow(Exception.class).when(mongoCollection).remove(eq(userObj));
        
        toTest.deleteUser(username);
        
    }
    
    private DBObject createDummyDBObject(String username, String displayName,
            String email, String password, String token) {
        
        DBObject userObj = new BasicDBObject();
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
