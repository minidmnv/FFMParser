package pl.mn.ffmparser.document;

import pl.mn.ffmparser.Application;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class HTMLDocumentProcessor implements TextProcessor {

    private static final int HEADERS_INDEX = 0;
    public static final String HEADER_PLACEHOLDER = "${header_%d}";
    public static final String TABLE_DATA_PLACEHOLDER = "${table_data}";
    public static final String ROW_DATA_PLACEHOLDER = "${row_data}";
    public static final String OUTPUT_TEMPLATE_HTML = "output_template.html";
    private String templateString;

    public HTMLDocumentProcessor() {
        URI templateHTMLuri = null;
        try {
            templateHTMLuri = Application.class.getClassLoader().getResource(OUTPUT_TEMPLATE_HTML).toURI();
        } catch (URISyntaxException uriSyntaxException) {
            uriSyntaxException.printStackTrace();
        }
        final Map<String, String> env = new HashMap<>();
        final String[] array = templateHTMLuri.toString().split("!");
        try (final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env)) {
            final Path workbooksPath = fs.getPath(array[1]);
            templateString = Files.readString(workbooksPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(List<List<String>> parsedText) {
        try {
            final UUID uuid = UUID.randomUUID();
            final StringBuilder htmlBuilder = constructHTML(parsedText, templateString);
            String htmlString = removePlaceholders(htmlBuilder);

            final Path outputFilePath = Path.of(String.format("generated_output_%s.html", uuid.toString()));
            Files.write(outputFilePath, htmlString.getBytes());
            Desktop.getDesktop().browse(outputFilePath.toUri());

        } catch (IOException e) {
            System.out.println("Exception while showing html site: " + e);
        }
    }

    private StringBuilder constructHTML(List<List<String>> parsedText, String htmlString) {
        final StringBuilder htmlBuilder = new StringBuilder(htmlString);
        insertHeaders(parsedText, htmlBuilder);
        parsedText.remove(HEADERS_INDEX);

        insertData(parsedText, htmlBuilder);
        return htmlBuilder;
    }

    private void insertData(List<List<String>> parsedText, StringBuilder htmlBuilder) {
        parsedText.forEach(
                rowData -> {
                    htmlBuilder.insert(htmlBuilder.indexOf(TABLE_DATA_PLACEHOLDER), "<tr>" + ROW_DATA_PLACEHOLDER + "</tr>");
                    rowData.forEach(data ->
                        htmlBuilder.insert(htmlBuilder.indexOf(ROW_DATA_PLACEHOLDER), "<td>" + data + "</td>")
                    );
                    htmlBuilder.replace(htmlBuilder.indexOf(ROW_DATA_PLACEHOLDER), htmlBuilder.indexOf(ROW_DATA_PLACEHOLDER) + ROW_DATA_PLACEHOLDER.length(), "");
                }
        );
    }

    private void insertHeaders(List<List<String>> parsedText, StringBuilder htmlBuilder) {
        final List<String> headers = parsedText.get(HEADERS_INDEX);
        IntStream.range(0, headers.size())
                .forEach(index -> htmlBuilder.insert(htmlBuilder.indexOf(String.format(HEADER_PLACEHOLDER, index)), headers.get(index)));
    }

    private String removePlaceholders(StringBuilder htmlBuilder) {
        String htmlString;
        final Pattern pattern = Pattern.compile("\\$\\{\\w*\\}");

        htmlString = pattern.matcher(htmlBuilder).replaceAll("");
        return htmlString;
    }


}
