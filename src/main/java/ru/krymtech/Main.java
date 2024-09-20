package ru.krymtech;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

/* XML is not a regular language. It cannot be parsed using a regular expression.
 * An expression would work will break when it gets nested tags,
 * then if fixed that it will break on XML comments, CDATA sections, processor directives, namespaces, ...
 */

public class Main {
    public static final String XML_FILENAME = "src/main/resources/forbidden_symbols.xml";
    private static final Pattern p = Pattern.compile("<(.+)>(.+)</\\1>");
    private static final Queue<String> tree = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        String fileString = reduce(Files.readString(Paths.get(XML_FILENAME)));
        fileString = replaceXmlSymbols(fileString);
        System.out.println(fileString);
    }

    public static String replaceXmlSymbols(String input) {
        Matcher m = p.matcher(input);
        recurse(m, input);
        StringBuilder sb = new StringBuilder();
        for (String s : tree) sb.append(s);
        return sb.toString();
    }

    private static void recurse(Matcher m, String next) {
        String tag = "";
        String inner = next;
        if (m.find()) {
            tag = m.group(1);
            tree.add("<" + tag + ">");
            inner = m.group(2);
            recurse(m, inner);
        } else {
            inner = inner
                    .replaceAll("<" + tag + ">", "")
                    .replaceAll("</" + tag + ">", "")
                    .replaceAll("[&:'\"]", "_");
            tree.add(inner);
        }
        if (!tag.isBlank())
            tree.add("</" + tag + ">");
    }

    public static String reduce(String xml) {
        return xml
                .replaceAll("[\r?\n\t]", "")
                .replaceAll(">\\s+<", "><")
                .replaceAll("(<[^/][^>]*>) +| +(</[^>]+>)", "$1$2");
    }
}
