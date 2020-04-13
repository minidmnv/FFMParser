package pl.mn.ffmparser.parser;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.mn.ffmparser.parser.FileParserTest.RESOURCES_PATH;

class CsvParserTest {

    private final CsvParser csvParser = new CsvParser();

    @Test
    void isDelimiterOmitsQuotes() {
        final List<List<String>> result = csvParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2.csv"));
        assertEquals(6, result.get(0).size());
    }

    @Test
    void isRowDelimitingReturnsNumberOfRowsAsItShould() {
        final List<List<String>> result = csvParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2.csv"));
        assertEquals(8, result.size());
    }

    @Test
    void isLoggingProblemWhenFileDoesntExists() {
        final List<List<String>> result = csvParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2s.csv"));
        assertTrue(result.isEmpty());
    }

}