package pl.mn.ffmparser.parser.formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PrnDateFormatterTest {

    public final PrnDateFormatter formatter = new PrnDateFormatter();

    @Test
    public void isProperFormatAfterFormatting() {
        String date = "19920717";

        Assertions.assertEquals("17/07/1992", formatter.format(date));
    }

    @Test
    public void isDateNotChangedAfterFormatting() {
        String date = "19901207";

        Assertions.assertEquals("07/12/1990", formatter.format(date));
    }

    @Test
    public void isExceptionThrownWhenArgumentIsNull() {
        String date = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> formatter.format(date));
    }

    @Test
    public void isExceptionThrownWhenDateFormatIsWrong() {
        String date = "1990778";

        Assertions.assertThrows(IllegalArgumentException.class, () -> formatter.format(date));
    }

    @Test
    public void isExceptionThrownWhenDateIsNotNumeric() {
        String date = "199a7787";

        Assertions.assertThrows(IllegalArgumentException.class, () -> formatter.format(date));
    }

}