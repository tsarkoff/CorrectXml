package ru.krymtech;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * Использованы материалы из RFC и w3.org:
 * rfc.org https://www.rfc-editor.org/rfc/rfc3076.txt (3.4 Character Modifications and Character References)
 * w3.org https://www.w3.org/TR/xml/ (2.2 Characters)
 * Reserved Characters: These are characters that have special meanings within XML itself, like angle brackets (<, >), the ampersand (&), and quotation marks "",''.
 * Basic Latin Alphabet: All characters from the basic Latin alphabet (A-Z, a-z) are valid in XML.
 * Digits: Numeric digits (0-9) are allowed.
 * Special Characters: Certain special characters such as hyphen (-), underscore (_), period (.), colon (:), and comma (,), among others, are permitted in XML.
 * Unicode Characters: XML supports a vast range of Unicode characters beyond the basic Latin alphabet, allowing representation of various languages, symbols, and emojis.
 */

public class Main {
    public static final String XML_FILENAME = "src/main/resources/forbidden_symbols.xml";

    public static void main(String[] args) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(XML_FILENAME));
        String fileString = new String(bytes, StandardCharsets.UTF_8);
        fileString = replaceXmlSymbols(fileString);
        System.out.println(fileString);
    }

    public static String replaceXmlSymbols(String input) {
        CorrectionHandler correctionHandler = new CorrectionHandler();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new InputSource(new StringReader(input)), correctionHandler);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Failed to parse XML: " + e.getMessage());
        }
        return correctionHandler.getCorrectedXml();
    }

    private static class CorrectionHandler extends DefaultHandler {
        private final StringBuilder sb = new StringBuilder();

        @Override
        // Doesn't work = it throws SAXException and stops parsing w/o possibility to continue XML assembling
        public void characters(char[] ch, int start, int length) {
            String content = new String(ch, start, length);
            for (char c : content.toCharArray())
                sb.append((c == '<' || c == '>' || c == '&' || c == '\"' || c == '\'' || c == ':') ? '_' : c);
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) {
            sb.append(ch, start, length);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            sb.append("<").append(qName);
            for (int i = 0; i < attributes.getLength(); i++) {
                sb.append(" ").append(attributes.getQName(i)).append("=\"").append(attributes.getValue(i)).append("\"");
            }
            sb.append(">");
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            sb.append("</").append(qName).append(">");
        }

        public String getCorrectedXml() {
            return sb.toString();
        }
    }
}
