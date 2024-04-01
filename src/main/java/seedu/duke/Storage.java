package seedu.duke;

import seedu.duke.exceptions.CustomException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with loading from the save file and saving in that file
 */
public class Storage {
    private static final String RESULTS_HEADER = "result";
    private static final String TOPIC_HEADER = "topic";
    private static final String ANSWER_HEADER = "answer";
    private static final String CORRECTNESS_HEADER = "correctness";
    private static final int STARTING_INDEX_TOPIC = 6;
    private static final int STARTING_INDEX_RESULT = 7;
    private static final int STARTING_INDEX_ANSWER = 7;
    private static final int STARTING_INDEX_CORRECTNESS = 12;
    private static final int FIRST_ANSWER = 0;
    private static final int RESULTS_INDEX = 0;
    private static final int TOPIC_NUMBER_INDEX = 1;
    private static final int INDEX_NUMBER_OF_CORRECT_ANSWERS = 0;
    private static final int INDEX_TOTAL_NUMBER_OF_QUESTIONS = 1;
    private static final int INDEX_SCORE = 2;
    private static final String RESULTS_SEPARATOR = "\\+";
    private static final String ARG_SEPARATOR = "\\|";
    private static final String FILE_PATH = "data/player2113.txt";
    private static final String FOLDER_NAME = "data";
    private static final String MESSAGE_ERROR_INIT = "There was an error initiating the save file.";
    private static final String MESSAGE_ERROR_WRITING = "There was an error writing to the save file.";


    /**
     * Constructs the Storage object.
     */
    public Storage() {

    }

    public void loadProgress(File f, ResultsList results, TopicList topics, AnswerTracker answers)
            throws FileNotFoundException {
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String line = s.nextLine();
            processLine(line, results, topics, answers);
        }
    }

    private static void processLine(String line, ResultsList results, TopicList topics, AnswerTracker answers) {
        if (line.startsWith(RESULTS_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_RESULT).split(ARG_SEPARATOR);
            
            String result = processedLine[RESULTS_INDEX].trim();
            Results temp = createResults(result);
            results.addResults(temp);

            int topicNum = Integer.parseInt(processedLine[TOPIC_NUMBER_INDEX].trim());
            results.addQuestions(topicNum);
        } else if (line.startsWith(TOPIC_HEADER)) {
            String topicName = line.substring(STARTING_INDEX_TOPIC).trim();
            ArrayList<Topic> topicList = topics.getTopicList();
            for (Topic topic : topicList) {
                if (topic.getTopicName().equals(topicName)) {
                    topic.markAsAttempted();
                    break;
                }
            }
        } else if (line.startsWith(ANSWER_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_ANSWER).split(ARG_SEPARATOR);
            ArrayList<String> userAnswers = new ArrayList<>();
            for (String answer : processedLine) {
                userAnswers.add(answer.trim());
            }
            answers.addUserAnswers(userAnswers);
        } else if (line.startsWith(CORRECTNESS_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_CORRECTNESS).split(ARG_SEPARATOR);
            ArrayList<Boolean> answerCorrectness = new ArrayList<>();
            for (String correctness : processedLine) {
                answerCorrectness.add(Boolean.parseBoolean(correctness.trim()));
            }
            answers.addUserCorrectness(answerCorrectness);
        }
    }

    private static Results createResults(String result) {
        String[] processedResult = result.split(RESULTS_SEPARATOR);
        Results temp = new Results();
        int numberOfCorrectAnswers = Integer.parseInt(processedResult[INDEX_NUMBER_OF_CORRECT_ANSWERS].trim());
        temp.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        int totalNumberOfQuestions = Integer.parseInt(processedResult[INDEX_TOTAL_NUMBER_OF_QUESTIONS].trim());
        temp.setTotalNumberOfQuestions(totalNumberOfQuestions);
        String score = processedResult[INDEX_SCORE].trim();
        temp.setScore(score);
        return temp;
    }

    public void initSaveFile(File file) throws CustomException {
        try {
            new File(FOLDER_NAME).mkdir();
            file.createNewFile();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_INIT);
        }
    }

    public void saveProgress(ResultsList results, TopicList topics, AnswerTracker answers)
            throws CustomException {
        try {
            writeToFile(results, topics, answers);
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_WRITING);
        }
    }

    private static void writeToFile(ResultsList results, TopicList topics, AnswerTracker answers)
            throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_PATH);

        ArrayList<Results> resultList = results.getSessionResults();
        ArrayList<Integer> topicsChosen = results.getTopicsChosen();
        for (int i = 0; i < resultList.size(); i++) {
            Results result = resultList.get(i);
            int numberOfCorrectAnswers = result.getNumberOfCorrectAnswers();
            int totalNumberOfQuestions = result.getTotalNumberOfQuestions();
            String score = result.getScore();
            int topicNum = topicsChosen.get(i);
            fileWriter.write("result " + numberOfCorrectAnswers + " + " + totalNumberOfQuestions + " + " +
                    score + " | " + topicNum + System.lineSeparator());
        }

        ArrayList<Topic> topicList = topics.getTopicList();
        for (int i = 0; i < topicList.size(); i++) {
            Topic topic = topicList.get(i);
            if (topic.hasAttempted()) {
                fileWriter.write("topic " + topic.getTopicName() + System.lineSeparator());
            }
        }

        ArrayList<ArrayList<String>> userAnswers = answers.getAllAnswers();
        ArrayList<ArrayList<Boolean>> isCorrect = answers.getAllCorrectness();
        for (int i = 0; i < userAnswers.size(); i++) {
            StringBuilder listOfAnswers = new StringBuilder();
            StringBuilder listOfCorrectness = new StringBuilder();

            listOfAnswers.append(userAnswers.get(i).get(FIRST_ANSWER));
            listOfCorrectness.append(isCorrect.get(i).get(FIRST_ANSWER));
            for (int j = 1; j < userAnswers.get(i).size(); j++) {
                String answer = userAnswers.get(i).get(j);
                boolean correctness = isCorrect.get(i).get(j);
                listOfAnswers.append(" | ").append(answer);
                listOfCorrectness.append(" | ").append(correctness);
            }
            fileWriter.write("answer " + listOfAnswers + System.lineSeparator());
            fileWriter.write("correctness " + listOfCorrectness + System.lineSeparator());
        }

        fileWriter.close();
    }
}
