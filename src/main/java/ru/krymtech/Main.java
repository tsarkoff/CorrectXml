package ru.krymtech;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Использованы материалы из RFC :
 * @rfc.org <a href="https://www.rfc-editor.org/rfc/rfc3076.txt">rfc3076</a>
 * 3.4 Character Modifications and Character References
 * @w3.org <a href="https://www.w3.org/TR/xml11/">xml11</a>
 * @Reserved Characters: These are characters that have special meanings within XML itself,
 * like angle brackets (<, >), the ampersand (&), and quotation marks (“, ‘).
 * If you try to use these characters directly in your data, they can confuse the XML parser and lead to errors.
 * @Valid Characters Allowed in XML
 * @Basic Latin Alphabet: All characters from the basic Latin alphabet (A-Z, a-z) are valid in XML.
 * @Digits: Numeric digits (0-9) are allowed.
 * @Special Characters: Certain special characters such as hyphen (-), underscore (_), period (.), colon (:), and comma (,), among others, are permitted in XML.
 * @Unicode Characters: XML supports a vast range of Unicode characters beyond the basic Latin alphabet, allowing representation of various languages, symbols, and emojis.
 */

public class Main {
    private enum Method {
        REPLACE_BY_CHAR,
        REPLACE_BY_REGEX
    }

    public static final String XML_FILENAME = "src/main/resources/forbidden_symbols.xml";

    public static void main(String[] args) throws IOException {
        Method method = Method.REPLACE_BY_CHAR;
        byte[] bytes = Files.readAllBytes(Paths.get(XML_FILENAME));
        String fileString = new String(bytes, StandardCharsets.UTF_8);
        String replacedXml = switch (method) {
            case REPLACE_BY_CHAR -> replaceXmlSymbols(fileString);
            case REPLACE_BY_REGEX -> replaceXmlString(fileString);
        };
        System.out.println(replacedXml);
    }

    public static String replaceXmlSymbols(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray())
            if (c == 0x9            // == '\t' (9)
                    || c == 0xA     // == '\n' (10)
                    || c == 0xD     // == '\r' (13)
                    || (c >= 0x20 && c <= 0xD7FF)
                    || (c >= 0xE000 && c <= 0xFFFD)
                    || (c >= 0x10000 && c <= 0x10FFFF)
            ) sb.append(c);
            else sb.append('_');

        return sb.toString();
    }

    public static String replaceXmlString(String input) {
        String regex = "[^\\x09\\x0A\\x0D\\x20-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFFF]";
        return input.replaceAll(regex, input);
    }
}
