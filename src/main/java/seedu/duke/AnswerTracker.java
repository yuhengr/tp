//@@author cyhjason29
package seedu.duke;

import java.util.ArrayList;

/**
 * Deals with all user answers and user answer correctness.
 */
public class AnswerTracker {
    protected ArrayList<ArrayList<String>> userAnswers;
    protected ArrayList<ArrayList<Boolean>> isCorrect;

    /**
     * Constructs the AnswerTracker class.
     */
    public AnswerTracker() {
        userAnswers = new ArrayList<>();
        isCorrect = new ArrayList<>();
    }

    /**
     * Get the user answer to a specific question of an attempt.
     *
     * @param index The question number.
     * @param attemptNumber The attempt number.
     * @return User answer.
     */
    public String getUserAnswers(int index, int attemptNumber) {
        return userAnswers.get(attemptNumber).get(index);
    }

    /**
     * Get the user answer correctness to a specific question of an attempt.
     *
     * @param index The question number.
     * @param attemptNumber The attempt number.
     * @return User answer correctness.
     */
    public Boolean getIsCorrect(int index, int attemptNumber) {
        return isCorrect.get(attemptNumber).get(index);
    }

    /**
     * Get the list of all user answers.
     *
     * @return List of all user answers.
     */
    public ArrayList<ArrayList<String>> getAllAnswers() {
        return userAnswers;
    }

    /**
     * Get the list of all user answer correctness.
     *
     * @return List of all user answer correctness.
     */
    public ArrayList<ArrayList<Boolean>> getAllCorrectness() {
        return isCorrect;
    }

    /**
     * Add user answers within an attempt to the list of all user answers.
     *
     * @param answers User answers within an attempt.
     */
    public void addUserAnswers(ArrayList<String> answers) {
        userAnswers.add(answers);
    }

    /**
     * Add user answer correctness within an attempt to the list of all user answers.
     *
     * @param correctness User answer correctness within an attempt.
     */
    public void addUserCorrectness(ArrayList<Boolean> correctness) {
        isCorrect.add(correctness);
    }
}
