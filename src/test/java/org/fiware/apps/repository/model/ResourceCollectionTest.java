/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.fiware.apps.repository.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResourceCollectionTest {
    
    private ResourceCollection toTest;
    
    public ResourceCollectionTest() {
    }
    
    @Test
    public void ResourceCollectionTotalTest() {
        ResourceCollection collectionAux = new ResourceCollection();
        Resource resourceAux = new Resource();
        Date date1 = new Date();
        Date date2 = new Date();
        String creator = "creator";
        String id = "id";
        
        List collections = new LinkedList();
        collections.add(collectionAux);
        List resources = new LinkedList();
        resources.add(resourceAux);
        
        toTest = new ResourceCollection();
        
        toTest.setCollections(collections);
        toTest.setResources(resources);
        
        assertEquals(toTest.getCollections(), collections);
        assertEquals(toTest.getResources(), resources);
        
        toTest.setCreationDate(date1);
        toTest.setCreator(creator);
        toTest.setId(id);
        toTest.setModificationDate(date2);
        
        assertEquals(toTest.getCreationDate(), date1);
        assertEquals(toTest.getCreator(), creator);
        assertEquals(toTest.getId(), id);
        assertEquals(toTest.getModificationDate(), date2);
        
    }
}
