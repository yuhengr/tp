package seedu.duke;

import java.util.logging.Logger;
import java.util.logging.Level;

public class ProgressManager {

    private ResultsList sessionResults;

    private int checkpointModeGoal;

    private boolean inCheckpointMode;

    private int numOfAttemptedCustomQuestions;

    private Logger logger = Logger.getLogger("ProgressManagerLogger");

    public ProgressManager(ResultsList sessionResults) {
        this.sessionResults = sessionResults;
        this.checkpointModeGoal = 0;
        this.inCheckpointMode = false;
        this.numOfAttemptedCustomQuestions = 0;
    }

    public ResultsList clearProgress() {
        logger.log(Level.INFO, "Clearing session progress.");
        sessionResults.clearResults();
        return sessionResults;
    }

    public void setCheckpointMode() {
        inCheckpointMode = true;
    }

    public void clearCheckpointMode() {
        inCheckpointMode = false;
    }

    public boolean isInCheckpointMode() {
        return inCheckpointMode;
    }

    public void setCheckpointModeGoal(int goal) {
        checkpointModeGoal = goal;
    }

    public void clearCheckpointModeGoal() {
        checkpointModeGoal = 0;
    }

    public int getCheckpointModeGoal() {
        return checkpointModeGoal;
    }

    public void incrementNumOfAttemptedCustomQuestions() {
        numOfAttemptedCustomQuestions++;
    }

    public int getNumOfAttemptedCustomQuestions() {
        return numOfAttemptedCustomQuestions;
    }

    public void clearNumOfAttemptedCustomQuestions() {
        numOfAttemptedCustomQuestions = 0;
    }
}
