package ru.krymtech;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainTest {
    static String fileString;

    @BeforeClass
    public static void beforeClass() throws Exception {
        byte[] bytes = Files.readAllBytes(Paths.get(Main.XML_FILENAME));
        fileString = new String(bytes, StandardCharsets.UTF_8);
    }

    @Test
    public void replaceXmlSymbols() {
        Assertions.assertNull(parseXML(fileString));
    }

    @Test
    public void replaceXmlString() {
    }

    public static Document parseXML(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            Document document = (Document) builder.parse(inputSource);
            System.out.println("XML parsed successfully!");
            return document;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Failed to parse XML: " + e.getMessage());
            System.out.println("Invalid characters in XML");
            System.out.println(xmlString);
            return null;
        }
    }
}