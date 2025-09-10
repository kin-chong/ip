package lazysourcea.ui;

import lazysourcea.task.Task;
import lazysourcea.task.TaskList;

import java.util.Scanner;
import java.util.function.Consumer;

public class Ui {
    private static final int LEFT_W  = 40;  // width for the command column
    private static final int TOTAL_W = 100;  // overall text width (monospace)
    private static final int RIGHT_W = TOTAL_W - LEFT_W - 1; // -1 for the space between cols

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

    /**
     * Shows the welcome message.
     */
    public void showWelcome() {
        //out.accept("Hello from\n" + LOGO);
        //out.accept("-----------------------");
        out.accept("hi. i'm lazysourcea.\nwhat do you want to do?");
        //out.accept("-----------------------");
        out.accept("enter help for available commands");
    }

    /**
     * Show the bye message.
     */
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

    /**
     * Shows the error message for any wrongly formatted user input.
     * @param message the specific error message to display
     */
    public void showError(String message) {
        assert message != null : "error message null";
        out.accept(message);
    }

    /**
     * Shows a general help menu.
     */
    public void showHelp() {
        out.accept("lazysourcea — Commands");
        out.accept("");

        out.accept("BASICS");
        row("- help [command]", "Show general help or details for a command");
        row("- list",           "Show all tasks");
        row("- find <keyword>", "Search tasks by keyword (case-insensitive)");
        row("- bye",            "Exit the program");
        out.accept("");

        out.accept("ADD TASKS");
        row("- todo <desc>", "Add a todo");
        row("- deadline <desc> /by <date>", "Add a deadline");
        row("- event <desc> /from <time> /to <time>", "Add an event");
        line("-- Date formats: yyyy-MM-dd  or  d/M/yyyy (e.g., 2019-10-15 or 2/12/2019)");
        out.accept("");

        out.accept("MANAGE TASKS");
        row("- mark <n>",   "Mark task n as done");
        row("- unmark <n>", "Mark task n as not done");
        row("- delete <n>", "Delete task n");
        out.accept("");

        out.accept("Tip: type 'help <command>' for examples, e.g., 'help deadline'.");
    }

    /**
     * Shows a more detailed help menu when a command is entered after help.
     * @param command the command for specific help
     */
    public void showHelp(String command) {
        if (command == null || command.isBlank()) { showHelp(); return; }
        switch (command.trim().toLowerCase()) {
            case "help":
                out.accept("help — show help");
                out.accept("Usage: help [command]");
                out.accept("Examples:");
                out.accept("  help");
                out.accept("  help deadline");
                break;
            case "list":
                out.accept("list — show all tasks");
                out.accept("Usage: list");
                break;
            case "find":
                out.accept("find — search tasks by keyword");
                out.accept("Usage: find <keyword>");
                out.accept("Examples:");
                out.accept("  find book");
                out.accept("  find meeting");
                break;
            case "todo":
                out.accept("todo — add a todo task");
                out.accept("Usage: todo <desc>");
                out.accept("Example: todo read book");
                break;
            case "deadline":
                out.accept("deadline — add a task with a due date");
                out.accept("Usage: deadline <desc> /by <date>");
                out.accept("Date formats: yyyy-MM-dd  or  d/M/yyyy");
                out.accept("Examples:");
                out.accept("  deadline return book /by 2019-10-15");
                out.accept("  deadline CS2103 iP v1 /by 2/12/2019");
                break;
            case "event":
                out.accept("event — add a task with a start and end time");
                out.accept("Usage: event <desc> /from <time> /to <time>");
                out.accept("Examples:");
                out.accept("  event project meeting /from 10:00 /to 12:00");
                out.accept("  event camp /from 2019-12-01 /to 2019-12-03");
                break;
            case "mark":
                out.accept("mark — mark a task as done");
                out.accept("Usage: mark <n>");
                out.accept("Example: mark 2");
                break;
            case "unmark":
                out.accept("unmark — mark a task as not done");
                out.accept("Usage: unmark <n>");
                out.accept("Example: unmark 2");
                break;
            case "delete":
                out.accept("delete — remove a task");
                out.accept("Usage: delete <n>");
                out.accept("Example: delete 3");
                break;
            case "bye":
                out.accept("bye — exit the program");
                out.accept("Usage: bye");
                break;
            default:
                out.accept("Unknown command: " + command);
                out.accept("Type 'help' to see available commands.");
        }
    }


