package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Utils.Config;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    private final int MAX_MEMORY = Config.MAX_MEMORY;
    private int memoryUsed;
    private final List<Task> tasks;

    public Memory() {
        this.memoryUsed = 0;
        this.tasks = new ArrayList<>();
    }

    public synchronized boolean allocateMemory(Task task) {
        int taskMemory = task.getMemory();

        if (isFull() || !hasMemoryAvailable(taskMemory)) {
            System.out.println("[Memory] Unable to allocate memory!");
            return false;
        }

        memoryUsed += taskMemory;
        tasks.add(task);
        return true;
    }

    public synchronized boolean freeMemory(Task task) {
        int taskMemory = task.getMemory();

        if (isEmpty() || !hasMemoryUsed(taskMemory)) {
            System.out.println("[Memory] Unable to free memory!");
            return false;
        }

        memoryUsed -= taskMemory;
        tasks.remove(task);
        return true;
    }

    public void modifyTask(Task task, long result) {
        System.out.println("[Memory] Writing result to task!");
        task.setResult("Task completed in " + result + " milliseconds.");
    }

    private boolean isFull() {
        return memoryUsed == MAX_MEMORY;
    }

    private boolean isEmpty() {
        return memoryUsed == 0;
    }

    private boolean hasMemoryAvailable(int memory) {
        return memoryUsed + memory <= MAX_MEMORY;
    }

    private boolean hasMemoryUsed(int memory) {
        return memoryUsed >= memory;
    }
}
