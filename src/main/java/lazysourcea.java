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

        Scanner scanner = new Scanner(System.in);

        String logo = """
                     _
                    | | __ _ _____   _ ___  ___  _   _ _ __ ___ ___  __ _
                    | |/ _` |_  / | | / __|/ _ \\| | | | '__/ __/ _ \\/ _` |
                    | | (_| |/ /| |_| \\__ \\ (_) | |_| | | | (_|  __/ (_| |
                    |_|\\__,_/___|\\__, |___/\\___/ \\__,_|_|  \\___\\___|\\__,_|
                                 |___/
                    """;

        String greeting = "hi. i'm lazysourcea.\nwhat do you want to do?";
        String greetingline = "-----------------------";
        String bye = "bye.";

        System.out.println("Hello from\n" + logo);

        System.out.println(greetingline);
        System.out.println(greeting);
        System.out.println(greetingline);
        System.out.println("enter help for available commands");

        boolean running = true;

        while (running) {
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();

            // split input into a command (first word) and remaining argument (if any)
            // e.g. "mark 2" -> command="mark", argument="2"
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();
            String argument = (parts.length > 1) ? parts[1].trim() : "";

            switch (command) {
            case "bye":
                System.out.print(bye);
                running = false; // exit loop
                break;

            case "list":
                // call taskList class to list tasks
                taskList.listTasks();
                break;

            case "mark":
                // mark task as done
                try {
                    int index = Integer.parseInt(argument) - 1;
                    Task t = taskList.get(index); // may throw IndexOutOfBoundsException
                    t.markAsDone();
                    System.out.println("task marked as done:\n " + t);
                    saveNow(storage, taskList);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("invalid task number. use: mark <number>");
                }
                break;

            case "unmark":
                // unmark a previously marked task
                try {
                    int index = Integer.parseInt(argument) - 1;
                    Task t = taskList.get(index); // may throw IndexOutOfBoundsException
                    t.markAsNotDone();
                    System.out.println("task marked as undone:\n " + t);
                    saveNow(storage, taskList);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("invalid task number. use: unmark <number>");
                }
                break;

            case "todo":
                if (argument.isEmpty()) {
                    System.out.println("tsk.. todo description cannot empty.\nuse: todo <desc>");
                } else {
                    Task todo = new Todo(argument);
                    taskList.add(todo);
                    System.out.println("ok. task added:\n  " + todo);
                    System.out.println("now you have " + taskList.size() + " tasks in the list.");
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
                    System.out.println("ok. task added:\n  " + deadline);
                    System.out.println("now you have " + taskList.size() + " task(s) in the list.");
                    saveNow(storage, taskList);
                } catch (Exception e) {
                    System.out.println("oi.. invalid deadline format.\nuse: deadline <desc> /by <time>"
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
                    System.out.println("ok. task added:\n  " + event);
                    System.out.println("now you have " + taskList.size() + " task(s) in the list.");
                    saveNow(storage, taskList);
                } catch (Exception e) {
                    System.out.println("oi.. invalid event format.\nuse: event <desc> /from <time> /to <time>");
                }
                break;

            case "delete":
                try {
                    int index = Integer.parseInt(argument) - 1;
                    Task removedTask = taskList.remove(index);
                    System.out.println("task:");
                    System.out.println("  " + removedTask + "\nremoved.");
                    System.out.println("now you have " + taskList.size() + " tasks in the list.");
                    saveNow(storage, taskList);
                } catch (NumberFormatException e) {
                    System.out.println("oi.. give valid task number pls.\nUsage: delete <number>");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("task number out of range lah.");
                }
                break;

            case "help":
                System.out.println("list:           shows your tasklist");
                System.out.println("todo:           adds a todo task. use: todo <desc>");
                System.out.println("deadline:       adds a deadline task. use: deadline <desc> /by <time> " +
                        "[accepted: yyyy-MM-dd (e.g., 2019-10-15) or d/M/yyyy (e.g., 2/12/2019)]");
                System.out.println("event:          adds a event task. use: event <desc> /from <time> /to <time>");
                System.out.println("mark:           marks a task in the list. use: mark <number>");
                System.out.println("unmark:         unmarks a task in the list. use: unmark <number>");
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
