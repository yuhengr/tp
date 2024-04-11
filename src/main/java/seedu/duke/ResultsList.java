//@@author cyhjason29
package seedu.duke;

import java.util.ArrayList;

/**
 * Deals with the list of all results for all attempts.
 */
public class ResultsList {
    protected ArrayList<Results> sessionResults;
    protected ArrayList<Integer> topicsChosen;

    /**
     * Constructs the ResultsList class.
     */
    public ResultsList() {
        sessionResults = new ArrayList<>();
        topicsChosen = new ArrayList<>();
    }

    /**
     * Adds a result to the list of results.
     *
     * @param roundResults Result for a particular attempt.
     */
    public void addResults(Results roundResults) {
        sessionResults.add(roundResults);
    }

    /**
     * Adds the topic number.
     *
     * @param topicNumber Topic number for a particular attempt.
     */
    public void addQuestions(Integer topicNumber) {
        topicsChosen.add(topicNumber);
    }

    /**
     * Gets a specified result.
     *
     * @param index Attempt number.
     * @return Result for the attempt.
     * @throws IndexOutOfBoundsException If the attempt does not exist.
     */
    public Results getSpecifiedResult(int index) throws IndexOutOfBoundsException {
        return sessionResults.get(index);
    }

    /**
     * Gets a specified topic number
     *
     * @param index Attempt number.
     * @return Topic number for the attempt.
     * @throws IndexOutOfBoundsException If the attempt does not exist.
     */
    public Integer getTopicNum(int index) throws IndexOutOfBoundsException {
        return topicsChosen.get(index);
    }

    /**
     * Get the total number of results.
     *
     * @return Total number of results.
     */
    public int getSizeOfAllResults() {
        return sessionResults.size();
    }

    /**
     * Clears all results.
     */
    public void clearResults() {
        sessionResults.clear();
    }

    /**
     * Retrieve the list of all results.
     *
     * @return List of all results.
     */
    public ArrayList<Results> getSessionResults() {
        return sessionResults;
    }

    /**
     * Retrieve the list of topic numbers of all attempts.
     *
     * @return List of topic numbers of all attempts.
     */
    public ArrayList<Integer> getTopicsChosen() {
        return topicsChosen;
    }
}
