package lazysourcea.storage;

import lazysourcea.task.Deadline;
import lazysourcea.task.Event;
import lazysourcea.task.Task;
import lazysourcea.task.Todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Handles saving and loading tasks from a file.
 * <p>
 * The {@code Storage} class ensures that tasks are persisted
 * between runs of the chatbot by reading and writing them
 * in a plain text format.
 */
public class Storage {
    private final Path file;

    /**
     * Creates a new {@code Storage} instance using the given path.
     * <p>
     * The path is constructed relative to the project directory.
     *
     * @param pathParts one or more path components (e.g. "data", "duke.txt")
     */
    public Storage(String... pathParts) {
        this.file = Paths.get("", pathParts);
    }

    /**
     * Loads tasks from the save file.
     * <p>
     * If the file does not exist, an empty list is returned.
     *
     * @return a list of {@link Task} objects loaded from the file
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            if (Files.notExists(file)) {
                return tasks;
            }
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] p = line.split("\\s*\\|\\s*");
                if (p.length < 3) {
                    continue;
                }
                String type = p[0];
                boolean done = "1".equals(p[1]);
                String desc = p[2];
                Task t;
                switch (type) {
                case "T":
                    t = new Todo(desc);
                    break;
                case "D":
                    if (p.length < 4) continue;
                    LocalDate by = LocalDate.parse(p[3]);
                    t = new Deadline(desc, by);
                    break;
                case "E":
                    if (p.length < 5) continue;
                    t = new Event(desc, p[3], p[4]);
                    break;
                default:
                    continue;
                }
                if (done) {
                    t.isDone();
                }
                tasks.add(t);
            }
        } catch (IOException ignored) {}
        return tasks;
    }

    /**
     * Saves the given list of tasks to the save file.
     * <p>
     * If the folder does not exist, it is created automatically.
     *
     * @param tasks the list of {@link Task} objects to save
     */
    public void save(List<Task> tasks) {
        try {
            Path parent = file.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                lines.add(t.toDataString());
            }
            Files.write(file, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ignored) {}
    }
}
