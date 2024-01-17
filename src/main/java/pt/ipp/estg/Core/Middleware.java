package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Utils.Config;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Middleware {
    private static final int MAX_CONCURRENT_CONSUMERS = Config.MAX_CONCURRENT_CONSUMERS;
    private final Kernel kernel;
    protected final Semaphore semaphore;
    private final Queue<Task> tasks;
    public final List<Task> tasksFinished;

    public Middleware() {
        this.kernel = new Kernel(this);
        this.semaphore = new Semaphore(MAX_CONCURRENT_CONSUMERS);
        this.tasks = new LinkedList<>();
        this.tasksFinished = new LinkedList<>();
    }

    public void sendTask(Task task) {
        try {
            System.out.println("[Middleware] Sending task to Kernel!");
            semaphore.acquire();
            tasks.add(task);
            kernel.receiveTask(Objects.requireNonNull(tasks.poll()));
        } catch (InterruptedException e) {
            Logger.getLogger(Middleware.class.getName()).log(Level.SEVERE, "An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        } finally {
            semaphore.release();
        }
    }

    public void receiveTask(Task task) {
        System.out.println("[Middleware] Sending result to application!");
        tasksFinished.add(task);
        semaphore.release();
        System.out.println(task);
    }

    public void turnOn() {
        System.out.println("[Middleware] Telling Kernel to turn on!");
        kernel.start();
    }

    public void turnOff() {
        System.out.println("[Middleware] Telling Kernel to turn off!");
        kernel.stop();
    }
}
