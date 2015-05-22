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
* Neither the name of the SAP AG nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SAP AG BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.fiware.apps.repository.oauth2;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Request;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProxyOAuthRequest.class, ProxyOAuthServiceImpl.class, Request.class})
public class ProxyOAuthServiceImplTest {


    ProxyOAuthServiceImpl toTest;
    @Mock DefaultApi20 api;
    @Mock OAuthConfig config;
    @Mock ProxyOAuthRequest request;
    @Mock Response response;
    @Mock Verifier verifier;
    @Mock AccessTokenExtractor tokenExtractor;

    public ProxyOAuthServiceImplTest() {
    }

    @Before
    public void setUp() {
        try {
            api = mock(DefaultApi20.class);
            config = mock(OAuthConfig.class);
            request = mock(ProxyOAuthRequest.class);
            response = mock(Response.class);
            tokenExtractor = mock(AccessTokenExtractor.class);
            verifier = mock(Verifier.class);

            when(api.getAccessTokenExtractor()).thenReturn(tokenExtractor);
            when(request.send()).thenReturn(response);
            when(response.getBody()).thenReturn("body");
            when(verifier.getValue()).thenReturn("value");

            PowerMockito.whenNew(ProxyOAuthRequest.class).withAnyArguments().thenReturn(request);

        } catch (Exception ex) {
            Logger.getLogger(ProxyOAuthServiceImplTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getAccessTokenGetParameterTest() {
        Token returned;
        String token = "token";
        String secret = "secret";
        String apiKey = "apiKey";
        String apiSecret = "apiSecret";
        String scope = "scope";
        String accesTokenEndPoint = "endPoint";
        String verif = "verifier";
        toTest = new ProxyOAuthServiceImpl(api, config, 100, 100, "proxyhost", 50, true, true);

        when(api.getAccessTokenVerb()).thenReturn(Verb.GET);
        when(api.getAccessTokenEndpoint()).thenReturn(accesTokenEndPoint);

        when(config.getApiKey()).thenReturn(apiKey);
        when(config.getApiSecret()).thenReturn(apiSecret);
        when(config.getScope()).thenReturn(scope);
        when(config.hasScope()).thenReturn(true);

        when(tokenExtractor.extract(anyString())).thenReturn(new Token(token, secret));

        returned = toTest.getAccessToken(new Token("", ""), new Verifier(verif));

        assertEquals(returned.getToken(), token);
        assertEquals(returned.getSecret(), secret);
    }

    @Test
    public void getAccessTokenGetParameter2Test() {
        Token returned;
        String token = "token";
        String secret = "secret";
        String apiKey = "apiKey";
        String apiSecret = "apiSecret";
        String scope = "scope";
        String accesTokenEndPoint = "endPoint";
        String verif = "verifier";
        toTest = new ProxyOAuthServiceImpl(api, config, 100, 100, "proxyhost", 50);

        when(api.getAccessTokenVerb()).thenReturn(Verb.GET);
        when(api.getAccessTokenEndpoint()).thenReturn(accesTokenEndPoint);

        when(config.getApiKey()).thenReturn(apiKey);
        when(config.getApiSecret()).thenReturn(apiSecret);
        when(config.getScope()).thenReturn(scope);
        when(config.hasScope()).thenReturn(false);

        when(tokenExtractor.extract(anyString())).thenReturn(new Token(token, secret));

        returned = toTest.getAccessToken(new Token("", ""), new Verifier(verif));

        assertEquals(returned.getToken(), token);
        assertEquals(returned.getSecret(), secret);
    }

    @Test
    public void getAccessTokenNotGetParameterTest() {
        Token returned;
        String token = "token";
        String secret = "secret";
        String apiKey = "apiKey";
        String apiSecret = "apiSecret";
        String scope = "scope";
        String accesTokenEndPoint = "endPoint";
        String verif = "verifier";
        toTest = new ProxyOAuthServiceImpl(api, config, 100, 100, "proxyhost", 50, false, true);

        when(api.getAccessTokenVerb()).thenReturn(Verb.GET);
        when(api.getAccessTokenEndpoint()).thenReturn(accesTokenEndPoint);

        when(config.getApiKey()).thenReturn(apiKey);
        when(config.getApiSecret()).thenReturn(apiSecret);
        when(config.getScope()).thenReturn(scope);
        when(config.hasScope()).thenReturn(true);

        when(tokenExtractor.extract(anyString())).thenReturn(new Token(token, secret));

        returned = toTest.getAccessToken(new Token("", ""), new Verifier(verif));

        assertEquals(returned.getToken(), token);
        assertEquals(returned.getSecret(), secret);
    }

    @Test
    public void getAccessTokenNotGetParameter2Test() {
        Token returned;
        String token = "token";
        String secret = "secret";
        String apiKey = "apiKey";
        String apiSecret = "apiSecret";
        String scope = "scope";
        String accesTokenEndPoint = "endPoint";
        String verif = "verifier";
        toTest = new ProxyOAuthServiceImpl(api, config, 100, 100, "proxyhost", 50, false, false);

        when(api.getAccessTokenVerb()).thenReturn(Verb.GET);
        when(api.getAccessTokenEndpoint()).thenReturn(accesTokenEndPoint);

        when(config.getApiKey()).thenReturn(apiKey);
        when(config.getApiSecret()).thenReturn(apiSecret);
        when(config.getScope()).thenReturn(scope);
        when(config.hasScope()).thenReturn(false);

        when(tokenExtractor.extract(anyString())).thenReturn(new Token(token, secret));

        returned = toTest.getAccessToken(new Token("", ""), new Verifier(verif));

        assertEquals(returned.getToken(), token);
        assertEquals(returned.getSecret(), secret);
    }
}
