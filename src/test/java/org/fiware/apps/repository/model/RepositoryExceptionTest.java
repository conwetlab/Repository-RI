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
