package com.feng.baoan;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileNameReader
{
    private static String path = "";
    private static String extension = "";
    private static List<String> fileNames = new ArrayList<>();

    public static void main( String[] args )
    {
        if ( readCfg() ) {
            readFileName();
            writeXLS();
        }
    }

    static void readFileName() {
        File directory = new File(path);
        File[] allFiles = directory.listFiles();

        if ( allFiles != null ) {
            for ( File file : allFiles ) {
                if ( file.isFile() && !file.isHidden() ) {
                    String totalName = file.getName();
                    int index = totalName.lastIndexOf('.');
                    String fileExt = "";
                    String fileName = "";
                    if ( index > 0 ) {
                        fileExt = totalName.substring(index + 1);
                        fileName = totalName.substring(0, index);
                    }
                    if ( extension.equals("") || extension.equals(fileExt) ) {
                        fileNames.add(fileName);
                    }
                }
            }
        }
    }

    static void writeXLS() {
        String strFile = getCurrentPath() + System.getProperty("file.separator") + "list.xls";
        File file = new File(strFile);
        if ( file.exists() ) {
            if ( !file.delete() ) {
                return;
            }
        }
        if( !createNewFile(file) ){
            return;
        }

        WritableWorkbook newBook;
        WritableSheet sheet;
        try {
            newBook = Workbook.createWorkbook(file);

            sheet = newBook.createSheet("FileNames", 0);
            //Label label1 = new Label(0, 0, "File Name");
            //sheet.addCell(label1);

            for ( String fileName : fileNames ) {
                int nRow = sheet.getRows();
                Label dataLabel1 = new Label(0, nRow, fileName);
                sheet.addCell(dataLabel1);
            }
        }
        catch(IOException | WriteException e) {
            return;
        }

        try {
            newBook.write();
            newBook.close();
        }
        catch (WriteException | IOException ignored) {
        }
    }

    static boolean readCfg(){
        String strXmlpath = getCurrentPath() + System.getProperty("file.separator") + "config.xml";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            return false;
        }

        try {
            document = builder.parse(new File(strXmlpath));
        }
        catch (SAXException | IOException e) {
            return false;
        }

        Element rootElement = document.getDocumentElement();
        NodeList list = rootElement.getElementsByTagName("path");
        if (null == list || list.getLength() <= 0){
            path = getCurrentPath();
        }
        else {
            Element element = (Element)list.item(0);
            path = element.getTextContent();
            if ( null == path || path.equals("") ) {
                path = getCurrentPath();
            }
        }

        list = rootElement.getElementsByTagName("extension");
        if (null == list || list.getLength() <= 0){
            extension = "";
        }
        else {
            Element element = (Element)list.item(0);
            extension = element.getTextContent();
            if ( null == extension ) {
                extension = "";
            }
        }

        return true;
    }

    static String getCurrentPath() {
        File file = new File("");
        return file.getAbsolutePath();
    }

    static boolean createNewFile(File file) {
        try {
            file.createNewFile();
        }
        catch (IOException ex) {
            return false;
        }
        return true;
    }

    public static String getExtension() {
        return extension;
    }

    public static void setExtension(String extension) {
        FileNameReader.extension = extension;
    }

    public static List<String> getFileNames() {
        return fileNames;
    }

    public static void setFileNames(List<String> fileNames) {
        FileNameReader.fileNames = fileNames;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        FileNameReader.path = path;
    }
}
