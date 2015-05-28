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