    /**
     * Shows the task added.
     * @param task the task that was added to the list
     * @param size the size of the task list after addition
     */
    public void showAdded(Task task, int size) {
        assert size >= 0 : "size negative";
        out.accept("ok. task added:\n  " + task);
        out.accept("now you have " + size + " task(s) in the list.");
    }

    /**
     * Shows the task removed.
     * @param task the task that was removed from the list
     * @param size the size of the task list after deletion
     */
    public void showDeleted(Task task, int size) {
        out.accept("task:");
        out.accept("  " + task + "\nremoved.");
        out.accept("now you have " + size + " tasks in the list.");
    }

    /**
     * Shows the marked task.
     * @param task the task to be marked
     */
    public void showMarked(Task task) {
        out.accept("task marked as done:\n " + task);
    }

    /**
     * Shows the unmarked task.
     * @param task the task to be unmarked
     */
    public void showUnmarked(Task task) {
        out.accept("task unmarked:\n " + task);
    }

    public void showList(TaskList taskList) {
        taskList.listTasks();
    }

    /**
     * Shows the tasks that matches the keyword entered by the user.
     * @param taskList the list of tasks
     * @param keyword the keyword to be matched to the tasks
     */
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

    /**
     * Shows the message if the user input is either empty or unknown.
     * @param raw the user input
     */
    public void showUnknownOrEmpty(String raw) {
        if (!raw.isEmpty()) {
            out.accept("tsk what u saying. i don't understand");
        } else {
            out.accept("oi.. enter something leh");
        }
    }

    /**
     * Wraps the given text into lines whose length does not exceed {@code width}.
     * <p>
     * The algorithm prefers breaking at the last space before the limit; if no
     * space exists in range, it hard-wraps at the limit. Leading/trailing spaces
     * are trimmed and any spaces at the wrap boundary are skipped so no line ends
     * with whitespace. Returns at least one element; for {@code null} or blank
     * input this is a single empty string.
     *
     * @param s     the text to wrap (may be {@code null})
     * @param width the maximum line width (in characters), expected {@code >= 1}
     * @return a list of wrapped lines in display order, each {@code <= width}
     */
    private static java.util.List<String> wrapToWidth(String s, int width) {
        java.util.ArrayList<String> lines = new java.util.ArrayList<>();
        if (s == null) { lines.add(""); return lines; }
        s = s.trim();
        if (s.isEmpty()) { lines.add(""); return lines; }

        int i = 0, n = s.length();
        while (i < n) {
            int end = Math.min(i + width, n);
            if (end < n) {
                int sp = s.lastIndexOf(' ', end);
                if (sp <= i) sp = end; // no space found; hard wrap
                end = sp;
            }
            lines.add(s.substring(i, end).trim());
            i = end;
            while (i < n && s.charAt(i) == ' ') i++; // skip spaces at wrap
        }
        return lines;
    }


    /**
     * Emit a two-column row with wrapping and alignment.
     * @param left left column text
     * @param right right column text
     */
    private void row(String left, String right) {
        var L = wrapToWidth(left, LEFT_W);
        var R = wrapToWidth(right, RIGHT_W);
        int rows = Math.max(L.size(), R.size());
        for (int i = 0; i < rows; i++) {
            String l = (i < L.size()) ? L.get(i) : "";
            String r = (i < R.size()) ? R.get(i) : "";
            out.accept(String.format("%-" + LEFT_W + "s %s", l, r));
        }
    }

    /**
     * Emit a full-width line.
     * @param text text to print
     */
    private void line(String text) {
        for (String l : wrapToWidth(text, TOTAL_W)) {
            out.accept(l);
        }
    }
}
