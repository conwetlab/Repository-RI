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
