package lazysourcea.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {

    @Test
    void add_Task_increasesListSizeAndStoresTask() {
        TaskList list = new TaskList();
        Todo t = new Todo("read book");
        list.addTask(t);
        assertEquals(1, list.listSize());
        assertSame(t, list.getTask(0));
    }

    @Test
    void remove_Task_returnsRemovedTaskAndShrinksList() {
        TaskList list = new TaskList();
        list.addTask(new Todo("A"));
        list.addTask(new Todo("B"));
        Task removed = list.removeTask(0);
        assertEquals("[T][ ] B", list.getTask(0).toString());
        assertEquals(1, list.listSize());
        assertTrue(removed.toString().contains("A"));
    }

    @Test
    void get_Task_outOfBounds_throws() {
        TaskList list = new TaskList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.getTask(0));
    }
}
