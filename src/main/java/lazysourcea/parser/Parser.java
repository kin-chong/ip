package lazysourcea.parser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public enum CommandType {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, HELP, UNKNOWN
    }

    private static final DateTimeFormatter IN_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter IN_SLASH = DateTimeFormatter.ofPattern("d/M/yyyy");

    public Parsed parse(String raw) {
        if (raw == null) {
            raw = "";
        }
        String[] parts = raw.trim().split(" ", 2);
        String cmd = parts[0].toLowerCase();
        String arg = (parts.length > 1) ? parts[1].trim() : "";
        return new Parsed(toType(cmd), arg, raw.trim());
    }

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
        default:
            return CommandType.UNKNOWN;
        }
    }

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

    public int parseIndex(String argument, int upperExclusive) {
        int idx = Integer.parseInt(argument) - 1;
        if (idx < 0 || idx >= upperExclusive) {
            throw new IndexOutOfBoundsException();
        }
        return idx;
    }

    private LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s, IN_ISO);
        } catch (DateTimeParseException ignored) {
            return LocalDate.parse(s, IN_SLASH);
        }
    }

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

    public static class DeadlineArgs {
        public final String desc;
        public final LocalDate by;

        public DeadlineArgs(String d, LocalDate b) {
            this.desc = d;
            this.by = b;
        }
    }

    public static class EventArgs {
        public final String desc, from, to;

        public EventArgs(String d, String f, String t) {
            this.desc = d;
            this.from = f;
            this.to = t;
        }
    }
}
