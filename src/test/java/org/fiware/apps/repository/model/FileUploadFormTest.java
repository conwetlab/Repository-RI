/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fiware.apps.repository.model;

import org.fiware.apps.repository.model.FileUploadForm;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jortiz
 */
public class FileUploadFormTest {

    private FileUploadForm toTest;

    public FileUploadFormTest() {
    }


    @Test
    public void FileUploadFormTotalTest()
    {
        byte data[] = "data".getBytes();
        String mimeType = "mimeType";
        String fileName = "fileName";

        toTest = new FileUploadForm();

        toTest.setFileData(data);

        toTest.setMimeType(mimeType);

        toTest.setFilename(fileName);

        assertEquals(data, toTest.getFileData());
        assertEquals(mimeType, toTest.getMimeType());
        assertEquals(fileName, toTest.getFilename());

    }
}
