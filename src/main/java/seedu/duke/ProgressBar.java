package seedu.duke;

public class ProgressBar {
    private int totalSteps;
    private int completedSteps;

    public ProgressBar(int totalSteps) {
        this.totalSteps = totalSteps;
        this.completedSteps = 0;
    }

    public ProgressBar(int totalSteps, int completedSteps) {
        this.totalSteps = totalSteps;
        this.completedSteps = completedSteps;
    }

    public void increment() {
        completedSteps = completedSteps + 1;
        progressPercentage(completedSteps, totalSteps);
    }

    public void display() {
        progressPercentage(completedSteps, totalSteps);
    }

    private static void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10; // 10unit for 100%
        int remainProcent = ((100 * remain) / total) / maxBareSize;
        char defaultChar = '-';
        String icon = "*";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("[");
        for (int i = 0; i < remainProcent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainProcent, bare.length());
        System.out.print("\r" + bareDone + bareRemain + " " + remainProcent * 10 + "%");
        if (remain == total) {
            System.out.print("\n");
        }
    }
}
