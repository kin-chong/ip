package lazysourcea.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input into structured commands and arguments.
 * <p>
 * The {@code Parser} class is responsible for:
 * <ul>
 *   <li>Identifying the command type from a string.</li>
 *   <li>Splitting arguments for tasks such as deadlines and events.</li>
 *   <li>Parsing dates into {@link LocalDate} objects.</li>
 *   <li>Validating indices when accessing tasks.</li>
 * </ul>
 */
public class Parser {

    /**
     * Enumeration of supported command types.
     */
    public enum CommandType {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, HELP, UNKNOWN, FIND
    }

    private static final DateTimeFormatter IN_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter IN_SLASH = DateTimeFormatter.ofPattern("d/M/yyyy");

    /**
     * Parses a raw input string into a structured {@link Parsed} object.
     *
     * @param raw the raw user input string
     * @return a {@code Parsed} object containing the command type, arguments, and raw input
     */
    public Parsed parse(String raw) {
        if (raw == null) {
            raw = "";
        }
        String[] parts = raw.trim().split(" ", 2);
        String cmd = parts[0].toLowerCase();
        String arg = (parts.length > 1) ? parts[1].trim() : "";
        return new Parsed(toType(cmd), arg, raw.trim());
    }

    /**
     * Maps a command word string to a {@link CommandType}.
     *
     * @param cmd the command word
     * @return the corresponding {@code CommandType}, or {@code UNKNOWN} if not recognized
     */
    private CommandType toType(String cmd) {
        switch (cmd) {
        case "bye":
            return CommandType.BYE;
        case "list":
            return CommandType.LIST;
        case "todo":
            return CommandType.TODO;
        case "deadline":
            return CommandType.DEADLINE;
        case "event":
            return CommandType.EVENT;
        case "mark":
            return CommandType.MARK;
        case "unmark":
            return CommandType.UNMARK;
        case "delete":
            return CommandType.DELETE;
        case "help":
            return CommandType.HELP;
        case "find":
            return CommandType.FIND;
        default:
            return CommandType.UNKNOWN;
        }
    }

    /**
     * Parses the arguments for a deadline command.
     * <p>
     * Expected format: {@code <description> /by <date>}
     *
     * @param argument the full argument string after the "deadline" keyword
     * @return a {@link DeadlineArgs} containing the description and due date
     * @throws IllegalArgumentException if the description is empty or "/by" is missing
     */
    public DeadlineArgs parseDeadlineArgs(String argument) {
        String[] parts = argument.split("/by", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("missing /by");
        }
        String desc = parts[0].trim();
        LocalDate by = parseDate(parts[1].trim());
        if (desc.isEmpty()) {
            throw new IllegalArgumentException("empty description");
        }
        return new DeadlineArgs(desc, by);
    }

    /**
     * Parses the arguments for an event command.
     * <p>
     * Expected format: {@code <description> /from <from> /to <to>}
     *
     * @param argument the full argument string after the "event" keyword
     * @return an {@link EventArgs} containing the description, start, and end times
     * @throws IllegalArgumentException if any required part is missing or blank
     */
    public EventArgs parseEventArgs(String argument) {
        String[] parts = argument.split("/from|/to");
        if (parts.length < 3) {
            throw new IllegalArgumentException("missing /from or /to");
        }
        String desc = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("blank fields");
        }
        return new EventArgs(desc, from, to);
    }

    /**
     * Parses a string index into a 0-based integer and validates bounds.
     *
     * @param argument the index string (1-based as entered by the user)
     * @param upperExclusive the exclusive upper bound of valid indices
     * @return the 0-based index
     * @throws NumberFormatException if the argument is not a number
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public int parseIndex(String argument, int upperExclusive) {
        int idx = Integer.parseInt(argument) - 1;
        if (idx < 0 || idx >= upperExclusive) {
            throw new IndexOutOfBoundsException();
        }
        return idx;
    }

    /**
     * Attempts to parse a date string in ISO or slash format.
     *
     * @param s the string to parse
     * @return the parsed {@link LocalDate}
     * @throws java.time.format.DateTimeParseException if parsing fails for both formats
     */
    private LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s, IN_ISO);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(s, IN_SLASH);
        }
    }

    /**
     * Encapsulates the result of parsing a raw input string.
     */
    public static class Parsed {
        public final CommandType type;
        public final String arg;
        public final String raw;

        public Parsed(CommandType type, String arg, String raw) {
            this.type = type;
            this.arg = arg;
            this.raw = raw;
        }
    }

    /**
     * Encapsulates the parsed arguments for a deadline command.
     */
    public static class DeadlineArgs {
        public final String desc;
        public final LocalDate by;

        public DeadlineArgs(String d, LocalDate b) {
            this.desc = d;
            this.by = b;
        }
    }

    /**
     * Encapsulates the parsed arguments for an event command.
     */
    public static class EventArgs {
        public final String desc, from, to;

        public EventArgs(String d, String f, String t) {
            this.desc = d;
            this.from = f;
            this.to = t;
        }
    }
}
