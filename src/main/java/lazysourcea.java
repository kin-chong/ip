import java.util.Scanner;

public class lazysourcea {
    public static void main(String[] args) {
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

        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello from\n" + logo);

        System.out.println(greetingline);
        System.out.println(greeting);
        System.out.println(greetingline);

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
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("invalid task number");
                    }
                    break;

                case "unmark":
                    // unmark a previously marked task
                    try {
                        int index = Integer.parseInt(argument) - 1;
                        Task t = taskList.get(index); // may throw IndexOutOfBoundsException
                        t.markAsNotDone();
                        System.out.println("task marked as done:\n " + t);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("invalid task number");
                    }
                    break;

                case "todo":
                    if (argument.isEmpty()) {
                        System.out.println("tsk. todo description cannot empty");
                    } else {
                        Task todo = new Todo(argument);
                        taskList.add(todo);
                        System.out.println("ok. task added:\n  " + todo);
                        System.out.println("now you have " + taskList.size() + " tasks in the list.");
                    }
                    break;

                case "deadline":
                    try {
                        String[] deadlineParts = argument.split("/by", 2);
                        String desc = deadlineParts[0].trim();
                        String by = deadlineParts[1].trim();
                        Task deadline = new Deadline(desc, by);
                        taskList.add(deadline);
                        System.out.println("ok. task added:\n  " + deadline);
                        System.out.println("now you have " + taskList.size() + " tasks in the list.");
                    } catch (Exception e) {
                        System.out.println("oi invalid deadline format. use: deadline <desc> /by <time>");
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
                        System.out.println("now you have " + taskList.size() + " tasks in the list.");
                    } catch (Exception e) {
                        System.out.println("oi invalid event format. use: event <desc> /from <time> /to <time>");
                    }
                    break;

                default:
                    // any other input becomes new task
                    if (!input.isEmpty()) {
                        Task newTask = new Task(input);
                        taskList.add(newTask);
                        System.out.println("added: " + newTask);
                    } else {
                        System.out.println("oi enter something leh");
                    }
                    break;
            }
        }
        scanner.close();
    }
}
