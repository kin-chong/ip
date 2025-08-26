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

public class Storage {
    private final Path file;

    public Storage(String... pathParts) {
        this.file = Paths.get("", pathParts);
    }

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
                    t.markAsDone();
                }
                tasks.add(t);
            }
        } catch (IOException ignored) {}
        return tasks;
    }

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
