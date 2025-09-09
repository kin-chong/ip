package lazysourcea;

import java.util.ArrayList;

import lazysourcea.parser.Parser;
import lazysourcea.storage.Storage;
import lazysourcea.task.Deadline;
import lazysourcea.task.Event;
import lazysourcea.task.Task;
import lazysourcea.task.TaskList;
import lazysourcea.task.Todo;
import lazysourcea.ui.Ui;

/**
 * Core engine for Lazysourcea: UI-agnostic and single-input driven, it loads and saves tasks via
 * {@code Storage}, holds the in-memory {@code TaskList}, parses commands with {@code Parser}, and
 * returns user-facing text for each call to {@link #getResponse(String)} (no printing or loops);
 * {@link #getWelcomeMessage()} yields the startup greeting, and {@link #isExit()} flips to {@code true}
 * after a "bye" command so the caller (e.g., JavaFX) can close the app.
 */
public class lazysourcea {
    // --- fields for shared core state ---
    private final Storage storage;
    private final TaskList taskList;
    private final Parser parser;
    private boolean isExit = false;

    /*
    public static void main(String[] args) {
        // Your original console loop (unchanged)
        Storage storage = new Storage("data", "lazysourcea.txt");
        TaskList taskList = new TaskList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        ArrayList<Task> loaded = storage.load();
        for (Task t : loaded) {
            taskList.addTask(t);
        }

        ui.showWelcome();
        boolean running = true;

        while (running) {
            ui.prompt();
            Parser.Parsed parsed = parser.parse(ui.readCommand());

            switch (parsed.type) {
                case BYE:
                    ui.showBye();
                    running = false;
                    break;

                case LIST:
                    ui.showList(taskList);
                    break;

                case MARK:
                    try {
                        int index = parser.parseIndex(parsed.arg, taskList.listSize());
                        Task t = taskList.getTask(index);
                        t.isDone();
                        ui.showMarked(t);
                        storage.save(taskList.asList());
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        ui.showError("invalid task number. use: mark <number>");
                    }
                    break;

                case UNMARK:
                    try {
                        int index = parser.parseIndex(parsed.arg, taskList.listSize());
                        Task t = taskList.getTask(index);
                        t.isNotDone();
                        ui.showUnmarked(t);
                        storage.save(taskList.asList());
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        ui.showError("invalid task number. use: unmark <number>");
                    }
                    break;

                case TODO:
                    if (parsed.arg.isEmpty()) {
                        ui.showError("tsk.. todo description cannot empty.\nuse: todo <desc>");
                    } else {
                        Task todo = new Todo(parsed.arg);
                        taskList.addTask(todo);
                        ui.showAdded(todo, taskList.listSize());
                        storage.save(taskList.asList());
                    }
                    break;

                case DEADLINE:
                    try {
                        Parser.DeadlineArgs d = parser.parseDeadlineArgs(parsed.arg);
                        Task deadline = new Deadline(d.desc, d.by);
                        taskList.addTask(deadline);
                        ui.showAdded(deadline, taskList.listSize());
                        storage.save(taskList.asList());
                    } catch (Exception e) {
                        ui.showError("oi.. invalid deadline format.\nuse: deadline <desc> /by <time>"
                                + "\naccepted: yyyy-MM-dd (e.g., 2019-10-15) or d/M/yyyy (e.g., 2/12/2019)");
                    }
                    break;

                case EVENT:
                    try {
                        Parser.EventArgs ev = parser.parseEventArgs(parsed.arg);
                        Task event = new Event(ev.desc, ev.from, ev.to);
                        taskList.addTask(event);
                        ui.showAdded(event, taskList.listSize());
                        storage.save(taskList.asList());
                    } catch (Exception e) {
                        ui.showError("oi.. invalid event format.\nuse: event <desc> /from <time> /to <time>");
                    }
                    break;

                case DELETE:
                    try {
                        int index = parser.parseIndex(parsed.arg, taskList.listSize());
                        Task removedTask = taskList.removeTask(index);
                        ui.showDeleted(removedTask, taskList.listSize());
                        storage.save(taskList.asList());
                    } catch (NumberFormatException e) {
                        ui.showError("oi.. give valid task number pls.\nUsage: delete <number>");
                    } catch (IndexOutOfBoundsException e) {
                        ui.showError("task number out of range lah.");
                    }
                    break;

                case FIND:
                    if (parsed.arg.isEmpty()) {
                        ui.showError("usage: find <keyword>");
                    } else {
                        ui.showFindResults(taskList, parsed.arg);
                    }
                    break;

                case HELP:
                    ui.showHelp();
                    break;

                default:
                    ui.showUnknownOrEmpty(parsed.raw);
            }
        }
    }
    */

