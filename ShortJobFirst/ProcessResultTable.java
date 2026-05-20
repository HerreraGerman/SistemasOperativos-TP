package ShortJobFirst;

public class ProcessResultTable {

    public static void print(ProcessWithRandomTime[] processes, String title) {
        java.util.List<String> lines = buildTableLines(processes, title);
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println();
    }

    public static java.util.List<String> getTableLines(ProcessWithRandomTime[] processes, String title) {
        return buildTableLines(processes, title);
    }

    public static void printSideBySide(ProcessWithRandomTime[] left, String leftTitle,
                                       ProcessWithRandomTime[] right, String rightTitle) {
        for (String line : getSideBySideLines(left, leftTitle, right, rightTitle)) {
            System.out.println(line);
        }
        System.out.println();
    }

    public static java.util.List<String> getSideBySideLines(ProcessWithRandomTime[] left, String leftTitle,
                                                            ProcessWithRandomTime[] right, String rightTitle) {
        java.util.List<String> leftLines = buildTableLines(left, leftTitle);
        java.util.List<String> rightLines = buildTableLines(right, rightTitle);

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

    private static java.util.List<String> buildTableLines(ProcessWithRandomTime[] processes, String title) {
        int nameWidth = "Process".length();
        int timeWidth = "Time".length();

        if (processes != null) {
            for (ProcessWithRandomTime process : processes) {
                if (process != null) {
                    nameWidth = Math.max(nameWidth, process.getProcessName().length());
                    timeWidth = Math.max(timeWidth, String.valueOf(process.getProcessTime()).length());
                }
            }
        }

        String format = String.format("| %%-%ds | %%%ds |", nameWidth, timeWidth);
        String border = "+" + repeat('-', nameWidth + 2) + "+" + repeat('-', timeWidth + 2) + "+";

        java.util.List<String> lines = new java.util.ArrayList<>();
        lines.add(title);
        lines.add(border);
        lines.add(String.format(format, "Process", "Time"));
        lines.add(border);

        if (processes != null) {
            for (ProcessWithRandomTime process : processes) {
                if (process != null) {
                    lines.add(String.format(format, process.getProcessName(), process.getProcessTime()));
                }
            }
        }

        lines.add(border);
        return lines;
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

