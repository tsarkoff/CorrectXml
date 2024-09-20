package ru.krymtech;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainTest {
    @ParameterizedTest
    @ValueSource(strings =
            {
                    Main.XML_FILENAME,
                    Main.XML_HEAVY_FILENAME,
                    Main.XML_CDATA_FILENAME
            }
    )
    public void replaceXmlSymbols(String xmlFilename) throws IOException {
        String fileString = Utils.reduce(Files.readString(Paths.get(xmlFilename)));
        fileString = Main.replaceXmlSymbols(fileString);
        Assertions.assertNotNull(Utils.prettyXlm(fileString));
    }
}