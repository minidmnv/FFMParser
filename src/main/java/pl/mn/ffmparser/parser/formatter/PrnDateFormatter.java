package pl.mn.ffmparser.parser.formatter;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class PrnDateFormatter implements Formatter {

    public static final String DATE_DELIMITER = "/";

    @Override
    public String format(String text) {
        if(text == null || text.length() != 8 || !isNumeric(text)) {
            throw new IllegalArgumentException("Date should have exactly 8 numeric signs");
        }

        return text.substring(6, 8) + DATE_DELIMITER + text.substring(4, 6) + DATE_DELIMITER + text.substring(0, 4);
    }
}
