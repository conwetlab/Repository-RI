/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.org.fiware.apps.repository.model;

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
        String password = "password";
        
        toTest = new User(username);
        
        assertEquals(toTest.getUsername(), username);
        
        toTest.setUsername(newUsername);
        toTest.setPassword(password);
        
        assertEquals(toTest.getUsername(), newUsername);
        assertEquals(toTest.getPassword(), password);
    }
}
