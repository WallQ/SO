package pt.ipp.estg;

import pt.ipp.estg.Core.Middleware;
import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Enums.Priority;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Station {
    public static void main(String[] args) {
        Middleware middleware = new Middleware();
        middleware.turnOn();

        String dateTime = getCurrentDateTime();

        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                for (int j = 0; j < 6; j++) {
                    Task task = new Task("(" + threadName + ") Task #" + (j + 1), "This is the task #" + (j + 1) + ".", dateTime, getRandomPriority());
                    middleware.sendTask(task);
                }
            }, "Thread #" + (i + 1)).start();
        }
    }

    private static String getCurrentDateTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    private static Priority getRandomPriority() {
        int randomNumber = (int) (Math.random() * 3);
        return switch (randomNumber) {
            case 0 -> Priority.HIGH;
            case 1 -> Priority.MEDIUM;
            default -> Priority.LOW;
        };
    }
}