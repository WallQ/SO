package pt.ipp.estg.Entities;

import pt.ipp.estg.Enums.Priority;
import pt.ipp.estg.Enums.Status;

import java.util.UUID;

public class Task implements Runnable {
    private UUID id;
    private String name;
    private String description;
    private String dateTime;
    private Priority priority;
    private Status status;
    private String result;
    private int memory;

    public Task(String name, String description, String dateTime, Priority priority) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.priority = priority;
        this.status = Status.CREATED;
        this.result = "";
        this.memory = generateMemoryRandomNumber();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    private int generateRandomNumber() {
        return (int) (Math.random() * 5 + 1) * 1000;
    }

    private int generateMemoryRandomNumber() {
        return (int) ((Math.random() * 1024) + 1);
    }

    @Override
    public void run() {
        try {
            this.status = Status.RUNNING;
            System.out.printf("[%s] Priority: %s - Status: %s%n", this.name, this.priority, this.status);
            Thread.sleep(generateRandomNumber());
        } catch (InterruptedException e) {
            System.err.println("An unexpected error has occurred!\n" + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", result='" + result + '\'' +
                ", memory=" + memory +
                '}';
    }
}
