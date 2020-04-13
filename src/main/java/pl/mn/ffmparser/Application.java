package pl.mn.ffmparser;

import pl.mn.ffmparser.document.HTMLDocumentProcessor;
import pl.mn.ffmparser.document.TextProcessor;
import pl.mn.ffmparser.parser.CsvParser;
import pl.mn.ffmparser.parser.FileParser;
import pl.mn.ffmparser.parser.PrnParser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Application {

    public static final String WORKBOOKS_PATH = "workbooks/";
    private static final FileParser rootParser;
    private static final TextProcessor textProcessor = new HTMLDocumentProcessor();
    static {
        rootParser = FileParser.builder()
                .with(new CsvParser())
                .with(new PrnParser())
                .build();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        final URI workbooksURI = Application.class.getClassLoader().getResource(WORKBOOKS_PATH).toURI();
        final Map<String, String> env = new HashMap<>();
        final String[] array = workbooksURI.toString().split("!");
        try (final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env)) {
            final Path workbooksPath = fs.getPath(array[1]);
            try (final Stream<Path> pathStream = Files.walk(workbooksPath)) {
                pathStream.filter(file -> !Files.isDirectory(file))
                        .map(rootParser::processFile)
                        .filter(list -> !list.isEmpty())
                        .forEach(textProcessor::process);
            }

        }
    }

}
