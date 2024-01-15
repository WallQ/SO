package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Utils.Config;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Middleware {
    private static final int MAX_CONCURRENT_CONSUMERS = Config.MAX_CONCURRENT_CONSUMERS;
    protected final Semaphore semaphore;
    private final Kernel kernel;
    private final Queue<Task> tasks;

    public Middleware() {
        this.kernel = new Kernel(this);
        this.semaphore = new Semaphore(MAX_CONCURRENT_CONSUMERS);
        this.tasks = new LinkedList<>();
    }

    public void sendTask(Task task) {
        try {
            System.out.println("[Middleware] Sending task to Kernel!");
//            Logger.log("Middleware", "Sending task to Kernel!");
            semaphore.acquire();
            tasks.add(task);
            kernel.receiveTask(Objects.requireNonNull(tasks.poll()));
            semaphore.release();
        } catch (Exception e) {
            System.err.println("An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }
    }

    public void receiveTask(Task task) {
        System.out.println("[Middleware] Sending result to Client!");
        semaphore.release();
    }

    public void turnOn() {
        System.out.println("[Middleware] Turning on Kernel!");
//        Logger.log("Middleware", "Turning on Kernel!");
        kernel.start();
    }

    public void turnOff() {
        System.out.println("[Middleware] Turning off Kernel!");
//        Logger.log("Middleware", "Turning off Kernel!");
        kernel.stop();
    }
}
