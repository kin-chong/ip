package lazysourcea.task;

/**
 * Represents a generic task in the task list.
 * <p>
 * A task has a textual description and a completion status
 * (done or not done). This class is meant to be extended by
 * specific task types such as {@link Todo}, {@link Deadline},
 * and {@link Event}.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new {@code Task} with the given description.
     * By default, a task is not done.
     *
     * @param description textual description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon for this task.
     *
     * @return {@code "X"} if the task is done, otherwise a single space
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markNotDone() {
        this.isDone = false;
    }

    /**
     * Returns a string representation of this task in a format
     * suitable for saving to storage.
     * <p>
     * Subclasses override this to include their specific type
     * and additional information.
     *
     * @return data string in the format {@code ? | 0/1 | description}
     */
    public String toDataString() {
        return "? | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns a string representation of this task for display to the user.
     *
     * @return string in the format {@code [ ] description} or {@code [X] description}
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
