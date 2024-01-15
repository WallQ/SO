package pt.ipp.estg.Entities;

import pt.ipp.estg.Utils.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void log(String type, String message) {
        LogEntry logEntry = new LogEntry(getCurrentTime(), type, message);
        JSON.saveLog(logEntry);
    }

    private static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static class LogEntry {
        public String dateTime;
        public String type;
        public String message;

        public LogEntry(String dateTime, String type, String message) {
            this.dateTime = dateTime;
            this.type = type;
            this.message = message;
        }
    }
}
