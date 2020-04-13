package pl.mn.ffmparser.parser;

import java.nio.file.Path;
import java.util.*;

public abstract class FileParser {

    public static final String CSV_EXTENSION = "CSV";
    public static final String PRN_EXTENSION = "PRN";
    public static final String DOT_DELIMITER = ".";

    private FileParser nextParser;
    protected Set<String> parserExtensions = Collections.emptySet();

    public List<List<String>> processFile(Path file) {
        return matchExtension(file) ? parseFile(file) : nextParser != null ? nextParser.processFile(file) : List.of();
    }

    protected abstract List<List<String>> parseFile(Path file);

    public static ParserChainBuilder builder() {
        return new ParserChainBuilder();
    }

    protected boolean matchExtension(Path file) {
        final String filename = file.toString();
        return parserExtensions.contains(filename.substring(filename.lastIndexOf(DOT_DELIMITER) + 1).toUpperCase());
    }

    public static class ParserChainBuilder {
        LinkedHashSet<FileParser> parsers = new LinkedHashSet<>();

        public ParserChainBuilder with(FileParser parser) {
            parsers.add(parser);
            return this;
        }

        public FileParser build() {
            final Iterator<FileParser> iterator = parsers.iterator();
            final FileParser chainRoot = iterator.next();

            parsers.forEach(p -> p.nextParser = iterator.hasNext() ? iterator.next() : null);

            return chainRoot;
        }
    }

}
