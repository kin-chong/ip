package lazysourcea.ui;

import lazysourcea.task.Task;
import lazysourcea.task.TaskList;

import java.util.Scanner;
import java.util.function.Consumer;

public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    // NEW: output sink
    private final Consumer<String> out;

    // Keep console behavior by default
    public Ui() {
        this.out = System.out::println;
    }

    // NEW: allow callers to capture output anywhere (JavaFX, tests, etc.)
    public Ui(Consumer<String> out) {
        this.out = out == null ? System.out::println : out;
    }

    private static final String LOGO = """
             _
            | | __ _ _____   _ ___  ___  _   _ _ __ ___ ___  __ _
            | |/ _` |_  / | | / __|/ _ \\| | | | '__/ __/ _ \\/ _` |
            | | (_| |/ /| |_| \\__ \\ (_) | |_| | | | (_|  __/ (_| |
            |_|\\__,_/___|\\__, |___/\\___/ \\__,_|_|  \\___\\___|\\__,_|
                         |___/
            """;

    public void showWelcome() {
        //out.accept("Hello from\n" + LOGO);
        //out.accept("-----------------------");
        out.accept("hi. i'm lazysourcea.\nwhat do you want to do?");
        //out.accept("-----------------------");
        out.accept("enter help for available commands");
    }

    public void showBye() {
        out.accept("bye.");
    }

    public void prompt() {
        // For console prompt, you had System.out.print. In sink mode we’ll just emit a line.
        out.accept(">>> ");
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        out.accept(message);
    }

    public void showHelp() {
        out.accept("list:           shows your tasklist");
        out.accept("todo:           adds a todo task. use: todo <desc>");
        out.accept("deadline:       adds a deadline task. use: deadline <desc> /by <date>");
        out.accept("event:          adds an event. use: event <desc> /from <time> /to <time>");
        out.accept("mark:           marks a task. use: mark <number>");
        out.accept("unmark:         unmarks a task. use: unmark <number>");
        out.accept("delete:         deletes a task. use: delete <number>");
        out.accept("bye:            exits the program");
    }

    public void showAdded(Task task, int size) {
        out.accept("ok. task added:\n  " + task);
        out.accept("now you have " + size + " task(s) in the list.");
    }

    public void showDeleted(Task task, int size) {
        out.accept("task:");
        out.accept("  " + task + "\nremoved.");
        out.accept("now you have " + size + " tasks in the list.");
    }

    public void showMarked(Task task) {
        out.accept("task marked as done:\n " + task);
    }

    public void showUnmarked(Task task) {
        out.accept("task unmarked:\n " + task);
    }

    public void showList(TaskList taskList) {
        taskList.listTasks(); // keep as-is if this already prints via System.out
        // If you want this to also funnel through the sink, change TaskList to call a Consumer too.
    }

    public void showFindResults(TaskList taskList, String keyword) {
        out.accept("ok found matches:");
        int index = 1;
        for (int i = 0; i < taskList.listSize(); i++) {
            var t = taskList.getTask(i);
            if (t.toString().toLowerCase().contains(keyword.toLowerCase())) {
                out.accept(index + "." + t);
                index++;
            }
        }
        if (index == 1) {
            out.accept("(no matching tasks found)");
        }
    }

    public void showUnknownOrEmpty(String raw) {
        if (!raw.isEmpty()) {
            out.accept("tsk what u saying. i don't understand");
        } else {
            out.accept("oi.. enter something leh");
        }
    }
}
