/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.fiware.apps.repository.model;

import java.util.Date;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import static org.junit.Assert.*;
import org.junit.Test;

public class ResourceTest {

    private Resource toTest;

    public ResourceTest() {
    }

    @Test
    public void ResourceTotalTest()
    {
        ResourceCollection resourceCollection = new ResourceCollection();
        byte bytes[] = "bytes".getBytes();
        String fileName = "fileName";
        String mimeName = "mimeName";
        String url = "url";
        String name = "name";
        Date date = new Date();
        Date date2 = new Date();
        String creator = "creator";
        String id = "id";


        toTest = new Resource();

        toTest.setCollection(resourceCollection);
        toTest.setContent(bytes);
        toTest.setContentFileName(fileName);
        toTest.setContentMimeType(mimeName);
        toTest.setContentUrl(url);
        toTest.setName(name);

        toTest.setCreationDate(date);
        toTest.setCreator(creator);
        toTest.setId(id);
        toTest.setModificationDate(date2);

        assertEquals(toTest.getCollection(), resourceCollection);
        assertEquals(toTest.getContent(), bytes);
        assertEquals(toTest.getContentFileName(), fileName);
        assertEquals(toTest.getContentMimeType(), mimeName);
        assertEquals(toTest.getContentUrl(), url);
        assertEquals(toTest.getName(), name);

        assertEquals(toTest.getCreationDate(), date);
        assertEquals(toTest.getCreator(), creator);
        assertEquals(toTest.getId(), id);
        assertEquals(toTest.getModificationDate(), date2);

    }
}
