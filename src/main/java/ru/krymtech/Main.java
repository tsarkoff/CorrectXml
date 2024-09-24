package ru.krymtech;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    private static final String XML_HEAVY_FILENAME = "src/main/resources/heavy-n-forbidden_symbols.xml";
    private static final String REGEX_RESERVED_XML_SYMBOLS = "[\\\\&<>':\"]";

    public static void main(String[] args) throws IOException {
        String fileString = Files.readString(Paths.get(XML_HEAVY_FILENAME)).replaceAll("[\r\n\t]", "").replaceAll(">\\s+<", "><").replaceAll("(<[^/][^>]*>) +| +(</[^>]+>)", "$1$2");
        System.out.println(fileString);
        System.out.println(replaceXmlSymbols(fileString));
    }

    public static String replaceXmlSymbols(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        Queue<String> queue = new LinkedList<>();
        String ltTag, gtTag;
        int lt, gt, stringPosition = 0;

        while (stringPosition < input.length()) {
            lt = input.indexOf('<', stringPosition);
            gt = input.indexOf('>', lt);
            queue.add(ltTag = input.substring(lt, gt + 1));

            stringPosition = gt;
            if (lt + ltTag.length() >= input.length()) break;
            if (input.charAt(gt + 1) == '<' && input.charAt(gt + 2) != '\\') continue;

            int gtTagIx = input.indexOf(gtTag = ltTag.replace("<", "</"), gt);

            queue.add(input.substring(gt + 1, gtTagIx).replaceAll(REGEX_RESERVED_XML_SYMBOLS, "_"));
            queue.add(gtTag);
            stringPosition = gtTagIx + gtTag.length();
        }

        for (String s : queue) sb.append(s);
        return sb.toString();
    }
}
