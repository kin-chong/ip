import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private final LocalDate by;
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    public LocalDate getBy() {
        return by;
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.toString();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUT) + ")";
    }
}
