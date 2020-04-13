package pl.mn.ffmparser.parser;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.mn.ffmparser.parser.FileParserTest.RESOURCES_PATH;

class PrnParserTest {
    private final PrnParser prnParser = new PrnParser();

    @Test
    void isDelimiterForHeaderReturnsGoodNumberOfColumns() {
        final List<List<String>> result = prnParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2.prn"));
        assertEquals(6, result.get(0).size());
    }

    @Test
    void isRowDelimitingReturnsNumberOfRowsAsItShould() {
        final List<List<String>> result = prnParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2.prn"));
        assertEquals(8, result.size());
    }

    @Test
    void isLoggingProblemWhenFileDoesntExists() {
        final List<List<String>> result = prnParser.processFile(Paths.get(RESOURCES_PATH, "Workbook2s.prn"));
        assertTrue(result.isEmpty());
    }
}