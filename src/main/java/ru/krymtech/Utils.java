package ru.krymtech;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;

public class Utils {

    public static String reduce(String xml) {
        return xml
                .replaceAll("[\r\n\t]", "")
                .replaceAll(">\\s+<", "><")
                .replaceAll("(<[^/][^>]*>) +| +(</[^>]+>)", "$1$2");
    }

    public static String prettyXlm(String xml) {
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            Document document = DocumentHelper.parseText(xml);
            StringWriter sw = new StringWriter();
            XMLWriter writer = new XMLWriter(sw, format);
            writer.write(document);
            return sw.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}