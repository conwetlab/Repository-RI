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
package org.fiware.apps.repository.model;

import java.util.List;
import org.fiware.apps.repository.model.Column;
import org.fiware.apps.repository.model.SelectQueryResponse;
import org.junit.Test;
import static org.junit.Assert.*;

public class SelectQueryResponseTest {
    
    private SelectQueryResponse toTest;
    
    public SelectQueryResponseTest() {
    }
    
    @Test
    public void SelectQueryResponseTotalTest() {
        String name = "string";
        int iLimit = 50;
        int jLimit = 50;
        List <Column> list;
        
        toTest = new SelectQueryResponse();
        
        for (int i = 0; i<iLimit; i++) {
            toTest.addColumn(name+i);
            for (int j = 0; j<jLimit; j++) {
                toTest.addValue(i, name+j+"."+i);
            }
        }
        
        list = toTest.getColumns();
        
        assertEquals(iLimit, toTest.getVars());
        
        for (int i = 0; i<iLimit; i++) {
            assertEquals(list.get(i).getName(), name+i);
            for (int j = 0; j<jLimit; j++) {
                assertEquals(list.get(i).get(j), name+j+"."+i);
            }
        }
    }
}
