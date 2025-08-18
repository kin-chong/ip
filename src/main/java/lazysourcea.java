import java.util.Scanner;

public class lazysourcea {
    public static void main(String[] args) {
        String logo = " _\n"
                + "| | __ _ _____   _ ___  ___  _   _ _ __ ___ ___  __ _\n"
                + "| |/ _` |_  / | | / __|/ _ \\| | | | '__/ __/ _ \\/ _` |\n"
                + "| | (_| |/ /| |_| \\__ \\ (_) | |_| | | | (_|  __/ (_| |\n"
                + "|_|\\__,_/___|\\__, |___/\\___/ \\__,_|_|  \\___\\___|\\__,_|\n"
                + "             |___/\n";

        String greeting = "hi. i'm lazysourcea.\nwhat do you want to do?";
        String greetingline = "-----------------------";

        String bye = "bye.";
        String byeline = "----";

        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello from\n" + logo);

        System.out.println(greetingline);
        System.out.println(greeting);
        System.out.println(greetingline);


        while (true) {
            System.out.print(">>> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                System.out.print(bye);
                break;
            } else if (input.equalsIgnoreCase("list")) {
                taskList.listTasks();
            } else if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5)) - 1;
                    Task t = taskList.get(index);
                    t.markAsDone();
                    System.out.println("task marked as done:");
                    System.out.println("  " + t);
                } catch (Exception e) {
                    System.out.println("invalid task number");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7)) - 1;
                    Task t = taskList.get(index);
                    t.markAsNotDone();
                    System.out.println("task unmarked as done:");
                    System.out.println("  " + t);
                } catch (Exception e) {
                    System.out.println("invalid task number");
                }
            } else {
                Task newTask = new Task(input);
                taskList.add(newTask);
                System.out.println("added: " + newTask);
            }
        }
        scanner.close();
    }
}
