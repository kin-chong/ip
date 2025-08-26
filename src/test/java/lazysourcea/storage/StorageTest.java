package lazysourcea.storage;

import lazysourcea.task.Deadline;
import lazysourcea.task.Task;
import lazysourcea.task.TaskList;
import lazysourcea.task.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    void saveThenLoad_roundTripsTasks() {
        Storage storage = new Storage(tempDir.toString(), "duke.txt");

        TaskList list = new TaskList();
        Task t1 = new Todo("read book");
        Task t2 = new Deadline("return book", LocalDate.of(2019, 12, 2));
        t2.markAsDone();
        list.add(t1);
        list.add(t2);

        storage.save(List.of(t1, t2));
        var loaded = storage.load();

        assertEquals(2, loaded.size());
        assertTrue(loaded.get(0) instanceof Todo);
        assertTrue(loaded.get(1) instanceof Deadline);
        assertEquals(t1.toDataString(), loaded.get(0).toDataString());
        assertEquals(t2.toDataString(), loaded.get(1).toDataString());
    }
}
