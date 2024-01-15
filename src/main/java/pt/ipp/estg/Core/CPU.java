package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Enums.Status;

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
                            task.setStatus(Status.RUNNING);
                            thread.start();
                            System.out.println("[CPU] Processing task!");
//                            Logger.log("CPU", "Processing task!");
                            synchronized (kernel.tasksInExecution) {
                                kernel.tasksInExecution.add(task);
                            }
                            thread.join();
                            task.setStatus(Status.COMPLETED);
                            long endTime = System.currentTimeMillis();
                            long executionTime = endTime - startTime;
                            System.out.println("[CPU] Sending result to Kernel!");
//                            Logger.log("CPU", "Sending result to Kernel!");
                            synchronized (kernel.tasksInExecution) {
                                kernel.tasksInExecution.remove(task);
                            }
                            kernel.writeResult(task, executionTime);
                        } else {
                            System.out.println("[CPU] Task postponed, not enough memory!");
//                            Logger.log("CPU", "Task postponed, not enough memory!");
                            kernel.tasks.addTask(task);
                            continue;
                        }
                    }
                }
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            System.err.println("An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }
    }
}
