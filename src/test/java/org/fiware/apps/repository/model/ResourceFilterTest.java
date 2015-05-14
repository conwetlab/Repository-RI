/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.fiware.apps.repository.model;

import org.fiware.apps.repository.model.ResourceFilter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jortiz
 */
public class ResourceFilterTest {
    
    private ResourceFilter toTest;
    
    public ResourceFilterTest() {
    }
    
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void ResourceFilterTest() {
        int offset = 1;
        int limit = 2;
        String filter = "filter,filter1:echo,filter2";
        
        toTest = new ResourceFilter(offset, limit, filter);
        
        assertEquals(toTest.getOffset(), offset);
        assertEquals(toTest.getLimit(), limit);
        assertEquals(toTest.getFilter(), filter);
        
        toTest.parseFilter();
    }
    
    @Test
    public void ResourceFilterNullTest() {
        int offset = 1;
        int offset2 = 3;
        int limit = 2;
        int limit2 = 4;
        
        toTest = new ResourceFilter(offset, limit, null);
        
        assertEquals(toTest.getOffset(), offset);
        assertEquals(toTest.getLimit(), limit);
        assertEquals(toTest.getFilter(), "");
        
        toTest.setFilter("CASA");
        toTest.setLimit(limit2);
        toTest.setOffset(offset2);
        
        assertEquals(toTest.getOffset(), offset2);
        assertEquals(toTest.getLimit(), limit2);
        assertEquals(toTest.getFilter(), "CASA");
        
        toTest.parseFilter();
    }
}
