/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeTLab, Universidad Politécnica de Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the UPMnor the
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

import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({RepositorySettings.class, Properties.class})
public class RepositorySettingsTest {

    private RepositorySettings toTest;

    public RepositorySettingsTest() {
    }

    @Before
    public void setUp() {
        toTest = new RepositorySettings("");
    }

    @Test
    public void RepositorySettingsConstructor() {
        toTest = new RepositorySettings(RepositorySettingsTest.class.getClassLoader().getResource("testProperties/PropertiesTest.properties").getPath());

        for (DefaultProperties prop : DefaultProperties.values()) {
            assertNotEquals(toTest.getProperties().getProperty(prop.getPropertyName()), prop.getValue());
        }
    }

    @Test
    (expected = IllegalArgumentException.class)
    public void RepositorySettingsConstructorBadPropertiesFile() {
        toTest = new RepositorySettings(RepositorySettingsTest.class.getClassLoader().getResource("testProperties/BadPropertiesTest.properties").getPath());
    }

    @Test
    public void RepositorySettingsConstructorFileNotFound() {
        toTest = new RepositorySettings("badpath");

        for (DefaultProperties prop : DefaultProperties.values()) {
            assertEquals(toTest.getProperties().getProperty(prop.getPropertyName()), prop.getValue());
        }
    }
}
