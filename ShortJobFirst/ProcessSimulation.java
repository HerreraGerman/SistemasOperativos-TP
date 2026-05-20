package ShortJobFirst;

public class ProcessSimulation implements ProcessWithRandomTime {
    private int processTime;
    private String processName;

    public ProcessSimulation(String name) {
        this.processName = name;
    }

    @Override
    public int getProcessTime() {
        return processTime;
    }

    @Override
    public void setProcessTime(int time) {
        this.processTime = time;
    }

    @Override
    public String getProcessName() {
        return processName;
    }

    @Override
    public void setProcessName(String name) {
        this.processName = name;
    }

    @Override
    public void generateRandomProcessTime(int min, int max) {
        this.processTime = java.util.concurrent.ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    @Override
    public String toString() {
        return processName + "=" + processTime;
    }
}

