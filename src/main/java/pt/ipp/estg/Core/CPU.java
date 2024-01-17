package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Enums.Status;
import pt.ipp.estg.Utils.Config;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CPU implements Runnable {
    private final Kernel kernel;

    public CPU(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public void run() {
        try {
            while (kernel.isRunning()) {
                synchronized (kernel.tasks) {
                    if (!kernel.tasks.isEmpty()) {
                        Task task = kernel.tasks.getTask();

                        if (task == null) continue;

                        if (kernel.allocateMemory(task)) {
                            Thread thread = new Thread(task);
                            long startTime = System.currentTimeMillis();

                            System.out.println("[CPU] Processing task!");
                            thread.start();

                            synchronized (kernel.tasksInExecution) {
                                kernel.tasksInExecution.add(task);
                            }

                            thread.join();
                            task.setStatus(Status.COMPLETED);

                            long endTime = System.currentTimeMillis();
                            long executionTime = endTime - startTime;

                            synchronized (kernel.tasksInExecution) {
                                kernel.tasksInExecution.remove(task);
                            }

                            System.out.println("[CPU] Task finished!");
                            kernel.writeResult(task, executionTime);
                        } else {
                            System.out.println("[CPU] Task postponed, not enough memory!");
                            kernel.tasks.addTask(task);
                        }
                    }
                }
                Thread.sleep(Config.NEXT_TASK_DELAY);
            }
        } catch (InterruptedException e) {
            Logger.getLogger(CPU.class.getName()).log(Level.SEVERE, "An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }
    }
}
