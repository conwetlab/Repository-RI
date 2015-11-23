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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.net.UnknownHostException;
import java.util.Properties;
import org.fiware.apps.repository.dao.impl.MongoCollectionDAO;
import org.fiware.apps.repository.dao.impl.MongoResourceDAO;
import org.fiware.apps.repository.dao.impl.MongoUserDAO;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.settings.RepositorySettings;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MongoDAOFactory.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PowerMockIgnore( {"javax.management.*"})
public class MongoDAOFactoryTest {

    private MongoClient mongo;
    private MongoDatabase db;
    private MongoResourceDAO mongoResourceDAO;
    private MongoCollectionDAO mongoCollectionDAO;
    private MongoUserDAO mongoUserDAO;

    @Mock private VirtuosoDAOFactory virtuosoDAOFactory;

    @InjectMocks private MongoDAOFactory toTest;

    private Properties properties;

    public MongoDAOFactoryTest() {
    }

    @Before
    public void setUp() throws Exception {

        properties = new RepositorySettings("").getProperties();

        // Mock Constructors
        mongo = mock(MongoClient.class);
        PowerMockito.whenNew(MongoClient.class).
                withArguments(
                        eq("127.0.0.1"), eq(27017)).
                thenReturn(mongo);

        mongoResourceDAO = mock(MongoResourceDAO.class);
        PowerMockito.whenNew(MongoResourceDAO.class)
                .withArguments(eq(db)).
                thenReturn(mongoResourceDAO);

        VirtuosoResourceDAO virtResDao = mock(VirtuosoResourceDAO.class);
        when(virtuosoDAOFactory.getVirtuosoResourceDAO(properties)).thenReturn(virtResDao);
        mongoCollectionDAO = mock(MongoCollectionDAO.class);
        PowerMockito.whenNew(MongoCollectionDAO.class).
                withArguments(eq(db), eq(virtResDao)).
                thenReturn(mongoCollectionDAO);

        mongoUserDAO = mock(MongoUserDAO.class);
        PowerMockito.whenNew(MongoUserDAO.class).
                withArguments(eq(db)).
                thenReturn(mongoUserDAO);
    }

    @After
    public void tearDown() {
    }

    @Test (expected = UnknownHostException.class)
    public void testCreateConnectionUnkownHost() {

        when(mongo.getDatabase(isA(String.class))).thenThrow(UnknownHostException.class);
        toTest.createConnection(properties);
    }

    @Test
    public void testCreateConnection() throws Exception {
        when(mongo.getDatabase(eq("test"))).thenReturn(db);

        toTest.createConnection(properties);

        // Check that the connection has been created
        PowerMockito.verifyNew(MongoClient.class).withArguments(isA(String.class), isA(Integer.class));
        verify(mongo).getDatabase(anyString());

        assertEquals(mongoResourceDAO, toTest.getResourceDAO());
        assertEquals(mongoCollectionDAO, toTest.getCollectionDAO());
        assertEquals(mongoUserDAO, toTest.getUserDao());
    }
}
