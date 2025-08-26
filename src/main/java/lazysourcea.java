import java.util.ArrayList;

public class lazysourcea {
    public static void main(String[] args) {
        Storage storage = new Storage("data", "lazysourcea.txt");
        TaskList taskList = new TaskList();
        Ui ui = new Ui();
        Parser parser = new Parser();

        ArrayList<Task> loaded = storage.load();
        for (Task t : loaded) {
            taskList.add(t);
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
                    int index = parser.parseIndex(parsed.arg, taskList.size());
                    Task t = taskList.get(index); // may throw IndexOutOfBoundsException
                    t.markAsDone();
                    ui.showMarked(t);
                    storage.save(taskList.asList());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    ui.showError("invalid task number. use: mark <number>");
                }
                break;

            case UNMARK:
                // unmark a previously marked task
                try {
                    int index = parser.parseIndex(parsed.arg, taskList.size());
                    Task t = taskList.get(index); // may throw IndexOutOfBoundsException
                    t.markAsNotDone();
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
                    taskList.add(todo);
                    ui.showAdded(todo, taskList.size());
                    storage.save(taskList.asList());
                }
                break;

            case DEADLINE:
                try {
                    Parser.DeadlineArgs d = parser.parseDeadlineArgs(parsed.arg);
                    Task deadline = new Deadline(d.desc, d.by);
                    taskList.add(deadline);
                    ui.showAdded(deadline, taskList.size());
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
                    taskList.add(event);
                    ui.showAdded(event, taskList.size());
                    storage.save(taskList.asList());
                } catch (Exception e) {
                    ui.showError("oi.. invalid event format.\nuse: event <desc> /from <time> /to <time>");
                }
                break;

            case DELETE:
                try {
                    int index = parser.parseIndex(parsed.arg, taskList.size());
                    Task removedTask = taskList.remove(index);
                    ui.showDeleted(removedTask, taskList.size());
                    storage.save(taskList.asList());
                } catch (NumberFormatException e) {
                    ui.showError("oi.. give valid task number pls.\nUsage: delete <number>");
                } catch (IndexOutOfBoundsException e) {
                    ui.showError("task number out of range lah.");
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
