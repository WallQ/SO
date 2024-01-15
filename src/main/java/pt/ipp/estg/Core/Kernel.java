package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Kernel {
    protected final TaskScheduler tasks;
    private final CPU cpu;
    private final Memory memory;
    private final Middleware middleware;
    protected final List<Task> tasksInExecution;
    private boolean isRunning;

    public Kernel(Middleware middleware) {
        this.cpu = new CPU(this);
        this.memory = new Memory(this);
        this.middleware = middleware;
        this.tasks = new TaskScheduler();
        this.tasksInExecution = new ArrayList<>();
        this.isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void receiveTask(Task task) {
        System.out.println("[Kernel] Received task from Middleware!");
//        Logger.log("Kernel", "Received task from Middleware!");
        task.setStatus(Status.PENDING);
        tasks.addTask(task);
    }

    public void sendTask(Task task) {
        try {
            if (memory.freeMemory(task.getMemory())) {
                System.out.println("[Kernel] Memory freed!");
//                Logger.log("Kernel", "Memory freed!");
            } else {
                System.out.println("[Kernel] Memory not freed!");
//                Logger.log("Kernel", "Memory not freed!");
            }
            System.out.println("[Kernel] Sending task to Middleware!");
//            Logger.log("Kernel", "Sending task to Middleware!");
            middleware.semaphore.acquire();
            middleware.receiveTask(task);
        } catch (Exception e) {
            System.err.println("An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }
    }

    public boolean allocateMemory(Task task) {
        if (memory.allocateMemory(task.getMemory())) {
            System.out.println("[Kernel] Memory allocated!");
//            Logger.log("Kernel", "Memory allocated!");
            return true;
        } else {
            System.out.println("[Kernel] Memory not allocated!");
//            Logger.log("Kernel", "Memory not allocated!");
            return false;
        }
    }

    public void writeResult(Task task, long result) {
        System.out.println("[Kernel] Writing result to Memory!");
//        Logger.log("Kernel", "Writing result to Memory!");
        memory.modifyTask(task, result);
        sendTask(task);
    }

    public void start() {
        System.out.println("[Kernel] Starting Kernel!");
//        Logger.log("Kernel", "Starting Kernel!");
        Thread thread = new Thread(cpu);
        thread.start();
        this.isRunning = true;
    }

    public void stop() {
        System.out.println("[Kernel] Stopping Kernel!");
//        Logger.log("Kernel", "Stopping Kernel!");
        tasks.clearTasks();
        while (!tasksInExecution.isEmpty()) {
            try {
                Thread.sleep(2500);
            } catch (Exception e) {
                System.err.println("An unexpected error has occurred!\n" + e.getMessage());
                System.exit(1);
            }
        }
        this.isRunning = false;
    }
}
