package pl.mn.ffmparser.parser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParser extends FileParser {

    private static final String CSV_DELIMITER = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    public CsvParser() {
        parserExtensions = Set.of(CSV_EXTENSION);
    }

    @Override
    protected List<List<String>> parseFile(Path file) {
        try(final Stream<String> lines = Files.lines(file, StandardCharsets.ISO_8859_1)) {
            return lines.map(line -> Arrays.stream(line.split(CSV_DELIMITER)).map(text -> text.replace("\"", ""))
                    .collect(Collectors.toList()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Exception while parsing CSV file");
        }

        return List.of();
    }

}
