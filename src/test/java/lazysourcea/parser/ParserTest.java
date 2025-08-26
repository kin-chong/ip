package lazysourcea.parser;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    @Test
    void parse_detectsCommandAndArgument() {
        Parser p = new Parser();
        Parser.Parsed parsed = p.parse("deadline return book /by 2019-12-02");
        assertEquals(Parser.CommandType.DEADLINE, parsed.type);
        assertEquals("return book /by 2019-12-02", parsed.arg);
    }

    @Test
    void parseDeadlineArgs_supportsIsoAndSlash() {
        Parser p = new Parser();
        Parser.DeadlineArgs d1 = p.parseDeadlineArgs("return book /by 2019-12-02");
        assertEquals("return book", d1.desc);
        assertEquals(LocalDate.of(2019, 12, 2), d1.by);

        Parser.DeadlineArgs d2 = p.parseDeadlineArgs("return book /by 2/12/2019");
        assertEquals(LocalDate.of(2019, 12, 2), d2.by);
    }

    @Test
    void parseIndex_validatesBounds() {
        Parser p = new Parser();
        assertEquals(0, p.parseIndex("1", 3));
        assertThrows(IndexOutOfBoundsException.class, () -> p.parseIndex("0", 3));
        assertThrows(IndexOutOfBoundsException.class, () -> p.parseIndex("4", 3));
        assertThrows(NumberFormatException.class, () -> p.parseIndex("abc", 3));
    }
}
