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
    private static final String PAUSE_HEADER = "pause";
    private static final String TEMP_RESULT_HEADER = "temp_result";
    private static final String TEMP_ANSWER_HEADER = "temp_answer";
    private static final String TEMP_CORRECTNESS_HEADER = "temp_correctness";
    private static final int STARTING_INDEX_TOPIC = 6;
    private static final int STARTING_INDEX_RESULT = 7;
    private static final int STARTING_INDEX_ANSWER = 7;
    private static final int STARTING_INDEX_CORRECTNESS = 12;
    private static final int STARTING_INDEX_TEMP_RESULT = 12;
    private static final int STARTING_INDEX_TEMP_ANSWER = 12;
    private static final int STARTING_INDEX_TEMP_CORRECTNESS = 17;
    private static final int STARTING_INDEX_TOPIC_NUM = 6;
    private static final int FIRST_ANSWER = 0;
    private static final int RESULTS_INDEX = 0;
    private static final int TOPIC_NUMBER_INDEX = 1;
    private static final int INDEX_NUMBER_OF_CORRECT_ANSWERS = 0;
    private static final int INDEX_TOTAL_NUMBER_OF_QUESTIONS = 1;
    private static final int INDEX_SCORE = 2;
    private static final int INDEX_TOPIC_NUM = 0;
    private static final int INDEX_INDEX = 1;
    private static final int TWO_PARAMETERS = 2;
    private static final int TOTAL_QUESTIONS_PARAMETERS = 7;
    private static final int TOTAL_OPTIONS = 4;
    private static final int QUESTION_PARAMETER = 0;
    private static final int SOLUTION_PARAMETER = 1;
    private static final int EXPLANATION_PARAMETER = 2;
    private static final int OPTIONS_START_PARAMETER = 3;
    private static final String RESULTS_SEPARATOR = "\\+";
    private static final String ARG_SEPARATOR = "\\|";
    private static final String FILE_PATH = "data/player2113.txt";
    private static final String FOLDER_NAME = "data";
    private static final String MESSAGE_ERROR_INIT = "There was an error initiating the save file.";
    private static final String MESSAGE_ERROR_WRITING = "There was an error writing to the save file.";

    private static boolean isPaused;


    /**
     * Constructs the Storage object.
     */
    public Storage() {
        isPaused = false;
    }

    public boolean loadProgress(File f, ResultsList results, TopicList topics, AnswerTracker answers)
            throws FileNotFoundException {
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String line = s.nextLine();
            processLine(line, results, topics, answers);
        }
        return isPaused;
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
        } else if (line.startsWith(PAUSE_HEADER)) {
            isPaused = true;
        }
    }

    public int[] resumeGame(File file, Results topicResults, ArrayList<String> userAnswers,
                            ArrayList<Boolean> correctness)
            throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int[] pausedQuestion = new int[TWO_PARAMETERS];
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith(PAUSE_HEADER)) {
                pausedQuestion = getPausedQuestion(line.substring(STARTING_INDEX_TOPIC_NUM).trim());
            } else if (line.startsWith(TEMP_RESULT_HEADER)) {
                createTempResult(topicResults, line.substring(STARTING_INDEX_TEMP_RESULT).trim());
            } else if (line.startsWith(TEMP_ANSWER_HEADER)) {
                createTempAnswers(userAnswers, line.substring(STARTING_INDEX_TEMP_ANSWER).trim());
            } else if (line.startsWith(TEMP_CORRECTNESS_HEADER)) {
                createTempCorrectness(correctness, line.substring(STARTING_INDEX_TEMP_CORRECTNESS).trim());
            }
        }
        return pausedQuestion;
    }

    private static int[] getPausedQuestion(String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        int[] pausedQuestion = new int[TWO_PARAMETERS];
        pausedQuestion[INDEX_TOPIC_NUM] = Integer.parseInt(processedLine[INDEX_TOPIC_NUM].trim());
        pausedQuestion[INDEX_INDEX] = Integer.parseInt(processedLine[INDEX_INDEX].trim());
        return pausedQuestion;
    }

    private static void createTempResult(Results topicResults, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        int numberOfCorrectAnswers = Integer.parseInt(processedLine[INDEX_NUMBER_OF_CORRECT_ANSWERS].trim());
        topicResults.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        int totalNumberOfQuestions = Integer.parseInt(processedLine[INDEX_TOTAL_NUMBER_OF_QUESTIONS].trim());
        topicResults.setTotalNumberOfQuestions(totalNumberOfQuestions);
    }

    private static void createTempAnswers(ArrayList<String> userAnswers, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        for (String answer : processedLine) {
            answer = answer.trim();
            userAnswers.add(answer);
        }
    }

    private static void createTempCorrectness(ArrayList<Boolean> correctness, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        for (String answerCorrectness : processedLine) {
            boolean isCorrect = Boolean.parseBoolean(answerCorrectness.trim());
            correctness.add(isCorrect);
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
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            writeToFile(results, topics, answers, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_WRITING);
        }
    }

    public void pauseGame(ResultsList results, TopicList topics, AnswerTracker answers, ArrayList<String> allAnswers,
                          ArrayList<Boolean> answersCorrectness, Results topicResults, int topicNum, int index)
            throws CustomException {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            savePoint(allAnswers, answersCorrectness, topicResults, fileWriter, topicNum, index);
            writeToFile(results, topics, answers, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_WRITING);
        }
    }

    private static void savePoint(ArrayList<String> allAnswers, ArrayList<Boolean> answersCorrectness,
                                  Results topicResults, FileWriter fileWriter, int topicNum, int index)
            throws IOException {

        fileWriter.write("pause " + topicNum + " | " + index + System.lineSeparator());

        StringBuilder listOfAnswers = new StringBuilder();
        StringBuilder listOfCorrectness = new StringBuilder();

        if (!allAnswers.isEmpty()) {
            listOfAnswers.append(allAnswers.get(FIRST_ANSWER));
            listOfCorrectness.append(answersCorrectness.get(FIRST_ANSWER));
        }

        for (int i = 1; i < allAnswers.size(); i++) {
            String answer = allAnswers.get(i);
            boolean correctness = answersCorrectness.get(i);
            listOfAnswers.append(" | ").append(answer);
            listOfCorrectness.append(" | ").append(correctness);
        }

        int numberOfCorrectAnswers = topicResults.getNumberOfCorrectAnswers();
        int totalNumberOfQuestions = topicResults.getTotalNumberOfQuestions();

        fileWriter.write("temp_result " + numberOfCorrectAnswers + " | " + totalNumberOfQuestions
                + System.lineSeparator());
        fileWriter.write("temp_answer " + listOfAnswers + System.lineSeparator());
        fileWriter.write("temp_correctness " + listOfCorrectness + System.lineSeparator());
    }

    private static void writeToFile(ResultsList results, TopicList topics, AnswerTracker answers,
                                    FileWriter fileWriter)
            throws IOException {

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
        for (Topic topic : topicList) {
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
    }


    // read from questionList.txt, generate questionsList
    public void generateQuestionsList(String textFile, QuestionsList questionsList1) {
        String absolutePath = getAbsoluteFilePath(textFile);
        File f = new File(absolutePath);
        // testing
        System.out.println("absoluteFilePath is: " + absolutePath);
        System.out.println("File object full path: " + f.getAbsolutePath());
        System.out.println("file exists?: " + f.exists());

        try {
            processQuestionsFile(f, questionsList1);
        } catch (CustomException e) {
            System.out.println(e.getMessage());
        }
    }

    private void processQuestionsFile(File f, QuestionsList currentQuestionsList) throws CustomException {
        Scanner s;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            throw new CustomException(MESSAGE_ERROR_INIT);
        }

        while (s.hasNext()) {
            String currentLineInFile = s.nextLine();
            String[] lineInParts = currentLineInFile.split(ARG_SEPARATOR); // rmb do trim()
            String[] parameters = new String[7];
            for (int i = 0; i < TOTAL_QUESTIONS_PARAMETERS; i += 1) {
                parameters[i] = lineInParts[i].trim();
            }
            // creating parameters for new Question()
            String question = parameters[QUESTION_PARAMETER];
            String solution = parameters[SOLUTION_PARAMETER];
            String explanation = parameters[EXPLANATION_PARAMETER];
            String[] options = new String[TOTAL_OPTIONS];
            for (int j = 0; j < TOTAL_OPTIONS; j++) {
                options[j] = parameters[OPTIONS_START_PARAMETER + j];
            }
            Question currentQuestion = new Question(question, solution, explanation, options);
            currentQuestionsList.addQuestion(currentQuestion);
        }
    }

    /**
     * Gets absolute file path to localFilePath
     *
     * @param localFilePath to store tasks in
     * @return absolute file path
     */
    public static String getAbsoluteFilePath(String localFilePath) {
        String currentDirectory = System.getProperty("user.dir");
        java.nio.file.Path directoryPath = java.nio.file.Paths.get(currentDirectory);
        return directoryPath + "\\" + localFilePath;
    }

}
