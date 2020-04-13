package pl.mn.ffmparser.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileParserTest {

    public static final String RESOURCES_PATH = "./src/test/resources/";
    @Spy
    final PrnParser prnParser = new PrnParser();
    @Spy
    final CsvParser csvParser = new CsvParser();
    private FileParser rootParser;


    @BeforeEach
    void setup() {
        rootParser = FileParser.builder()
                .with(prnParser)
                .with(prnParser)
                .with(csvParser)
                .build();
    }

    @Test
    void isBuilderPlacingCorrectRootParser() {
        assertEquals(prnParser, rootParser);
    }

    @Test
    void isParsersInChainNotRedundant() {
        rootParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2.csv"));
        verify(prnParser, times(1)).processFile(any());
    }

    @Test
    void isExtensionMatchingProperParsers() {
        rootParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2.csv"));
        verify(prnParser, times(0)).parseFile(any());
        verify(csvParser, times(1)).parseFile(any());
    }
}