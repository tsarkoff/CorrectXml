package ru.krymtech;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

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
    public static final String XML_CDATA_FILENAME = "src/main/resources/cdata_symbols.xml";
    public static final String XML_HEAVY_FILENAME = "src/main/resources/heavy-n-forbidden_symbols.xml";
    public static final String[] XML_FILENAMES = {XML_FILENAME, XML_HEAVY_FILENAME, XML_CDATA_FILENAME};
    public static final String REGEX_RESERVED_XML_SYMBOLS = "[\\\\&<>':\"]";
    public static final String CDATA_ENTRY = "<![CDATA[";

    public static void main(String[] args) throws IOException {
        for (String filename : XML_FILENAMES) {
            String fileString = Utils.reduce(Files.readString(Paths.get(filename)));
            fileString = replaceXmlSymbols(fileString);
            System.out.println("===> Raw XML output:");
            System.out.println(fileString);
            System.out.println("===> Pretty XML output:");
            System.out.println(Utils.prettyXlm(fileString));
        }
    }

    public static String replaceXmlSymbols(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        Queue<String> queue = new LinkedList<>();

        int i = 0;
        int lt = 0;
        int gt = 0;
        int size = input.length();

        while (i < size) {
            lt = input.indexOf('<', gt);
            gt = input.indexOf('>', lt);

            if (input.startsWith(CDATA_ENTRY, lt)) {
                gt = input.indexOf("]]>", gt);
                i = gt + 3;
                queue.add(input.substring(lt, i));
                continue;
            }

            String ltTag = input.substring(lt + 1, gt);
            queue.add("<" + ltTag + ">");

            if (lt + ("<" + ltTag + ">").length() >= size)
                break;

            if (input.charAt(gt + 1) == '<' && input.charAt(gt + 2) != '\\')
                continue;

            int gtTag = input.indexOf("</" + ltTag + ">", gt);
            String data = input.substring(gt + 1, gtTag);
            data = data.replaceAll(REGEX_RESERVED_XML_SYMBOLS, "_");
            queue.add(data);
            i = gtTag + ("</" + ltTag + ">").length();
        }

        for (String s : queue)
            sb.append(s);
        return sb.toString();
    }
}
