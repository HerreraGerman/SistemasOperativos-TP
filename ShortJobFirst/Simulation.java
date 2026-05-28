package ShortJobFirst;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Simulation {
    // Cantidad predeterminada de ejecuciones de la simulación
    private static final int DEFAULT_RUN_COUNT = 5;
    // Cantidad predeterminada de procesos a generar en cada ejecución
    private static final int DEFAULT_PROCESS_COUNT = 5;
    // Rango predeterminado para los tiempos de ráfaga generados aleatoriamente
    private static final int DEFAULT_MIN_TIME = 1;
    private static final int DEFAULT_MAX_TIME = 10;
    // Rango predeterminado para los tiempos de llegada generados aleatoriamente
    private static final int DEFAULT_MAX_ARRIVAL = 10;
    // Nombre del archivo de resultados
    private static final String RESULT_FILE_NAME = "simulation_results.txt";

    // Método principal para ejecutar la simulación con los parámetros especificados
    public static void main(String[] args) {
        int runCount = parseIntArg(args, 0, DEFAULT_RUN_COUNT);
        int processCount = parseIntArg(args, 1, DEFAULT_PROCESS_COUNT);
        int maxArrivalTime = parseIntArg(args, 2, DEFAULT_MAX_ARRIVAL);
        StringBuilder report = new StringBuilder();

        for (int run = 1; run <= runCount; run++) {
            ProcessSimulation[] processes = createRandomProcesses(processCount, DEFAULT_MIN_TIME, DEFAULT_MAX_TIME, maxArrivalTime);
            ProcessSimulation[] rawProcesses = processes.clone();

            String runHeader = "Ejecucion " + run + " de " + runCount + ":";
            println(report, runHeader);

            List<ProcessSimulation> schedule = scheduleProcesses(processes);

            List<String> rawLines = ProcessResultTable.getBurstLines(rawProcesses, "Lista de procesos sin programar:");
            ProcessResultTable.printLines(rawLines);
            appendLines(report, rawLines);

            List<String> scheduleLines = ProcessResultTable.getScheduleLines(schedule, "Métricas de la programación SJF:");
            ProcessResultTable.printLines(scheduleLines);
            appendLines(report, scheduleLines);

            List<String> summaryLines = getSummaryLines(schedule);
            for (String line : summaryLines) {
                println(report, line);
            }

            if (run < runCount) {
                String separator = "".repeat(50);
                println(report, separator);
            }
        }

        Path outputFile = resolveOutputFile();
        writeReportToFile(outputFile, report.toString());
        System.out.println("Resultados guardados en " + outputFile.toAbsolutePath());
    }

    // Método para crear un arreglo de procesos con tiempos de ráfaga y tiempos de llegada generados aleatoriamente
    private static ProcessSimulation[] createRandomProcesses(int count, int minTime, int maxTime, int maxArrivalTime) {
        ProcessSimulation[] processes = new ProcessSimulation[count];
        for (int i = 0; i < count; i++) {
            processes[i] = new ProcessSimulation("Process " + (i + 1));
            processes[i].generateRandomProcessTime(minTime, maxTime);
            processes[i].setArrivalTime(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, maxArrivalTime + 1));
        }
        return processes;
    }

    // Método auxiliar para analizar argumentos de línea de comandos con valores predeterminados
    private static int parseIntArg(String[] args, int index, int defaultValue) {
        if (args.length <= index) {
            return defaultValue;
        }
        try {
            int value = Integer.parseInt(args[index]);
            return value > 0 ? value : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Método para programar los procesos utilizando el algoritmo Shortest Job First (SJF) y calcular las métricas correspondientes
    private static List<ProcessSimulation> scheduleProcesses(ProcessSimulation[] tasks) {
        List<ProcessSimulation> unscheduled = new ArrayList<>(Arrays.asList(tasks));
        List<ProcessSimulation> schedule = new ArrayList<>();
        int currentTime = unscheduled.stream()
                .mapToInt(ProcessSimulation::getArrivalTime)
                .min()
                .orElse(0);

        while (!unscheduled.isEmpty()) {
            List<ProcessSimulation> ready = new ArrayList<>();
            for (ProcessSimulation process : unscheduled) {
                if (process.getArrivalTime() <= currentTime) {
                    ready.add(process);
                }
            }
            if (ready.isEmpty()) {
                currentTime = unscheduled.stream()
                        .mapToInt(ProcessSimulation::getArrivalTime)
                        .min()
                        .orElse(currentTime);
                continue;
            }

            ProcessSimulation next = ready.stream()
                    .min(Comparator.comparingInt(ProcessSimulation::getProcessTime)
                            .thenComparingInt(ProcessSimulation::getArrivalTime)
                            .thenComparing(ProcessSimulation::getProcessName))
                    .orElseThrow();

            int startTime = Math.max(currentTime, next.getArrivalTime());
            next.setStartTime(startTime);
            next.setFinishTime(startTime + next.getProcessTime());
            next.setWaitingTime(startTime - next.getArrivalTime());
            next.setTurnaroundTime(next.getFinishTime() - next.getArrivalTime());
            next.setResponseTime(next.getWaitingTime());
            currentTime = next.getFinishTime();
            schedule.add(next);
            unscheduled.remove(next);
        }
        return schedule;
    }

    // Método para calcular las métricas resumen de la programación y generar líneas de texto para el reporte
    private static List<String> getSummaryLines(List<ProcessSimulation> schedule) {
        int totalWaiting = 0;
        int totalTurnaround = 0;
        int totalIdle = 0;
        int previousFinish = schedule.isEmpty() ? 0 : schedule.get(0).getStartTime();

        for (int i = 0; i < schedule.size(); i++) {
            ProcessSimulation process = schedule.get(i);
            totalWaiting += process.getWaitingTime();
            totalTurnaround += process.getTurnaroundTime();
            if (i > 0) {
                totalIdle += Math.max(0, process.getStartTime() - previousFinish);
            }
            previousFinish = process.getFinishTime();
        }

        double averageWaiting = schedule.isEmpty() ? 0 : (double) totalWaiting / schedule.size();
        double averageTurnaround = schedule.isEmpty() ? 0 : (double) totalTurnaround / schedule.size();
        int makespan = schedule.isEmpty() ? 0 : schedule.get(schedule.size() - 1).getFinishTime()
                - schedule.get(0).getArrivalTime();

        List<String> summary = new ArrayList<>();
        summary.add("Resumen:");
        summary.add(String.format("Tiempo promedio de espera: %.2f", averageWaiting));
        summary.add(String.format("Tiempo promedio de turnaround: %.2f", averageTurnaround));
        summary.add("Tiempo total de inactividad: " + totalIdle);
        summary.add("Makespan: " + makespan);
        summary.add("");
        return summary;
    }

    // Método auxiliar para imprimir una línea de texto tanto en la consola como en el reporte
    private static void println(StringBuilder report, String text) {
        System.out.println(text);
        report.append(text).append(System.lineSeparator());
    }

    // Método auxiliar para agregar una lista de líneas al reporte
    private static void appendLines(StringBuilder report, List<String> lines) {
        for (String line : lines) {
            report.append(line).append(System.lineSeparator());
        }
    }

    // Método para resolver la ruta del archivo de resultados, asegurando que se cree en el directorio correcto
    private static Path resolveOutputFile() {
        Path cwd = Path.of(System.getProperty("user.dir"));
        if (cwd.endsWith("ShortJobFirst")) {
            return cwd.resolve(RESULT_FILE_NAME);
        }
        return cwd.resolve("ShortJobFirst").resolve(RESULT_FILE_NAME);
    }

    // Método para escribir el reporte completo a un archivo de texto
    private static void writeReportToFile(Path file, String content) {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("No se puede escribir los resultados al archivo: " + e.getMessage());
        }
    }
}
