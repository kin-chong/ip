package lazysourcea.task;

/**
 * Represents a simple {@link Task} without any date or time attached.
 * <p>
 * A todo task only contains a description and its completion status.
 */
public class Todo extends Task {

    /**
     * Creates a new {@code Todo} with the given description.
     * The task is initially not marked as done.
     *
     * @param description description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this task suitable for saving.
     * <p>
     * Format: {@code T | 0/1 | description}
     *
     * @return the serialized form of this todo task
     */
    @Override
    public String toDataString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns a string representation of this task for user display.
     * <p>
     * Format: {@code [T][ ] description} or {@code [T][X] description}
     *
     * @return a human-readable string for this todo task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
