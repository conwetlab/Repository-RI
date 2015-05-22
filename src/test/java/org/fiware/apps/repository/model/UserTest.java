/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fiware.apps.repository.model;

import org.fiware.apps.repository.model.User;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    public UserTest() {
    }

    private User toTest;

    @Test
    public void UserTotalTest() {
        String username = "userName";
        String newUsername = "newUserName";
        String displayName = "displayName";
        String email = "email";
        String password = "password";
        String token = "token";

        toTest = new User(username);

        assertEquals(toTest.getUserName(), username);

        toTest.setUserName(newUsername);
        toTest.setPassword(password);
        toTest.setDisplayName(displayName);
        toTest.setEmail(email);
        toTest.setToken(token);

        assertEquals(toTest.getUserName(), newUsername);
        assertEquals(toTest.getPassword(), password);
        assertEquals(toTest.getDisplayName(), displayName);
        assertEquals(toTest.getEmail(), email);
        assertEquals(toTest.getToken(), token);
    }
}
