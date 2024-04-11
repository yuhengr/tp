//@@author cyhjason29
package seedu.duke;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Contains the result of a user for a single attempt.
 */
public class Results {
    private static final int HUNDRED_PERCENT = 100;
    private static final int ZERO_QUESTIONS = 0;

    private static Logger logger = Logger.getLogger("ResultsLogger");

    protected int numberOfCorrectAnswers;
    protected int totalNumberOfQuestions;
    protected String score;

    /**
     * Constructs the Results class.
     */
    public Results() {
        numberOfCorrectAnswers = ZERO_QUESTIONS;
        totalNumberOfQuestions = ZERO_QUESTIONS;
        score = "";
    }

    /**
     * Calculates the score for the attempt.
     */
    public void calculateScore() {
        LogManager.getLogManager().reset();
        logger.setLevel(Level.ALL);
        logger.log(Level.INFO, "going to start calculating score");
        int scorePercentage = (int) ((double) numberOfCorrectAnswers / (double) totalNumberOfQuestions *
                HUNDRED_PERCENT);
        assert scorePercentage >= 0;
        score = numberOfCorrectAnswers + "/" + totalNumberOfQuestions + " (" + scorePercentage + "%)";
        logger.log(Level.INFO,"end of calculation");
    }

    /**
     * Get the number of correct answers to an attempt.
     *
     * @return Number of correct answers.
     */
    public int getNumberOfCorrectAnswers() {
        return numberOfCorrectAnswers;
    }

    /**
     * Get the total number of questions to an attempt.
     *
     * @return Total number of questions.
     */
    public int getTotalNumberOfQuestions() {
        return totalNumberOfQuestions;
    }

    /**
     * Get the score to an attempt.
     *
     * @return Score.
     */
    public String getScore() {
        return score;
    }

    /**
     * Increase the number of correct answers by 1.
     */
    public void increaseCorrectAnswers() {
        numberOfCorrectAnswers++;
    }

    /**
     * Increase the total number of questions by 1.
     */
    public void increaseNumberOfQuestions() {
        totalNumberOfQuestions++;
    }

    /**
     * Set the total number of questions.
     *
     * @param totalNumberOfQuestions Total number of questions.
     */
    public void setTotalNumberOfQuestions(int totalNumberOfQuestions) {
        this.totalNumberOfQuestions = totalNumberOfQuestions;
    }

    /**
     * Set the number of correct answers.
     *
     * @param numberOfCorrectAnswers Number of correct answers.
     */
    public void setNumberOfCorrectAnswers(int numberOfCorrectAnswers) {
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
    }

    /**
     * Set the score.
     *
     * @param score Score.
     */
    public void setScore(String score) {
        this.score = score;
    }
}
