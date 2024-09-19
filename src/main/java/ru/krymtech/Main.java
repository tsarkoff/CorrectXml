package ru.krymtech;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
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

    public static void main(String[] args) throws IOException, XMLStreamException {
        byte[] bytes = Files.readAllBytes(Paths.get(XML_FILENAME));
        String fileString = new String(bytes, StandardCharsets.UTF_8);
        fileString = replaceXmlSymbols(fileString);
        System.out.println(fileString);
    }

    public static String replaceXmlSymbols(String input) throws XMLStreamException {
        StringBuilder sb = new StringBuilder(input.length() * 2);
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new StringReader(input));
        StartElement startElement;
        EndElement endElement;
        String elementData;
        while (reader.hasNext()) {
            try {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    startElement = nextEvent.asStartElement();
                    sb.append("<").append(startElement.getName()).append(">");
                }
                if (nextEvent.isEndElement()) {
                    endElement = nextEvent.asEndElement();
                    sb.append("<").append(endElement.getName()).append(">");
                }
                if (nextEvent.isCharacters()) {
                    elementData = nextEvent.asCharacters().getData();
                    for (char c : elementData.toCharArray())
                        sb.append((c == '<' || c == '>' || c == '&' || c == '\"' || c == '\'' || c == ':') ? '_' : c);
                }
            } catch (XMLStreamException e) {
                System.out.println(e.getMessage());
                break;  // if no break, then error is bypassed and caught into a endless loop
            }
        }
        return sb.toString();
    }
}
