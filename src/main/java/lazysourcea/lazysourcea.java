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
 * Application entry point for Lazysourcea.
 *
 * <p>Initializes the storage, task list, UI and parser, loads previously saved tasks,
 * and runs the interactive command loop. Supported commands include:
 * list, todo, deadline, event, mark, unmark, delete, find, help and bye.
 *
 * <p>Mutating commands persist changes to storage and UI is used for all user-facing output
 * and error messages.
 */
public class lazysourcea {
    public static void main(String[] args) {
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
                running = false; // exit loop
                break;

            case LIST:
                // call taskList class to list tasks
                ui.showList(taskList);
                break;

            case MARK:
                // mark task as done
                try {
                    int index = parser.parseIndex(parsed.arg, taskList.listSize());
                    Task t = taskList.getTask(index); // may throw IndexOutOfBoundsException
                    t.isDone();
                    ui.showMarked(t);
                    storage.save(taskList.asList());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    ui.showError("invalid task number. use: mark <number>");
                }
                break;

            case UNMARK:
                // unmark a previously marked task
                try {
                    int index = parser.parseIndex(parsed.arg, taskList.listSize());
                    Task t = taskList.getTask(index); // may throw IndexOutOfBoundsException
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
}
