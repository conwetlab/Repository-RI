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
* Neither the name of UPM nor the
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
package org.fiware.apps.repository.it.queryService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.fiware.apps.repository.settings.RepositorySettings;


public class QueryServiceTestClient {

    private HttpClient client;

    public QueryServiceTestClient() {
        this.client = new DefaultHttpClient();
    }

    public HttpResponse getResourceByContentUrl(String query, List <Header> headers) throws IOException {
        String finalURL = RepositorySettings.REPOSITORY_BASE_URL + RepositorySettings.QUERY_SERVICE_NAME + "/";
        HttpGet request = new HttpGet(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        return client.execute(request);
    }

    public HttpResponse getQuery(String query, List <Header> headers) throws IOException {
        String finalURL = RepositorySettings.REPOSITORY_BASE_URL + RepositorySettings.QUERY_SERVICE_NAME + "?query=" + query;
        //TODO: Añadir la query codificada a la URL??
        HttpGet request = new HttpGet(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        return client.execute(request);
    }

    public HttpResponse postQuery(String query, List <Header> headers) throws IOException {
        String finalURL = RepositorySettings.REPOSITORY_BASE_URL + RepositorySettings.QUERY_SERVICE_NAME;
        HttpPost request = new HttpPost(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        //Add Entity
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream stream = new ByteArrayInputStream(query.getBytes());

        entity.setContent(stream);
        request.setEntity(entity);

        return client.execute(request);
    }

}
