package seedu.duke;


import java.util.ArrayList;

public class TopicList {
    private ArrayList<Topic> topicList;

    private ProgressBar topicProgressBar;

    public TopicList() {
        topicList = new ArrayList<>();
        topicProgressBar = new ProgressBar(0);
    }
    public void addTopic(Topic topic){
        topicList.add(topic);
    }

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

    public int getSize() {
        return topicList.size();
    }

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
