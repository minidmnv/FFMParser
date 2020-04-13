package pl.mn.ffmparser.parser;

import pl.mn.ffmparser.parser.formatter.Formatter;
import pl.mn.ffmparser.parser.formatter.PrnDateFormatter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PrnParser extends FileParser {

    public static final int HEADER_ROW_INDEX = 0;
    private static final List<String> COLUMN_NAMES = List.of("Name", "Address", "Postcode", "Phone", "Credit Limit", "Birthday");
    private static final int BIRTHDAY_COLUMN_INDEX = 5;
    private static final PrnDateFormatter dateFormatter = new PrnDateFormatter();

    public PrnParser() {
        parserExtensions = Set.of(PRN_EXTENSION);
    }

    @Override
    protected List<List<String>> parseFile(Path file) {

        List<String> dataRows;
        try(final Stream<String> lines = Files.lines(file, StandardCharsets.ISO_8859_1)) {
            dataRows = lines.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Exception while parsing PRN file");
            return List.of();
        }

        final String headerRow = dataRows.get(HEADER_ROW_INDEX);
        final List<Integer> columnIndexes = COLUMN_NAMES.stream().map(headerRow::indexOf)
                .collect(Collectors.toList());

        List<List<String>> result = dataRows.stream().map(row -> {
            List<String> parsedRows = new ArrayList<>();
            IntStream.range(HEADER_ROW_INDEX, columnIndexes.size())
                    .forEach(index ->
                            parsedRows.add(row.substring(columnIndexes.get(index),
                                    index + 1 < columnIndexes.size() ? columnIndexes.get(index + 1) : row.length()).strip())
                    );
            return parsedRows;
        }).collect(Collectors.toList());

        IntStream.range(HEADER_ROW_INDEX + 1, result.size())
                .forEach(
                        index -> result.get(index)
                                .set(BIRTHDAY_COLUMN_INDEX, dateFormatter.format(result.get(index).get(BIRTHDAY_COLUMN_INDEX)))
                );

        return result;
    }

}
