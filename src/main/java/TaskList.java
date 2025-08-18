public class TaskList {
    private final String[] items;
    private int count = 0;

    public TaskList(int capacity) {
        items = new String[capacity];
    }

    public boolean add(String task) {
        if (count >= items.length) return false;
        items[count++] = task;
        return true;
    }

    public int size() {
        return count;
    }

    public String[] toArray() throws EmptyTaskListException {
        if (count == 0) {
            throw new EmptyTaskListException("list empty pls (¬_¬)");
        }
        return java.util.Arrays.copyOf(items, count);
    }
}
