package lazysourcea;

import java.util.ArrayList;

import lazysourcea.parser.Parser;
import lazysourcea.storage.Storage;
import lazysourcea.task.Deadline;
import lazysourcea.task.Event;
import lazysourcea.task.Task;
import lazysourcea.task.TaskList;
import lazysourcea.task.Todo;
import lazysourcea.ui.Ui;
import lazysourcea.logic.CommandExecutor;

/**
 * UI-agnostic core orchestrator for Lazysourcea. Holds the persistent
 * {@link Storage}, in-memory {@link TaskList}, and {@link Parser}. Each call
 * to {@link #getResponse(String)} parses the input and delegates command
 * handling to {@link CommandExecutor}, capturing user-facing lines through a
 * {@code Consumer<String>} sink (no printing, no blocking loop) and returning
 * the composed reply. Use {@link #getWelcomeMessage()} for the startup greeting,
 * and check {@link #isExit()} after each call to detect a "bye" so the caller
 * (e.g., JavaFX) can close the app.
 */
public class lazysourcea {
    // --- fields for shared core state ---
    private final Storage storage;
    private final TaskList taskList;
    private final Parser parser;

    private boolean isExit = false;

    // --- JavaFX-friendly constructor (no console I/O) ---
    public lazysourcea(String dataDir, String fileName) {
        this.storage = new Storage(dataDir, fileName);
        this.taskList = new TaskList();
        this.parser = new Parser();

        ArrayList<Task> loaded = storage.load();
        for (Task t : loaded) {
            taskList.addTask(t);
        }
    }

    /**
     * Process a single user input and return a single string reply.
     * No printing, no blocking loop.
     */
    public String getResponse(String input) {
        Parser.Parsed parsed = parser.parse(input);

        StringBuilder sb = new StringBuilder();
        java.util.function.Consumer<String> out = line -> {
            if (line != null) sb.append(line).append(System.lineSeparator());
        };

        CommandExecutor exec =
                new CommandExecutor(taskList, storage, parser, out);

        boolean exit = exec.execute(parsed, out);
        this.isExit = exit;

        return sb.toString().trim();
    }



    /** For JavaFX to check if it should close the window after showing the reply. */
    public boolean isExit() {
        return isExit;
    }

    public String getWelcomeMessage() {
        var sb = new StringBuilder();
        var ui = new Ui(line -> { if (line != null) sb.append(line).append(System.lineSeparator()); });
        ui.showWelcome();
        return sb.toString().trim();
    }

}
