/*
Modified BSD License
====================

Copyright (c) 2012, SAP AG
Copyright (c) 2015, CoNWeT Lab., Universidad Politécnica de Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the copyright holders nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.fiware.apps.repository.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RepositorySettings {
        private static Properties properties;

	public static String REPOSITORY_BASE_URL = "http://localhost:8080/FiwareRepository/";
        public final static String COLLECTION_SERVICE_NAME = "collec";
        public final static String QUERY_SERVICE_NAME = "query";

        public static String getProperty(String property) {
        if(properties == null) {
            try {
                properties = new Properties();
                properties.load(new FileInputStream(new File("/etc/default/Repository-RI.properties")));
            } catch (IOException ex) {
                //Insert default properties.
                properties = new Properties();

                // MongoDB
                properties.setProperty("mongodb.host", "127.0.0.1");
                properties.setProperty("mongodb.db", "test");
                properties.setProperty("mongodb.port", "27017");

                //Virtuoso DB
                properties.setProperty("virtuoso.host", "jdbc:virtuoso://localhost:");
                properties.setProperty("virtuoso.port", "1111");
                properties.setProperty("virtuoso.user", "dba");
                properties.setProperty("virtuoso.pass", "dba");

                //Oauth2
                properties.setProperty("oauth2.server", "https://account.lab.fiware.org");
                properties.setProperty("oauth2.key", "61686dc9f9734d0ba3237ae573c63a1c");
                properties.setProperty("oauth2.secret", "01c8253c51bc42e48561bbc6ded05a84");
                properties.setProperty("oauth2.callbackURL", "http://localhost:8080/FiwareRepository/v2/callback");
            }
        }
        return properties.getProperty(property);
    }

}
