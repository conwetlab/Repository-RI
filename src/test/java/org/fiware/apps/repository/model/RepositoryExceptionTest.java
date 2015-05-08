/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fiware.apps.repository.model;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.fiware.apps.repository.model.RepositoryException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RepositoryExceptionTest {
    
    private RepositoryException toTest, toTest2;
    
    public RepositoryExceptionTest() {
    }
    
    @Test
    public void RespositoryExceptionTotalTest() {
        Status status = Status.ACCEPTED;
        String message = "message";
        
        toTest = new RepositoryException();
        toTest.setDescription(message);
        toTest.setErrorCode(status.getStatusCode());
        toTest.setReasonPhrase(status.getReasonPhrase());
        
        toTest2 = new RepositoryException(status, message);
        
        assertEquals(toTest.getDescription(), message);
        assertEquals(toTest.getErrorCode(), status.getStatusCode());
        assertEquals(toTest.getReasonPhrase(), status.getReasonPhrase());
        
        assertEquals(toTest.getDescription(), toTest2.getDescription());
        assertEquals(toTest.getDescription(), toTest2.getDescription());
        assertEquals(toTest.getDescription(), toTest2.getDescription());
    }
    
}
