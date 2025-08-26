package lazysourcea.ui;

import lazysourcea.task.Task;
import lazysourcea.task.TaskList;

import java.util.Scanner;

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

    public void showWelcome() {
        System.out.println("Hello from\n" + LOGO);
        System.out.println("-----------------------");
        System.out.println("hi. i'm lazysourcea.lazysourcea.\nwhat do you want to do?");
        System.out.println("-----------------------");
        System.out.println("enter help for available commands");
    }

    public void showBye() {
        System.out.print("bye.");
    }

    public void prompt() {
        System.out.print(">>> ");
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        System.out.println(message);
    }

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

    public void showAdded(Task task, int size) {
        System.out.println("ok. task added:\n  " + task);
        System.out.println("now you have " + size + " task(s) in the list.");
    }

    public void showDeleted(Task task, int size) {
        System.out.println("task:");
        System.out.println("  " + task + "\nremoved.");
        System.out.println("now you have " + size + " tasks in the list.");
    }

    public void showMarked(Task task) {
        System.out.println("task marked as done:\n " + task);
    }

    public void showUnmarked(Task task) {
        System.out.println("task unmarked:\n " + task);
    }

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
        for (int i = 0; i < taskList.size(); i++) {
            Task t = taskList.get(i);
            if (t.toString().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(index + "." + t);
                index++;
            }
        }
        if (index == 1) {
            System.out.println("(no matching tasks found)");
        }
    }


    public void showUnknownOrEmpty(String raw) {
        if (!raw.isEmpty()) {
            System.out.println("tsk what u saying. i don't understand");
        } else {
            System.out.println("oi.. enter something leh");
        }
    }
}
