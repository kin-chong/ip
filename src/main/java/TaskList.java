import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> items;

    public TaskList() {
        this.items = new ArrayList<>();
    }

    public void add(Task task) {
        items.add(task);
    }

    public int size() {
        return items.size();
    }

    public Task get(int index) throws IndexOutOfBoundsException {
        return items.get(index);
    }

    public Task remove(int index) throws IndexOutOfBoundsException {
        return items.remove(index);
    }

    public void listTasks() {
        if (items.isEmpty()) {
            System.out.println("(no tasks yet)");
        } else {
            System.out.println("your tasks:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + "." + items.get(i));
            }
        }
    }
}
