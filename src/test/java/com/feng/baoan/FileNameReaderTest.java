package com.feng.baoan;


import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for FileNameReader.
 */
public class FileNameReaderTest
{
    @Test
    public void testReadCfg() {
        FileNameReader.readCfg();

        Assert.assertEquals(FileNameReader.getPath(), "/home/baoan/dev/java/ReadFileNameToXLS");
        Assert.assertEquals(FileNameReader.getExtension(), "xml");
    }

    @Test
    public void testReadFileName() {
        FileNameReader.readCfg();
        FileNameReader.readFileName();

        List<String> fileNames = new ArrayList<>();
        fileNames.add("pom");
        fileNames.add("config");

        int index = 0;
        for ( String fileName : fileNames ) {
            Assert.assertEquals(fileName, FileNameReader.getFileNames().get(index++));
        }
    }

}