    // --- JavaFX-friendly constructor (no console I/O) ---
    public lazysourcea(String dataDir, String fileName) {
        this.storage = new Storage(dataDir, fileName);
        this.taskList = new TaskList();
        this.parser = new Parser();

        ArrayList<Task> loaded = storage.load();
        for (Task t : loaded) {
            taskList.addTask(t);
        }
    }

    /**
     * Process a single user input and return a single string reply.
     * No printing, no blocking loop.
     */
    public String getResponse(String input) {
        Parser.Parsed parsed = parser.parse(input);

        StringBuilder sb = new StringBuilder();
        Ui ui = new Ui(line -> {
            if (line != null) sb.append(line).append(System.lineSeparator());
        });

        switch (parsed.type) {
        case BYE:
            ui.showBye();
            this.isExit = true;
            break;
        case LIST:
            taskList.listTasks(line -> sb.append(line).append('\n'));
            break;
        case MARK:
            try {
                int index = parser.parseIndex(parsed.arg, taskList.listSize());
                assert index >= 0 && index < taskList.listSize() : "index out of bounds after parseIndex";
                Task t = taskList.getTask(index);
                t.markDone();
                storage.save(taskList.asList());
                ui.showMarked(t);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                ui.showError("invalid task number. use: mark <number>");
            }
            break;
        case UNMARK:
            try {
                int index = parser.parseIndex(parsed.arg, taskList.listSize());
                assert index >= 0 && index < taskList.listSize() : "index out of bounds after parseIndex";
                Task t = taskList.getTask(index);
                t.markNotDone();
                storage.save(taskList.asList());
                ui.showUnmarked(t);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                ui.showError("invalid task number. use: unmark <number>");
            }
            break;
        case TODO:
            if (parsed.arg.isEmpty()) {
                ui.showError("tsk.. todo description cannot empty.\nuse: todo <desc>");
            } else {
                Task todo = new Todo(parsed.arg);
                taskList.addTask(todo);
                storage.save(taskList.asList());
                ui.showAdded(todo, taskList.listSize());
            }
            break;
        case DEADLINE:
            try {
                Parser.DeadlineArgs d = parser.parseDeadlineArgs(parsed.arg);
                Task deadline = new Deadline(d.desc, d.by);
                taskList.addTask(deadline);
                storage.save(taskList.asList());
                ui.showAdded(deadline, taskList.listSize());
            } catch (Exception e) {
                ui.showError("oi.. invalid deadline format.\nuse: deadline <desc> /by <time>"
                        + "\naccepted: yyyy-MM-dd (e.g., 2019-10-15) or d/M/yyyy (e.g., 2/12/2019)");
            }
            break;
        case EVENT:
            try {
                Parser.EventArgs ev = parser.parseEventArgs(parsed.arg);
                Task event = new Event(ev.desc, ev.from, ev.to);
                taskList.addTask(event);
                storage.save(taskList.asList());
                ui.showAdded(event, taskList.listSize());
            } catch (Exception e) {
                ui.showError("oi.. invalid event format.\nuse: event <desc> /from <time> /to <time>");
            }
            break;
        case DELETE:
            try {
                int index = parser.parseIndex(parsed.arg, taskList.listSize());
                Task removed = taskList.removeTask(index);
                storage.save(taskList.asList());
                ui.showDeleted(removed, taskList.listSize());
            } catch (NumberFormatException e) {
                ui.showError("oi.. give valid task number pls.\nUsage: delete <number>");
            } catch (IndexOutOfBoundsException e) {
                ui.showError("task number out of range lah.");
            }
            break;
        case FIND:
            if (parsed.arg.isEmpty()) {
                ui.showError("usage: find <keyword>");
            } else {
                ui.showFindResults(taskList, parsed.arg);
            }
            break;
        case HELP:
            ui.showHelp();
            break;
        default:
            ui.showUnknownOrEmpty(parsed.raw == null ? "" : parsed.raw);
        }

        return sb.toString().trim();
    }


    /** For JavaFX to check if it should close the window after showing the reply. */
    public boolean isExit() {
        return isExit;
    }

    public String getWelcomeMessage() {
        var sb = new StringBuilder();
        var ui = new Ui(line -> { if (line != null) sb.append(line).append(System.lineSeparator()); });
        ui.showWelcome();
        return sb.toString().trim();
    }

}
