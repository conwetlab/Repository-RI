/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeT Lab, Universidad Politecnica Madrid
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
package org.fiware.apps.repository.settings;

/**
 *
 * @author jortiz
 */
public enum DefaultProperties {
    MONGO_HOST ("mongodb.host", "127.0.0.1"),
    MONGO_PORT ("mongodb.port", "27017"),
    MONGO_DB ("mongodb.db", "test"),
    VIRTUOSO_HOST ("virtuoso.host", "jdbc:virtuoso://localhost:"),
    VIRTUOSO_PORT ("virtuoso.port", "1111"),
    VIRTUOSO_USER ("virtuoso.user", "dba"),
    VIRTUOSO_PASSWORD ("virtuoso.password", "dba"),
    OAUTH2_SERVER ("oauth2.server", "https://account.lab.fiware.org"),
    OAUTH2_KEY ("oauth2.key", ""),
    OAUTH2_SECRET ("oauth2.secret", ""),
    OAUTH2_CALLBACKURL ("oauth2.callbackURL", "");

    private final String propertyName, value;

    DefaultProperties (String propertyName, String value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {return this.propertyName; }
    public String getValue() { return this.value; }
}
