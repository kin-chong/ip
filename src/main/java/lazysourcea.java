import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class lazysourcea {
    private static final DateTimeFormatter IN_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter IN_SLASH = DateTimeFormatter.ofPattern("d/M/yyyy");

    public static void main(String[] args) {
        Storage storage = new Storage("data", "lazysourcea.txt");

        TaskList taskList = new TaskList();

        ArrayList<Task> loaded = storage.load();
        for (Task t : loaded) {
            taskList.add(t);
        }

        Ui ui = new Ui();

        Scanner scanner = new Scanner(System.in);

        ui.showWelcome();
        boolean running = true;

        while (running) {
            ui.prompt();
            String input = ui.readCommand();

            // split input into a command (first word) and remaining argument (if any)
            // e.g. "mark 2" -> command="mark", argument="2"
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();
            String argument = (parts.length > 1) ? parts[1].trim() : "";

            switch (command) {
            case "bye":
                ui.showBye();
                running = false; // exit loop
                break;

            case "list":
                // call taskList class to list tasks
                ui.showList(taskList);
                break;

            case "mark":
                // mark task as done
                try {
                    int index = Integer.parseInt(argument) - 1;
                    Task t = taskList.get(index); // may throw IndexOutOfBoundsException
                    t.markAsDone();
                    ui.showMarked(t);
                    saveNow(storage, taskList);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    ui.showError("invalid task number. use: mark <number>");
                }
                break;

            case "unmark":
                // unmark a previously marked task
                try {
                    int index = Integer.parseInt(argument) - 1;
                    Task t = taskList.get(index); // may throw IndexOutOfBoundsException
                    t.markAsNotDone();
                    ui.showUnmarked(t);
                    saveNow(storage, taskList);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    ui.showError("invalid task number. use: unmark <number>");
                }
                break;

            case "todo":
                if (argument.isEmpty()) {
                    ui.showError("tsk.. todo description cannot empty.\nuse: todo <desc>");
                } else {
                    Task todo = new Todo(argument);
                    taskList.add(todo);
                    ui.showAdded(todo, taskList.size());
                    saveNow(storage, taskList);
                }
                break;

            case "deadline":
                try {
                    String[] deadlineParts = argument.split("/by", 2);
                    String desc = deadlineParts[0].trim();
                    LocalDate by = parseDate(deadlineParts[1]);
                    Task deadline = new Deadline(desc, by);
                    taskList.add(deadline);
                    ui.showAdded(deadline, taskList.size());
                    saveNow(storage, taskList);
                } catch (Exception e) {
                    ui.showError("oi.. invalid deadline format.\nuse: deadline <desc> /by <time>"
                            + "\naccepted: yyyy-MM-dd (e.g., 2019-10-15) or d/M/yyyy (e.g., 2/12/2019)");
                }
                break;

            case "event":
                try {
                    String[] eventParts = argument.split("/from|/to");
                    String desc = eventParts[0].trim();
                    String from = eventParts[1].trim();
                    String to = eventParts[2].trim();
                    Task event = new Event(desc, from, to);
                    taskList.add(event);
                    ui.showAdded(event, taskList.size());
                    saveNow(storage, taskList);
                } catch (Exception e) {
                    ui.showError("oi.. invalid event format.\nuse: event <desc> /from <time> /to <time>");
                }
                break;

            case "delete":
                try {
                    int index = Integer.parseInt(argument) - 1;
                    Task removedTask = taskList.remove(index);
                    ui.showDeleted(removedTask, taskList.size());
                    saveNow(storage, taskList);
                } catch (NumberFormatException e) {
                    ui.showError("oi.. give valid task number pls.\nUsage: delete <number>");
                } catch (IndexOutOfBoundsException e) {
                    ui.showError("task number out of range lah.");
                }
                break;

            case "help":
                ui.showHelp();
                break;

            default:
                if (!input.isEmpty()) {
                    System.out.println("tsk what u saying. i don't understand");
                } else {
                    System.out.println("oi.. enter something leh");
                }
                break;
            }
        }
        scanner.close();
    }

    private static void saveNow(Storage storage, TaskList taskList) {
        storage.save(taskList.asList());
    }

    private static LocalDate parseDate(String raw) {
        String s = raw.trim();
        try {
            // Try ISO first
            return LocalDate.parse(s, IN_ISO);
        } catch (DateTimeParseException ignored) {
            // Fallback: 2/12/2019 -> Dec 2 2019
            return LocalDate.parse(s, IN_SLASH);
        }
    }
}
