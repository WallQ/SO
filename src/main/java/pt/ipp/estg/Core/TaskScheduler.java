package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskScheduler {
    private final Queue<Task> highPriorityTasks;
    private final Queue<Task> mediumPriorityTasks;
    private final Queue<Task> lowPriorityTasks;
    private int starvationCounter;

    public TaskScheduler() {
        this.highPriorityTasks = new LinkedBlockingQueue<>();
        this.mediumPriorityTasks = new LinkedBlockingQueue<>();
        this.lowPriorityTasks = new LinkedBlockingQueue<>();
        this.starvationCounter = 0;
    }

    public void addTask(Task task) {
        switch (task.getPriority()) {
            case HIGH:
                highPriorityTasks.add(task);
                break;
            case MEDIUM:
                mediumPriorityTasks.add(task);
                break;
            case LOW:
                lowPriorityTasks.add(task);
                break;
        }
    }

    public synchronized Task getTask() {
        Task task = null;

        if (isEmpty()) return null;

        if (!highPriorityTasks.isEmpty() && starvationCounter == 0) {
            task = highPriorityTasks.poll();
            incrementStarvationCounter();
        } else if (!mediumPriorityTasks.isEmpty() && starvationCounter > 0 && starvationCounter < 3) {
            task = mediumPriorityTasks.poll();
            incrementStarvationCounter();
        } else if (!lowPriorityTasks.isEmpty() && starvationCounter >= 3 && starvationCounter < 6) {
            task = lowPriorityTasks.poll();
            incrementStarvationCounter();
        } else {
            resetStarvationCounter();
        }

        return task;
    }

    public synchronized void incrementStarvationCounter() {
        starvationCounter++;
    }

    public synchronized void resetStarvationCounter() {
        starvationCounter = 0;
    }

    public boolean isEmpty() {
        return highPriorityTasks.isEmpty() && mediumPriorityTasks.isEmpty() && lowPriorityTasks.isEmpty();
    }

    public void clearTasks() {
        highPriorityTasks.clear();
        mediumPriorityTasks.clear();
        lowPriorityTasks.clear();
    }
}
