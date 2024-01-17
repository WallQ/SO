package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Enums.Status;
import pt.ipp.estg.Utils.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Kernel {
    private final CPU cpu;
    private final Memory memory;
    private final Middleware middleware;
    protected final Scheduler tasks;
    protected final List<Task> tasksInExecution;
    private boolean isRunning;

    public Kernel(Middleware middleware) {
        this.cpu = new CPU(this);
        this.memory = new Memory();
        this.middleware = middleware;
        this.tasks = new Scheduler();
        this.tasksInExecution = new ArrayList<>();
        this.isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void receiveTask(Task task) {
        System.out.println("[Kernel] Received task from Middleware!");
        task.setStatus(Status.PENDING);
        tasks.addTask(task);
    }

    public void sendTask(Task task) {
        try {
            if (memory.freeMemory(task)) {
                System.out.println("[Kernel] Sending task to Middleware!");
                middleware.semaphore.acquire();
                middleware.receiveTask(task);
            }
        } catch (Exception e) {
            Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, "An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }
    }

    public boolean allocateMemory(Task task) {
        if (memory.allocateMemory(task)) {
            System.out.println("[Kernel] Memory allocated!");
            return true;
        } else {
            System.out.println("[Kernel] Memory not allocated!");
            return false;
        }
    }

    public void writeResult(Task task, long result) {
        System.out.println("[Kernel] Writing result to Memory!");
        memory.modifyTask(task, result);
        sendTask(task);
    }

    public void start() {
        System.out.println("[Kernel] Starting Kernel!");
        new Thread(cpu).start();
        isRunning = true;
    }

    public void stop() {
        System.out.println("[Kernel] Stopping Kernel!");
        synchronized (tasksInExecution) {
            tasks.clearTasks();
            while (!tasksInExecution.isEmpty()) {
                try {
                    Thread.sleep(Config.VERIFY_TASK_EXECUTION_DELAY);
                } catch (InterruptedException e) {
                    Logger.getLogger(Kernel.class.getName()).log(Level.SEVERE, "An unexpected error has occurred!\n" + e.getMessage());
                    System.exit(1);
                }
            }
        }
        isRunning = false;
    }
}
