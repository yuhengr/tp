//@@author yuhengr
package seedu.duke;

import seedu.duke.exceptions.CustomException;

public enum CommandList {

    TOPIC, HELP, SOLUTION, EXPLAIN, RESULTS, TIMED_MODE, BYE, CUSTOM, CHECKPOINT, CLEAR, INVALID;

    private static final String PATTERN_TOPIC = "(?i)topic\\s+(\\d*)";

    private static final String PATTERN_BYE = "(?i)bye";

    private static final String PATTERN_SOLUTION = "(?i)solution\\s+(\\d+)\\s*(.*)";

    private static final String PATTERN_CUSTOM = "(?i)custom\\s+(\\d+)\\s+(\\d+)";
    private static final String PATTERN_CHECKPOINT = "(?i)checkpoint\\s+(\\d+)";

    private static final String PATTERN_EXPLAIN = "(?i)explain\\s+(\\d+)\\s*(.*)";

    private static final String PATTERN_HELP = "(?i)help\\s*(\\w*)";

    private static final String PATTERN_RESULTS = "(?i)results\\s*(\\d+)";
    private static final String PATTERN_CLEAR = "^(?i)clear$";
    private static final int FIRST_COMMAND_PARAM_INDEX = 0;

    public static String getTopicPattern() {
        return PATTERN_TOPIC;
    }

    public static String getSolutionPattern() {
        return PATTERN_SOLUTION;
    }

    public static String getExplainPattern() {
        return PATTERN_EXPLAIN;
    }

    public static String getCustomPattern() {
        return PATTERN_CUSTOM;
    }

    public static String getClearPattern() {
        return PATTERN_CLEAR;
    }

    public static String getCheckpointPattern() {
        return PATTERN_CHECKPOINT;
    }

    /**
     * Parses the command given by the user and extracts the main command as a token.
     *
     * @param command           User command given as input.
     * @return                  A command token representing the main command.
     * @throws CustomException  if command given is invalid.
     */
    public static CommandList getCommandToken(String command) throws CustomException {
        String[] splitCommand = command.split(" ");
        String mainCommand;

        try {
            mainCommand = splitCommand[FIRST_COMMAND_PARAM_INDEX].toLowerCase();
        } catch (ArrayIndexOutOfBoundsException error) {
            throw new CustomException("That's an invalid command");
        }

        if (mainCommand.contentEquals("topic")) {
            return TOPIC;
        }
        if (mainCommand.contentEquals("help")) {
            return HELP;
        }
        if (mainCommand.contentEquals("solution")) {
            return SOLUTION;
        }
        if (mainCommand.contentEquals("custom")) {
            return CUSTOM;
        }
        if (mainCommand.contentEquals("checkpoint")) {
            return CHECKPOINT;
        }
        if (mainCommand.contentEquals("explain")) {
            return EXPLAIN;
        }
        if (mainCommand.contentEquals("results")) {
            return RESULTS;
        }
        if (mainCommand.contentEquals("timed mode")) {
            return TIMED_MODE;
        }
        if (mainCommand.contentEquals("bye")) {
            return BYE;
        }
        if (mainCommand.contentEquals("clear")) {
            return CLEAR;
        }
        return INVALID;
    }
}
