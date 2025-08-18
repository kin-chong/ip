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

        TaskList tasks = new TaskList(100);
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
                try {
                    String[] all = tasks.toArray();
                    for (int i = 0; i < all.length; i++) {
                        System.out.println((i + 1) + ". " + all[i]);
                    }
                } catch (EmptyTaskListException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                if (tasks.add(input)) {
                    System.out.println("added: " + input);
                } else {
                    System.out.println("Sorry, task list is full (100 items).");
                }
            }
        }
        scanner.close();
    }
}
