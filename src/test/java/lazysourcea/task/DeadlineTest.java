package lazysourcea.task;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeadlineTest {

    @Test
    void toString_formatsDateAsPretty() {
        Deadline d = new Deadline("return book", LocalDate.of(2019, 12, 2));
        String s = d.toString();
        assertTrue(s.contains("Dec 2 2019"));
        assertTrue(s.startsWith("[D][ ]"));
    }

    @Test
    void toDataString_savesIsoDate() {
        Deadline d = new Deadline("return book", LocalDate.of(2019, 12, 2));
        assertEquals("D | 0 | return book | 2019-12-02", d.toDataString());
        d.markAsDone();
        assertEquals("D | 1 | return book | 2019-12-02", d.toDataString());
    }
}
