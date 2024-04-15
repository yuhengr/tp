//@@author hongyijie06
package seedu.duke;

/**
 * handles the topics
 */
public class Topic {

    protected QuestionsList chosenQuestionsList;
    protected String topicName;
    protected boolean hasAttemptedStatus;
    protected String summary;

    /**
     * constructor for the Topic class
     *
     * @param chosenQuestionsList the question list of a topic
     * @param topicName the name of the topic
     * @param hasAttemptedStatus if the topic has been attempted
     * @param summary
     */
    public Topic(QuestionsList chosenQuestionsList, String topicName, boolean hasAttemptedStatus, String summary) {
        this.chosenQuestionsList = chosenQuestionsList;
        this.topicName = topicName;
        this.hasAttemptedStatus = hasAttemptedStatus;
        this.summary = summary;
    }

    /**
     * check if topic has been attempted
     * @return attempt status of the topic
     */
    public boolean hasAttempted() {
        return this.hasAttemptedStatus;
    }

    /**
     * marks topic as attempted
     */
    public void markAsAttempted() {
        this.hasAttemptedStatus = true;
    }

    public String toString(){
        String status = this.hasAttempted() ? "Attempted" : "Not attempted";
        return "[" + status + "]" + topicName;
    }

    /**
     * gets topic name
     * @return name of the topic
     */
    public String getTopicName() {
        return topicName;
    }
}
