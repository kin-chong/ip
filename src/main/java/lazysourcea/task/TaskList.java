package lazysourcea.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a mutable list of {@link Task} objects.
 * <p>
 * Provides methods to add, remove, retrieve, and display tasks.
 * The internal list is encapsulated and cannot be modified directly
 * outside of this class.
 */
public class TaskList {
    private final ArrayList<Task> items;

    /**
     * Creates a new empty {@code TaskList}.
     */
    public TaskList() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     *
     * @param task the {@link Task} to be added
     */
    public void addTask(Task task) {
        items.add(task);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the size of the task list
     */
    public int listSize() {
        return items.size();
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index zero-based index of the task to retrieve
     * @return the {@link Task} at the specified index
     * @throws IndexOutOfBoundsException if {@code index} is invalid
     */
    public Task getTask(int index) throws IndexOutOfBoundsException {
        return items.get(index);
    }

    /**
     * Removes the task at the specified index.
     *
     * @param index zero-based index of the task to remove
     * @return the removed {@link Task}
     * @throws IndexOutOfBoundsException if {@code index} is invalid
     */
    public Task removeTask(int index) throws IndexOutOfBoundsException {
        return items.remove(index);
    }

    /**
     * Returns an unmodifiable view of the current task list.
     * <p>
     * This ensures external code cannot mutate the internal list
     * while still being able to iterate over the tasks.
     *
     * @return an unmodifiable {@link List} of {@link Task} objects
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Prints all tasks in the list to standard output.
     * <p>
     * If the list is empty, a placeholder message is shown.
     */
    public void listTasks() {
        if (items.isEmpty()) {
            System.out.println("(no tasks yet)");
        } else {
            System.out.println("your tasks:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + "." + items.get(i));
            }
        }
    }
}
