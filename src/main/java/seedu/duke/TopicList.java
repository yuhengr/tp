package seedu.duke;


import java.util.ArrayList;

/**
 * handles the list of topics
 */
public class TopicList {
    private ArrayList<Topic> topicList;

    private ProgressBar topicProgressBar;

    /**
     * constructor for TopicList class
     */
    public TopicList() {
        topicList = new ArrayList<>();
        topicProgressBar = new ProgressBar(0);
    }

    /**
     * add a topic to list of topics
     * @param topic the topic to be added
     */
    public void addTopic(Topic topic){
        topicList.add(topic);
    }

    /**
     * retrieves topic of interest
     * @param index the index of the topic to get in the topicList
     * @return the name of the topic
     */
    public String getTopic(int index){
        return topicList.get(index).topicName;
    }

    //@@author songyuew
    public String[][] listAllTopics() {
        int commandNum = topicList.size();
        String[][] tableData = new String[commandNum][];
        for (int i = 0; i < commandNum; i++) {
            tableData[i] = new String[]{
                    String.valueOf(i + 1),
                    topicList.get(i).topicName,
                    topicList.get(i).summary,
                    String.valueOf(topicList.get(i).hasAttempted())
            };
        }
        return tableData;
    }

    /**
     * number of topics in the list of topics
     * @return total number of topics
     */
    public int getSize() {
        return topicList.size();
    }

    /**
     * gets Topic topic
     * @param index the index of the topic of interest in the list of topics
     * @return the topic of interest
     */
    public Topic get(int index){
        return topicList.get(index);
    }

    public ArrayList<Topic> getTopicList() {
        return topicList;
    }

    //@@author songyuew
    public void displayProgressBar() {
        int attempted = 0;
        for (Topic t : topicList) {
            if (t.hasAttempted()) {
                attempted += 1;
            }
        }
        topicProgressBar = new ProgressBar(topicList.size(), attempted);
        topicProgressBar.display();
        System.out.print(" " + attempted + "/" + topicList.size() + " topics attempted");
        System.out.println();
    }

}
