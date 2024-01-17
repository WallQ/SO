package pt.ipp.estg.Utils;

import com.google.gson.Gson;
import pt.ipp.estg.Entities.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class JSON {
    private static final String LOGS_FILE_PATH = Resources.getPathFromResources("logs.json");

    public static ArrayList<Logger.LogEntry> readLogs() {
        ArrayList<Logger.LogEntry> logs = new ArrayList<>();
        Gson gson = new Gson();

        try (Reader fileReader = new FileReader(LOGS_FILE_PATH)) {
            Logger.LogEntry[] logsArray = gson.fromJson(fileReader, Logger.LogEntry[].class);
            if (logsArray != null) logs.addAll(Arrays.asList(logsArray));
        } catch (IOException e) {
            System.err.println("An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }

        return logs;
    }

    public static void saveLog(Logger.LogEntry logEntry) {
        Gson gson = new Gson();
        ArrayList<Logger.LogEntry> existingLogs = readLogs();

        try (FileWriter fileWriter = new FileWriter(LOGS_FILE_PATH)) {
            existingLogs.add(logEntry);
            gson.toJson(existingLogs, fileWriter);
        } catch (IOException e) {
            System.err.println("An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }
    }
}
