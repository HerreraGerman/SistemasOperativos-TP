package ShortJobFirst;

public class ProcessResultTable {

    public static java.util.List<String> getBurstLines(ProcessSimulation[] processes, String title) {
        int nameWidth = "Process".length();
        int arrivalWidth = "Arrival".length();
        int burstWidth = "Burst".length();

        if (processes != null) {
            for (ProcessSimulation process : processes) {
                if (process != null) {
                    nameWidth = Math.max(nameWidth, process.getProcessName().length());
                    arrivalWidth = Math.max(arrivalWidth, String.valueOf(process.getArrivalTime()).length());
                    burstWidth = Math.max(burstWidth, String.valueOf(process.getProcessTime()).length());
                }
            }
        }

        String format = String.format("| %%-%ds | %%%ds | %%%ds |", nameWidth, arrivalWidth, burstWidth);
        String border = "+" + repeat('-', nameWidth + 2) + "+" + repeat('-', arrivalWidth + 2) + "+" + repeat('-', burstWidth + 2) + "+";

        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add(title);
        lines.add(border);
        lines.add(String.format(format, "Process", "Arrival", "Burst"));
        lines.add(border);

        if (processes != null) {
            for (ProcessSimulation process : processes) {
                if (process != null) {
                    lines.add(String.format(format,
                            process.getProcessName(),
                            process.getArrivalTime(),
                            process.getProcessTime()));
                }
            }
        }

        lines.add(border);
        lines.add("");
        return lines;
    }

    public static java.util.List<String> getScheduleLines(java.util.List<ProcessSimulation> schedule, String title) {
        int nameWidth = "Process".length();
        int arrivalWidth = "Arrival".length();
        int burstWidth = "Burst".length();
        int startWidth = "Start".length();
        int finishWidth = "Finish".length();
        int waitingWidth = "Wait".length();
        int turnaroundWidth = "Turnaround".length();
        int responseWidth = "Response".length();

        if (schedule != null) {
            for (ProcessSimulation process : schedule) {
                if (process != null) {
                    nameWidth = Math.max(nameWidth, process.getProcessName().length());
                    arrivalWidth = Math.max(arrivalWidth, String.valueOf(process.getArrivalTime()).length());
                    burstWidth = Math.max(burstWidth, String.valueOf(process.getProcessTime()).length());
                    startWidth = Math.max(startWidth, String.valueOf(process.getStartTime()).length());
                    finishWidth = Math.max(finishWidth, String.valueOf(process.getFinishTime()).length());
                    waitingWidth = Math.max(waitingWidth, String.valueOf(process.getWaitingTime()).length());
                    turnaroundWidth = Math.max(turnaroundWidth, String.valueOf(process.getTurnaroundTime()).length());
                    responseWidth = Math.max(responseWidth, String.valueOf(process.getResponseTime()).length());
                }
            }
        }

        String format = String.format("| %%-%ds | %%%ds | %%%ds | %%%ds | %%%ds | %%%ds | %%%ds | %%%ds |",
                nameWidth, arrivalWidth, burstWidth, startWidth, finishWidth, waitingWidth, turnaroundWidth, responseWidth);
        String border = "+" + repeat('-', nameWidth + 2) + "+" + repeat('-', arrivalWidth + 2) + "+" + repeat('-', burstWidth + 2)
                + "+" + repeat('-', startWidth + 2) + "+" + repeat('-', finishWidth + 2) + "+" + repeat('-', waitingWidth + 2)
                + "+" + repeat('-', turnaroundWidth + 2) + "+" + repeat('-', responseWidth + 2) + "+";

        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add(title);
        lines.add(border);
        lines.add(String.format(format, "Process", "Arrival", "Burst", "Start", "Finish", "Wait", "Turnaround", "Response"));
        lines.add(border);

        if (schedule != null) {
            for (ProcessSimulation process : schedule) {
                if (process != null) {
                    lines.add(String.format(format,
                            process.getProcessName(),
                            process.getArrivalTime(),
                            process.getProcessTime(),
                            process.getStartTime(),
                            process.getFinishTime(),
                            process.getWaitingTime(),
                            process.getTurnaroundTime(),
                            process.getResponseTime()));
                }
            }
        }

        lines.add(border);
        lines.add("");
        return lines;
    }

    public static void printLines(java.util.List<String> lines) {
        if (lines == null) {
            return;
        }
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public static java.util.List<String> getSideBySideLines(ProcessSimulation[] left, String leftTitle,
                                                            ProcessSimulation[] right, String rightTitle) {
        java.util.List<String> leftLines = getBurstLines(left, leftTitle);
        java.util.List<String> rightLines = getBurstLines(right, rightTitle);

        int leftWidth = 0;
        for (String line : leftLines) {
            leftWidth = Math.max(leftWidth, line.length());
        }

        String gap = "   ";
        int maxLines = Math.max(leftLines.size(), rightLines.size());
        java.util.List<String> combined = new java.util.ArrayList<>(maxLines + 1);

        for (int i = 0; i < maxLines; i++) {
            String leftLine = i < leftLines.size() ? leftLines.get(i) : repeat(' ', leftWidth);
            String rightLine = i < rightLines.size() ? rightLines.get(i) : "";
            combined.add(leftLine + gap + rightLine);
        }
        combined.add("");
        return combined;
    }

    private static String repeat(char ch, int count) {
        int length = Math.max(0, count);
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(ch);
        }
        return builder.toString();
    }
}

