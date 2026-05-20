package ShortJobFirst;

public class ProcessSimulation implements ProcessWithRandomTime {
    private int processTime;
    private String processName;
    private int arrivalTime;
    private int startTime;
    private int finishTime;
    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;

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

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return processName + " [arrival=" + arrivalTime + ", burst=" + processTime + "]";
    }
}

