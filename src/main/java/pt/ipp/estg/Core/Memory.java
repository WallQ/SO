package pt.ipp.estg.Core;

import pt.ipp.estg.Entities.Task;
import pt.ipp.estg.Utils.Config;

public class Memory {
    private final Kernel kernel;
    private final int MAX_MEMORY = Config.MAX_MEMORY;
    private int memoryUsed;
    private int memoryAvailable;

    public Memory(Kernel kernel) {
        this.kernel = kernel;
        this.memoryUsed = 0;
        this.memoryAvailable = MAX_MEMORY;
    }

    private boolean isEmpty() {
        return memoryUsed == 0;
    }

    private boolean isFull() {
        return memoryUsed == MAX_MEMORY;
    }

    private boolean hasMemoryAvailable(int memory) {
        return memoryAvailable >= memory;
    }

    private boolean hasMemoryUsed(int memory) {
        return memoryUsed >= memory;
    }

    public synchronized boolean allocateMemory(int memory) {
        if (isFull()) {
            System.out.println("[Memory] Memory is full!");
//            Logger.log("Memory", "Memory is full!");
            return false;
        }

        if (!hasMemoryAvailable(memory)) {
            System.out.println("[Memory] Not enough memory available!");
//            Logger.log("Memory", "Not enough memory available!");
            return false;
        }

        memoryUsed += memory;
        memoryAvailable -= memory;
        return true;
    }

    public synchronized boolean freeMemory(int memory) {
        if (isEmpty()) {
            System.out.println("[Memory] Memory is empty!");
//            Logger.log("Memory", "Memory is empty!");
            return false;
        }

        if (!hasMemoryUsed(memory)) {
            System.out.println("[Memory] Not enough memory used!");
//            Logger.log("Memory", "Not enough memory used!");
            return false;
        }

        memoryUsed -= memory;
        memoryAvailable += memory;
        return true;
    }

    public void modifyTask(Task task, long result) {
        System.out.println("[Memory] Writing result to Task!");
//        Logger.log("Memory", "Writing result to Task!");
        task.setResult("Task completed in " + result + " milliseconds.");
    }
}
