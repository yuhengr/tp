package seedu.duke;

public enum CommandList {

    TOPIC, HELP, SOLUTION, EXPLAIN, RESULTS, TIMED_MODE, BYE, CUSTOM, INVALID;

    private static final String PATTERN_TOPIC = "(?i)topic\\s*(\\d*)";

    private static final String PATTERN_BYE = "(?i)bye";

    private static final String PATTERN_SOLUTION = "(?i)solution\\s*(\\d+)\\s*(.*)";

    private static final String PATTERN_CUSTOM = "(?i)custom";

    private static final String PATTERN_EXPLANATION = "(?i)explanation\\s*(\\d+)\\s*(\\d+)";

    private static final String PATTERN_HELP = "(?i)help\\s*(\\w*)";

    private static final String PATTERN_RESULTS = "(?i)results\\s*(\\d+)";

    public static String getTopicPattern() {
        return PATTERN_TOPIC;
    }

    public static String getSolutionPattern() {
        return PATTERN_SOLUTION;
    }

    public static CommandList getCommandToken(String command) {
        String[] splitCommand = command.split(" ");
        String mainCommand = splitCommand[0].toLowerCase();

        if (mainCommand.contentEquals("topic")) {
            return TOPIC;
        } else if (mainCommand.contentEquals("help")) {
            return HELP;
        } else if (mainCommand.contentEquals("solution")) {
            return SOLUTION;
        } else if (mainCommand.contentEquals("custom")) {
            return CUSTOM;
        } else if (mainCommand.contentEquals("explanation")) {
            return EXPLAIN;
        } else if (mainCommand.contentEquals("results")) {
            return RESULTS;
        } else if (mainCommand.contentEquals("timed mode")) {
            return TIMED_MODE;
        } else if (mainCommand.contentEquals("bye")) {
            return BYE;
        } else {
            return INVALID;
        }
    }
}
