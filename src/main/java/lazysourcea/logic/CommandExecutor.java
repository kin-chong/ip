package lazysourcea.logic;

import java.util.function.Consumer;

import lazysourcea.parser.Parser;
import lazysourcea.storage.Storage;
import lazysourcea.task.*;
import lazysourcea.ui.Ui;

/**
 * Executes a single parsed command against the current application state.
 * <p>
 * This class is UI-agnostic: it formats user-facing output via {@link Ui},
 * which writes to a provided {@code Consumer<String>} sink. It may mutate
 * {@link TaskList} and persist changes through {@link Storage}. Use the
 * boolean return of {@link #execute(Parser.Parsed, Consumer)} to detect
 * an exit request (i.e., {@code bye}).
 */
public class CommandExecutor {
    private final TaskList taskList;
    private final Storage storage;
    private final Parser parser;
    private final Ui ui;

    /**
     * Creates a command executor that writes user-visible lines to the given
     * output sink.
     *
     * @param taskList the in-memory task list to read/mutate
     * @param storage  the storage used to persist mutations
     * @param parser   helper for parsing indices and sub-arguments
     * @param out      sink that receives formatted output lines
     *                 (e.g., collected for JavaFX rendering)
     */
    public CommandExecutor(TaskList taskList, Storage storage, Parser parser, Consumer<String> out) {
        this.taskList = taskList;
        this.storage = storage;
        this.parser = parser;
        this.ui = new Ui(out);
    }

    /**
     * Executes one parsed command, emitting formatted lines to the configured
     * output sink. Mutating commands save the task list to storage. Errors are
     * reported as human-friendly messages; exceptions are handled internally.
     *
     * @param parsed     the already-parsed command and its argument(s)
     * @param outForList sink to receive each line of {@code list} output
     *                   (forwarded to {@link TaskList#listTasks(Consumer)})
     * @return {@code true} if the command requests program exit (BYE),
     *         {@code false} otherwise
     */
    public boolean execute(Parser.Parsed parsed, Consumer<String> outForList) {
        switch (parsed.type) {
            case BYE:
                ui.showBye();
                return true;
            case LIST:
                taskList.listTasks(outForList);
                return false;
            case MARK:
                try {
                    int index = parser.parseIndex(parsed.arg, taskList.listSize());
                    Task t = taskList.getTask(index);
                    t.markDone();
                    storage.save(taskList.asList());
                    ui.showMarked(t);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    ui.showError("invalid task number. use: mark <number>");
                }
                return false;
            case UNMARK:
                try {
                    int index = parser.parseIndex(parsed.arg, taskList.listSize());
                    Task t = taskList.getTask(index);
                    t.markNotDone();
                    storage.save(taskList.asList());
                    ui.showUnmarked(t);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    ui.showError("invalid task number. use: unmark <number>");
                }
                return false;
            case TODO:
                if (parsed.arg.isEmpty()) {
                    ui.showError("tsk.. todo description cannot empty.\nuse: todo <desc>");
                } else {
                    Task todo = new Todo(parsed.arg);
                    taskList.addTask(todo);
                    storage.save(taskList.asList());
                    ui.showAdded(todo, taskList.listSize());
                }
                return false;
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
                return false;
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
                return false;
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
                return false;
            case FIND:
                if (parsed.arg.isEmpty()) {
                    ui.showError("usage: find <keyword>");
                } else {
                    ui.showFindResults(taskList, parsed.arg);
                }
                return false;
            case HELP:
                ui.showHelp();
                return false;
            default:
                ui.showUnknownOrEmpty(parsed.raw == null ? "" : parsed.raw);
                return false;
        }
    }
}
