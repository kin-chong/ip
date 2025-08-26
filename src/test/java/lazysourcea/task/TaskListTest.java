package lazysourcea.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {

    @Test
    void add_increasesSizeAndStoresTask() {
        TaskList list = new TaskList();
        Todo t = new Todo("read book");
        list.add(t);
        assertEquals(1, list.size());
        assertSame(t, list.get(0));
    }

    @Test
    void remove_returnsRemovedTaskAndShrinksList() {
        TaskList list = new TaskList();
        list.add(new Todo("A"));
        list.add(new Todo("B"));
        Task removed = list.remove(0);
        assertEquals("[T][ ] B", list.get(0).toString());
        assertEquals(1, list.size());
        assertTrue(removed.toString().contains("A"));
    }

    @Test
    void get_outOfBounds_throws() {
        TaskList list = new TaskList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }
}
