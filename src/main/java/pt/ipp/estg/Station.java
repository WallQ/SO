package pt.ipp.estg;

import pt.ipp.estg.Core.Middleware;
import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Enums.Priority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Station {
    public static void main(String[] args) {
        Middleware middleware = new Middleware();
        middleware.turnOn();

        List<Thread> threads = getThreads(middleware);

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Logger.getLogger(Station.class.getName()).log(Level.SEVERE, "An unexpected error has occurred!\n" + e.getMessage());
                System.exit(1);
            }
        }

        new Thread(() -> {
            try {
                for (Task task : middleware.tasksFinished) {
                    if (task != null) System.out.println(task);
                }
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                Logger.getLogger(Station.class.getName()).log(Level.SEVERE, "An unexpected error has occurred!\n" + e.getMessage());
                System.exit(1);
            }
        }).start();
    }

    private static List<Thread> getThreads(Middleware middleware) {
        String dateTime = getCurrentDateTime();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Thread thread = new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                for (int j = 0; j < 6; j++) {
                    Task task = new Task("(" + threadName + ") Task #" + j, "This is the task #" + j + ".", dateTime, getRandomPriority());
                    middleware.sendTask(task);
                }
            }, "Thread #" + i);
            threads.add(thread);
            thread.start();
        }

        return threads;
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