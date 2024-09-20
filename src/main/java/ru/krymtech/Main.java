package ru.krymtech;

import java.io.IOException;
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
        String fileString = reduce(Files.readString(Paths.get(XML_FILENAME)));
        fileString = replaceXmlSymbols(fileString);
        System.out.println(fileString);
    }

    public static String replaceXmlSymbols(String input) {
        StringBuilder xml = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            if (i < input.length() - 1
                    && input.charAt(i) == '>'
                    && input.charAt(i + 1) != '<') {
                xml.append(input.charAt(i));
                StringBuilder tagContent = new StringBuilder();
                int j = i + 1;
                for (; input.charAt(j) != '<'
                        && input.charAt(j + 1) != '/'
                        && j < input.length() - 2; j++) {
                    tagContent.append(input.charAt(j));
                }
                String corrected = tagContent.toString().replaceAll("[&<>':\"]", "_");
                xml.append(corrected).append(input.charAt(j));
                i = j;
                continue;
            }
            xml.append(input.charAt(i));
        }
        return xml.toString();
    }

    public static String reduce(String xml) {
        return xml
                .replaceAll("[\r?\n\t]", "")
                .replaceAll(">\\s+<", "><")
                .replaceAll("(<[^/][^>]*>) +| +(</[^>]+>)", "$1$2");
    }
}
