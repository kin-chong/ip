package lazysourcea.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a {@link Task} with a deadline.
 * <p>
 * A deadline task has a description and a due date,
 * stored as a {@link LocalDate}. The date is displayed
 * in a human-friendly format (e.g., {@code Dec 2 2019}),
 * but saved in ISO-8601 format ({@code yyyy-MM-dd}).
 */
public class Deadline extends Task {
    private final LocalDate by;
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Creates a new {@code Deadline} with the given description and due date.
     *
     * @param description description of the task
     * @param by the due date of the task as a {@link LocalDate}
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date of this deadline task.
     *
     * @return the due date as a {@link LocalDate}
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns a string representation of this task suitable for saving.
     * <p>
     * Format: {@code D | 0/1 | description | yyyy-MM-dd}
     *
     * @return the serialized form of this deadline task
     */
    @Override
    public String toDataString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.toString();
    }

    /**
     * Returns a string representation of this task for user display.
     * <p>
     * Format: {@code [D][ ] description (by: Dec 2 2019)}
     *
     * @return a human-readable string for this deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUT) + ")";
    }
}
