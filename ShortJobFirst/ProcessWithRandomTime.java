package ShortJobFirst;

// Interfaz para procesos con tiempo de ráfaga generado aleatoriamente
public interface ProcessWithRandomTime {
    int getProcessTime();
    void setProcessTime(int time);
    String getProcessName();
    void setProcessName(String name);
    void generateRandomProcessTime(int min, int max);
}
