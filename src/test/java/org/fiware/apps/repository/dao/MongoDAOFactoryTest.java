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
package org.fiware.apps.repository.dao;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;
import org.fiware.apps.repository.dao.impl.MongoCollectionDAO;
import org.fiware.apps.repository.dao.impl.MongoResourceDAO;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MongoDAOFactory.class, Mongo.class, MongoCollectionDAO.class, MongoResourceDAO.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoDAOFactoryTest {

    MongoDAOFactory toTest;
    @Mock Mongo mongo;
    @Mock DB db;
    @Mock MongoResourceDAO mongoResourceDAO;
    @Mock MongoCollectionDAO mongoCollectionDAO;

    public MongoDAOFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        mongo = mock(Mongo.class);
        db = mock(DB.class);
        PowerMockito.whenNew(Mongo.class).withAnyArguments().thenReturn(mongo);

    }

    @After
    public void tearDown() {
    }

    @Test (expected = MongoException.class)
    public void createConnectionException1Test() {
        try {
            when(mongo.getDB(anyString())).thenThrow(UnknownHostException.class);
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
        MongoDAOFactory.createConnection();

    }

    @Test (expected = MongoException.class)
    public void createConnectionException2Test() {
        try {
            when(mongo.getDB(anyString())).thenThrow(MongoException.class);
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
        MongoDAOFactory.createConnection();
    }

    @Test
    public void createConnection3Test() {
        MongoDAOFactory.createConnection();
        verify(mongo).getDB(anyString());
    }

    @Test
    public void getResourceDAOTest() {
        mongoResourceDAO = mock(MongoResourceDAO.class);
        try {
            PowerMockito.whenNew(MongoResourceDAO.class).withNoArguments().thenReturn(mongoResourceDAO);
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
        toTest = new MongoDAOFactory();
        toTest.getResourceDAO();
    }

    @Test
    public void getCollectionDAOTest() {
        mongoCollectionDAO = mock(MongoCollectionDAO.class);
        try {
            PowerMockito.whenNew(MongoCollectionDAO.class).withNoArguments().thenReturn(mongoCollectionDAO);
        } catch (Exception ex) {
            fail(ex.getLocalizedMessage());
        }
        toTest = new MongoDAOFactory();
        toTest.getCollectionDAO();
    }
}
