package lazysourcea.ui;

import lazysourcea.task.Task;
import lazysourcea.task.TaskList;

import java.util.Scanner;

/**
 * Handles all user interactions with the chatbot.
 * <p>
 * The {@code Ui} class is responsible for displaying messages,
 * reading user input, and printing feedback for commands such
 * as adding, deleting, marking, or listing tasks.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    private static final String LOGO = """
             _
            | | __ _ _____   _ ___  ___  _   _ _ __ ___ ___  __ _
            | |/ _` |_  / | | / __|/ _ \\| | | | '__/ __/ _ \\/ _` |
            | | (_| |/ /| |_| \\__ \\ (_) | |_| | | | (_|  __/ (_| |
            |_|\\__,_/___|\\__, |___/\\___/ \\__,_|_|  \\___\\___|\\__,_|
                         |___/
            """;

    /**
     * Prints the welcome message, including the logo and basic instructions.
     */
    public void showWelcome() {
        System.out.println("Hello from\n" + LOGO);
        System.out.println("-----------------------");
        System.out.println("hi. i'm lazysourcea.lazysourcea.\nwhat do you want to do?");
        System.out.println("-----------------------");
        System.out.println("enter help for available commands");
    }

    /**
     * Prints the farewell message when exiting the chatbot.
     */
    public void showBye() {
        System.out.print("bye.");
    }

    /**
     * Prints the command-line prompt symbol.
     */
    public void prompt() {
        System.out.print(">>> ");
    }

    /**
     * Reads a command entered by the user.
     *
     * @return the trimmed command string
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints an error message to the user.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Prints a list of available commands and their usage.
     */
    public void showHelp() {
        System.out.println("list:           shows your tasklist");
        System.out.println("todo:           adds a todo task. use: todo <desc>");
        System.out.println("deadline:       adds a deadline task. use: deadline <desc> /by <date>");
        System.out.println("event:          adds an event. use: event <desc> /from <time> /to <time>");
        System.out.println("mark:           marks a task. use: mark <number>");
        System.out.println("unmark:         unmarks a task. use: unmark <number>");
        System.out.println("delete:         deletes a task. use: delete <number>");
        System.out.println("bye:            exits the program");
    }

    /**
     * Prints a confirmation message when a task is added.
     *
     * @param task the task that was added
     * @param size the new size of the task list
     */
    public void showAdded(Task task, int size) {
        System.out.println("ok. task added:\n  " + task);
        System.out.println("now you have " + size + " task(s) in the list.");
    }

    /**
     * Prints a confirmation message when a task is deleted.
     *
     * @param task the task that was removed
     * @param size the new size of the task list
     */
    public void showDeleted(Task task, int size) {
        System.out.println("task:");
        System.out.println("  " + task + "\nremoved.");
        System.out.println("now you have " + size + " tasks in the list.");
    }

    /**
     * Prints a confirmation message when a task is marked as done.
     *
     * @param task the task that was marked
     */
    public void showMarked(Task task) {
        System.out.println("task marked as done:\n " + task);
    }

    /**
     * Prints a confirmation message when a task is unmarked.
     *
     * @param task the task that was unmarked
     */
    public void showUnmarked(Task task) {
        System.out.println("task unmarked:\n " + task);
    }

    /**
     * Prints the entire list of tasks.
     *
     * @param taskList the list of tasks to display
     */
    public void showList(TaskList taskList) {
        taskList.listTasks();
    }

    /**
     * Displays all tasks whose description contains the given keyword.
     * <p>
     * The search is case-insensitive and matches any substring within
     * the task's {@code toString()} representation. Results are shown
     * in a numbered list, similar to {@link #showList(lazysourcea.task.TaskList)}.
     * <p>
     * If no tasks match the keyword, a placeholder message is printed.
     *
     * @param taskList the {@link lazysourcea.task.TaskList} to search
     * @param keyword  the search keyword to look for in task descriptions
     */
    public void showFindResults(TaskList taskList, String keyword) {
        System.out.println("ok found matches:");
        int index = 1;
        for (int i = 0; i < taskList.listSize(); i++) {
            Task t = taskList.getTask(i);
            if (t.toString().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(index + "." + t);
                index++;
            }
        }
        if (index == 1) {
            System.out.println("(no matching tasks found)");
        }
    }


    /**
     * Prints a message when the input is empty or not understood.
     *
     * @param raw the raw user input
     */
    public void showUnknownOrEmpty(String raw) {
        if (!raw.isEmpty()) {
            System.out.println("tsk what u saying. i don't understand");
        } else {
            System.out.println("oi.. enter something leh");
        }
    }
}
