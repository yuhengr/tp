package seedu.duke;

import java.util.logging.Logger;
import java.util.logging.Level;

public class ProgressManager {

    private ResultsList sessionResults;

    private int customModeGoal;

    private boolean inCustomMode;

    private Logger logger = Logger.getLogger("ProgressManagerLogger");

    public ProgressManager(ResultsList sessionResults) {
        this.sessionResults = sessionResults;
        this.customModeGoal = 0;
        this.inCustomMode = false;
    }

    public ResultsList clearProgress() {
        logger.log(Level.INFO, "Clearing session progress.");
        sessionResults.clearResults();
        return sessionResults;
    }

    public void setCustomMode() {
        inCustomMode = true;
    }

    public void clearCustomMode() {
        inCustomMode = false;
    }

    public boolean isInCustomMode() {
        return inCustomMode;
    }

    public void setCustomModeGoal(int goal) {
        customModeGoal = goal;
    }

    public void clearCustomModeGoal() {
        customModeGoal = 0;
    }

    public int getCustomModeGoal() {
        return customModeGoal;
    }
}
