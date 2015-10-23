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
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositorySettings {
    private static Properties properties;
    public final static String COLLECTION_SERVICE_NAME = "collec";
    public final static String QUERY_SERVICE_NAME = "query";

    public RepositorySettings(String propertiesFile) {
        properties = new Properties();

        for (DefaultProperties prop : DefaultProperties.values()) {
            properties.setProperty(prop.getPropertyName(), prop.getValue());
        }

        try (FileInputStream fis = new FileInputStream(new File(propertiesFile))) {
            Properties fileProperties = new Properties();
            fileProperties.load(fis);

            for (DefaultProperties prop : DefaultProperties.values()) {
                checkProperty(fileProperties, prop.getPropertyName());
            }

            properties.putAll(fileProperties);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        } catch (IOException ex) {
            //Logger.getLogger(RepositorySettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Properties getProperties() { return properties; }

    private static String checkProperty(Properties properties, String name) {
        String value = properties.getProperty(name);

        if (value == null) {
            throw new IllegalArgumentException("Property " + name + "is not defined");
        } else {
            return value;
        }
    }

}
