package ShortJobFirst;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Simulation {
    private static final int DEFAULT_RUN_COUNT = 3;
    private static final int DEFAULT_PROCESS_COUNT = 5;
    private static final int DEFAULT_MIN_TIME = 1;
    private static final int DEFAULT_MAX_TIME = 10;
    private static final String RESULT_FILE_NAME = "simulation_results.txt";

    public static void main(String[] args) {
        int runCount = parseIntArg(args, 0, DEFAULT_RUN_COUNT);
        int processCount = parseIntArg(args, 1, DEFAULT_PROCESS_COUNT);
        StringBuilder report = new StringBuilder();

        for (int run = 1; run <= runCount; run++) {
            ProcessSimulation[] processes = createRandomProcesses(processCount, DEFAULT_MIN_TIME, DEFAULT_MAX_TIME);
            ProcessSimulation[] rawProcesses = processes.clone();

            String runHeader = "Run " + run + " of " + runCount + ":";
            println(report, runHeader);

            java.util.Arrays.sort(processes, java.util.Comparator.comparingInt(ProcessWithRandomTime::getProcessTime));
            java.util.List<String> sideBySide = ProcessResultTable.getSideBySideLines(rawProcesses, "Raw process times:",
                                                                                     processes, "Sorted by Shortest Job First:");
            for (String line : sideBySide) {
                println(report, line);
            }

            if (run < runCount) {
                String separator = "".repeat(50);
                println(report, separator);
            }
        }

        Path outputFile = resolveOutputFile();
        writeReportToFile(outputFile, report.toString());
        System.out.println("Results saved to " + outputFile.toAbsolutePath());
    }

    private static ProcessSimulation[] createRandomProcesses(int count, int minTime, int maxTime) {
        ProcessSimulation[] processes = new ProcessSimulation[count];
        for (int i = 0; i < count; i++) {
            processes[i] = new ProcessSimulation("Process " + (i + 1));
            processes[i].generateRandomProcessTime(minTime, maxTime);
        }
        return processes;
    }

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

    private static void println(StringBuilder report, String text) {
        System.out.println(text);
        report.append(text).append(System.lineSeparator());
    }

    private static Path resolveOutputFile() {
        Path cwd = Path.of(System.getProperty("user.dir"));
        if (cwd.endsWith("ShortJobFirst")) {
            return cwd.resolve(RESULT_FILE_NAME);
        }
        return cwd.resolve("ShortJobFirst").resolve(RESULT_FILE_NAME);
    }

    private static void writeReportToFile(Path file, String content) {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Unable to write results to file: " + e.getMessage());
        }
    }
}
