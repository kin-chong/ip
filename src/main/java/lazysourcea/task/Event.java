package lazysourcea.task;

/**
 * Represents a {@link Task} that spans a period of time.
 * <p>
 * An event task has a description, a start time, and an end time.
 * Unlike {@link Deadline}, which has a single due date, an event
 * specifies both the beginning and end of the activity.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates a new {@code Event} with the given description and time range.
     *
     * @param description description of the event
     * @param from the start time or date of the event
     * @param to the end time or date of the event
     */

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a string representation of this task suitable for saving.
     * <p>
     * Format: {@code E | 0/1 | description | from | to}
     *
     * @return the serialized form of this event task
     */
    @Override
    public String toDataString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    /**
     * Returns a string representation of this task for user display.
     * <p>
     * Format: {@code [E][ ] description (from: <from> to: <to>)}
     *
     * @return a human-readable string for this event task
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
