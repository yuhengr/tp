package seedu.duke;


import com.bethecoder.ascii_table.ASCIITable;
import seedu.duke.exceptions.CustomException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Ui {
    private static final Scanner in = new Scanner(System.in);
    private static final String HEADER_ALL_RESULTS = "These are all your results so far:\n";
    private static final String MESSAGE_INPUT = "Input a command player!";
    private static final String MESSAGE_NUMBER_USER_NAME = "Number ";
    private static final String MESSAGE_ANSWER = "Enter your answer: ";
    private static final String MESSAGE_ASK_RESUME = "The game is paused.\nInput \"resume\" to continue, " +
            "or \"bye\" to exit.";
    private static final String MESSAGE_RESUME = "The game has been resumed.";

    private static final String MESSAGE_CANNOT_PAUSE = "You cannot pause in timed mode!";
    private static final String MESSAGE_TOPIC_FINISHED = "You have finished the topic! What will be your " +
            "next topic?";
    private static final String MESSAGE_ALL_TOPICS = "Here are the topics in CS2113:";
    private static final String MESSAGE_TOPIC_DONE = " (DONE)";
    private static final String MESSAGE_ONE_SOLUTION = "The solution for question ";
    private static final String MESSAGE_ONE_EXPLANATION = "The explanation for question ";
    private static final String MESSAGE_ALL_SOLUTIONS = "The solutions are :";
    private static final String MESSAGE_ALL_EXPLANATIONS = "The explanations are :";
    private static final String MESSAGE_ASK_FOR_NAME = "What is your name?";
    private static final String MESSAGE_ASK_FOR_NAME_AGAIN = "Good try, but I still don't know who I'm talking to";
    private static final String MESSAGE_SAY_BYE = "Most often, the problem is not the lack of time but the lack of " +
            "direction";
    private static final String ANSWER_TIMEOUT = "You ran out of time!";
    private static final String MESSAGE_ANSWER_FORMAT = "Your answer must be either a, b, c, or d!";
    private static final String RESUME = "resume";
    private static final String INVALID_INPUT = "Invalid input. Please type 'yes' or 'no'";
    private static final String INSTRUCTIONS = "Type 'yes' to restart session or 'no' to resume.";

    private static final int INDEX_TOPIC_NUM = 0;
    private static final int INDEX_INDEX = 1;

    private static final int NEW_LINE_LENGTH = 48;
    private static final boolean IS_TIMED_MODE = true;
    public boolean isPlaying = true;

    public boolean hasStartedGame = false;
    public TopicList topicList;
    public QuestionListByTopic questionListByTopic;

    public String[] inputAnswers;

    private boolean isTimesUp;
    private boolean isActivated;
    private boolean hasCompletedSet;

    private int indexGlobal;
    private ProgressBar questionProgressBar = new ProgressBar(0);

    public void displayProgressBar(int current, int total) {
        questionProgressBar = new ProgressBar(total, current);
        questionProgressBar.display();
        System.out.print(" " + current + "/" + total + " questions attempted");
        System.out.println();
    }

    //@@author ngxzs
    public void readCommands(
            Ui ui, TopicList topicList,
            QuestionListByTopic questionListByTopic, ResultsList allResults, Helper helper, AnswerTracker userAnswers,
            Storage storage, ProgressManager progressManager
    ) {
        Parser parser = new Parser();

        while (isPlaying) {
            ui.askForInput();
            String command = in.nextLine();
            try {
                parser.parseCommand(command, ui, topicList, questionListByTopic, allResults, helper,
                        userAnswers, storage, progressManager);
            } catch (CustomException e) {
                ui.handleException(e);
            }
        }

        sayBye();
    }

    //@@author ngxzs
    private void askForInput() {
        System.out.println(MESSAGE_INPUT);
    }

    //@@author
    public void askForAnswerInput() {
        System.out.print(MESSAGE_ANSWER);
    }

    //@@author hongyijie06
    public void printInvalidForResume(){
        System.out.println(INVALID_INPUT);
    }

    public void printInstructions(){
        System.out.println(INSTRUCTIONS);
    }

    //@@author
    public void printTopicList(TopicList topicList, Ui ui) {
        int topicListSize = topicList.getSize();
        System.out.println(MESSAGE_ALL_TOPICS);
        for (int index = 0; index < topicListSize; index++) {
            System.out.print((index + 1) + ". " + topicList.getTopic(index));
            if (topicList.get(index).hasAttempted()) {
                System.out.print(MESSAGE_TOPIC_DONE);
            }
            System.out.println();
        }
        System.out.println((topicListSize + 1) + ". " + "Randomly select a topic for me ;)");
        printLine();
        System.out.println("Please choose a topic to play:");//input command in the form "start [INDEX]
        printLine();
    }

    public void printChosenTopic(
            int topicNum, TopicList topicList, QuestionListByTopic questionListByTopic, ResultsList allResults,
            AnswerTracker userAnswers, boolean isTimedMode, Storage storage, Ui ui, int timeLimit
    ) throws CustomException {
        Results topicResults = new Results();
        QuestionsList qnList;
        hasCompletedSet = false;

        printSelectedTopic(topicList, topicNum);
        if (isTimedMode) {
            printTimeLimit(timeLimit);
        }
        int topicNumIndex = topicNum - 1; //-1 due to zero index
        qnList = questionListByTopic.getQuestionSet(topicNumIndex);
        allResults.addQuestions(topicNumIndex);

        int numOfQns = qnList.getSize();
        Question questionUnit;
        String[] inputAnswers = new String[numOfQns];
        String answer;
        ArrayList<String> allAnswers = new ArrayList<>();
        ArrayList<Boolean> answersCorrectness = new ArrayList<>();

        for (indexGlobal = 0; indexGlobal < numOfQns; indexGlobal++) {//go through 1 question set
            printLine();
            displayProgressBar(indexGlobal, numOfQns);
            questionUnit = qnList.getQuestionUnit(indexGlobal);
            topicResults.increaseNumberOfQuestions();
            printQuestion(questionUnit);

            if (isTimedMode) {
                timerBegin(hasCompletedSet, allAnswers, numOfQns, answersCorrectness, timeLimit);
            }

            Parser parser = new Parser();
            boolean isPaused = false;
            boolean isCorrectFormat = false;
            boolean wasPaused;

            do {
                wasPaused = isPaused;
                askForAnswerInput();
                answer = in.nextLine();
                isPaused = parser.checkPause(answer, allResults, topicList, userAnswers, ui, storage, isPaused,
                        isTimedMode, allAnswers, answersCorrectness, topicResults, topicNum, indexGlobal);
                if (!isPaused && !answer.equalsIgnoreCase(RESUME) && !isTimesUp) {
                    isCorrectFormat = parser.checkFormat(answer, ui);
                }
            } while ((isPaused || wasPaused || !isCorrectFormat) && !isTimesUp);

            if (!isTimesUp) {
                parser.handleAnswerInputs(inputAnswers, indexGlobal, answer, questionUnit,
                        topicResults, answersCorrectness);
                finishBeforeTimerChecker(numOfQns, isTimedMode);

                allAnswers.add(answer);
            }
        }
        topicResults.calculateScore();
        allResults.addResults(topicResults);
        userAnswers.addUserAnswers(allAnswers);
        userAnswers.addUserCorrectness(answersCorrectness);
        isTimesUp = false;
    }

    //@@author hongyijie06

    /**
     * handles timer in the timed mode
     *
     * @param hasCompletedSet whether all the questions in the question set of the chosen topic has been answered
     * @param allAnswers the ArrayList of the user's answers for the question set
     * @param numOfQns total number of questions in the question set
     * @param answersCorrectness the Arraylist of whether the user's answers are correct for each question
     * @param timeLimit the time limit the user set in the timed mode in seconds
     */
    public void timerBegin(boolean hasCompletedSet, ArrayList<String> allAnswers, int numOfQns,
                           ArrayList<Boolean> answersCorrectness, int timeLimit) {

        if (indexGlobal == 0) {
            Timer timer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (Ui.class) {
                        timeOut(allAnswers, numOfQns, answersCorrectness);
                        timer.cancel();
                        isTimesUp = true;
                    }
                }
            };
            int timeLimitInMilliSec = timeLimit * 1000;
            timer.schedule(task, timeLimitInMilliSec);
        }
    }

    //@@author hongyijie06

    /**
     * handles the ui when time limit is reached in timed mode
     *
     * @param allAnswers the ArrayList of the user's answers
     * @param numOfQns number of questions in the question set of the chosen topic
     * @param answersCorrectness the ArrayList of whether the user's answers are correct for each question
     */
    public void timeOut(ArrayList<String> allAnswers, int numOfQns, ArrayList<Boolean> answersCorrectness) {
        if (!hasCompletedSet) {
            allAnswers.add(ANSWER_TIMEOUT);
            answersCorrectness.add(false);
            assert allAnswers.size() <= numOfQns :
                    "Number of questions answered needs to be <= available number or questions";
            assert answersCorrectness.size() <= allAnswers.size() :
                    "Number of questions correct needs to be <= number of answered questions";
            indexGlobal = numOfQns;
            printTimesUpMessage();
        }
    }

    //@@author hongyijie06

    /**
     * handles successful completion of the question set before the set time limit
     *
     * @param numOfQns the total number of questions in the question set
     * @param isTimedMode a boolean whether the game is in timed mode
     */
    public void finishBeforeTimerChecker(int numOfQns, boolean isTimedMode) {
        int qnNumberIndex = numOfQns - 1;//-1 due to zero index
        if (indexGlobal == qnNumberIndex && isTimedMode) {
            printCongratulatoryMessage();
            hasCompletedSet = true;
        }
    }

    public void printRemainingTime(int timeLeft) {
        System.out.println(timeLeft);
    }

    public void printTimeLimit(int timeLimit) {
        System.out.println("Time limit: " + timeLimit + " seconds");
    }

    public void printFinishedTopic() {
        System.out.println(MESSAGE_TOPIC_FINISHED);
        printLine();
    }

    public void printSelectedTopic(TopicList topicList, int topicNum) {
        System.out.println("Selected topic: " + topicList.getTopic(topicNum - 1));
        System.out.println("Here are the questions: ");
    }

    //@@author cyhjason29
    /**
     * Resumes the game from the question of which the user last left off when exiting a paused game.
     *
     * @param pausedQuestion An array of integers containing the previously paused topic number and question number.
     * @param topicList List of all topics.
     * @param questionListByTopic List of all questions by topic.
     * @param allResults List of all results.
     * @param userAnswers List of all user answers to questions.
     * @param storage Storage that deals with game data.
     * @param ui User interface.
     * @param answers User answers within the current attempt.
     * @param correctness User answer correctness within the current attempt.
     * @param topicResults User results within the current attempt.
     * @throws CustomException If there was an error pausing the game.
     */
    public void resumeTopic(int[] pausedQuestion, TopicList topicList, QuestionListByTopic questionListByTopic,
                            ResultsList allResults, AnswerTracker userAnswers, Storage storage, Ui ui,
                            ArrayList<String> answers, ArrayList<Boolean> correctness, Results topicResults)
            throws CustomException {
        QuestionsList qnList;
        int topicNum = pausedQuestion[INDEX_TOPIC_NUM];
        int qnNum = pausedQuestion[INDEX_INDEX];
        qnList = questionListByTopic.getQuestionSet(topicNum - 1);
        allResults.addQuestions(topicNum - 1);
        int numOfQns = qnList.getSize();
        Question questionUnit;
        String[] inputAnswers = new String[numOfQns];
        String answer;
        for (int index = qnNum; index < numOfQns; index++) { //go through 1 question set
            questionUnit = qnList.getQuestionUnit(index);
            System.out.println(questionUnit.getQuestion());

            Parser parser = new Parser();
            boolean isPaused = false;
            boolean wasPaused;
            boolean isCorrectFormat = false;

            do {
                wasPaused = isPaused;
                askForAnswerInput();
                answer = in.nextLine();
                isPaused = parser.checkPause(answer, allResults, topicList, userAnswers, ui, storage, isPaused,
                        !IS_TIMED_MODE, answers, correctness, topicResults, topicNum, indexGlobal);
                if (!isPaused && !answer.equalsIgnoreCase(RESUME)) {
                    isCorrectFormat = parser.checkFormat(answer, ui);
                }
            } while (isPaused || wasPaused || !isCorrectFormat);

            parser.handleAnswerInputs(inputAnswers, index, answer, questionUnit, topicResults, correctness);
            answers.add(answer);
        }
        topicResults.calculateScore();
        allResults.addResults(topicResults);
        userAnswers.addUserAnswers(answers);
        userAnswers.addUserCorrectness(correctness);
    }

    //@@author hongyijie06
    public void printCongratulatoryMessage() {
        System.out.println("Congrats! You beat the timer!");
    }

    public static void printTimesUpMessage() {
        System.out.println("Time is up!");
        System.out.println("Press enter to go back to topic selection. ");
    }

    public static void printTimedModeSelected() {
        System.out.println("Timed mode selected. Please enter the topic you would like to try. ");
        showCannotPause();
    }

    public void printNoSolutionAccess() {
        System.out.println("Attempt the topic first!");
    }

    public void printQuestion(Question questionUnit) {
        printLine();
        System.out.println(questionUnit.getQuestion());
    }

    //@@author ngxzs
    public void printOneSolution(int questionNum, String solution) {
        System.out.println(MESSAGE_ONE_SOLUTION + questionNum + ":"
                + System.lineSeparator() + solution);
    }

    public void printOneExplanation(int questionNum, String explanation) {
        System.out.println(MESSAGE_ONE_EXPLANATION + questionNum + ":"
                + System.lineSeparator() + explanation);
    }

    public void printAllSolutions(String allSolutions) {
        System.out.print(MESSAGE_ALL_SOLUTIONS
                + System.lineSeparator() + allSolutions);
    }

    public void printAllExplanations(String allExplanations) {
        System.out.print(MESSAGE_ALL_EXPLANATIONS
                + System.lineSeparator() + allExplanations);
    }

    //@@author cyhjason29

    /**
     * Prints one result to the user.
     *
     * @param includesDetails Whether the user has asked for details.
     * @param topicNum The number of the topic for the result.
     * @param score The score for the result.
     * @param questionListByTopic List of questions by topic.
     * @param userAnswers List of all user answers to questions.
     * @param index Attempt number requested by user.
     */
    public void printOneResult(boolean includesDetails, int topicNum, String score,
                               QuestionListByTopic questionListByTopic, AnswerTracker userAnswers, int index) {
        System.out.println("Attempt " + index + ": " + System.lineSeparator() + "Your results for Topic " +
                (topicNum + 1) + ":\n" + score + System.lineSeparator());
        if (includesDetails) {
            printResultDetails(questionListByTopic, topicNum, index - 1, userAnswers);
        }
    }

    /**
     * Prints all results to the user.
     *
     * @param includesDetails Whether the user has asked for details.
     * @param allResults List of all results.
     * @param questionListByTopic List of questions by topic.
     * @param userAnswers List of all user answers to questions.
     */
    public void printAllResults(boolean includesDetails, ResultsList allResults,
                                QuestionListByTopic questionListByTopic, AnswerTracker userAnswers) {
        int numberOfResults = allResults.getSizeOfAllResults();
        System.out.println(HEADER_ALL_RESULTS);
        for (int i = 0; i < numberOfResults; i++) {
            int topicNum = allResults.getTopicNum(i);
            System.out.println("Attempt " + (i + 1) + ": " + System.lineSeparator() + "Your results for Topic " +
                    (topicNum + 1) + ":\n" + allResults.getSpecifiedResult(i).getScore()
                    + System.lineSeparator());
            if (includesDetails) {
                printResultDetails(questionListByTopic, topicNum, i, userAnswers);
            }
        }
    }

    /**
     * Prints the questions and the user answers to them within a topic.
     *
     * @param questionListByTopic List of questions by topic/.
     * @param topicNum Topic number
     * @param index Attempt number
     * @param userAnswers List of all user answers to questions.
     */
    private void printResultDetails(QuestionListByTopic questionListByTopic, int topicNum, int index,
                                    AnswerTracker userAnswers) {
        QuestionsList listOfQuestions = questionListByTopic.getQuestionSet(topicNum);
        for (int i = 0; i < userAnswers.getAllAnswers().get(index).size(); i++) {
            Question questionUnit = listOfQuestions.getQuestionUnit(i);
            boolean isCorrectAnswer = userAnswers.getIsCorrect(i, index);
            String answer = userAnswers.getUserAnswers(i, index);
            System.out.println(questionUnit.getQuestion() + "\nYou answered:\n" + answer
                    + ((answer.equals(ANSWER_TIMEOUT)) ? System.lineSeparator()
                    : "\nYou got it " + ((isCorrectAnswer) ? "right!\n" : "wrong!\n")));
        }
    }

    //@@author ngxzs
    public void handleException(CustomException e) {
        System.out.println(e.getMessage());
    }

    public void printLine() {
        for (int i = 0; i < NEW_LINE_LENGTH; i += 1) {
            System.out.print("*");
        }
        System.out.println();
    }

    public void sayHi() {
        String logo =
                "______ _                       _____  __   __   _____\n" +
                        "| ___ \\ |                     / __  \\/  | /  | |____ |\n" +
                        "| |_/ / | __ _ _   _  ___ _ __`' / /'`| | `| |     / /\n" +
                        "|  __/| |/ _` | | | |/ _ \\ '__| / /   | |  | |     \\ \\\n" +
                        "| |   | | (_| | |_| |  __/ |  ./ /____| |__| |_.___/ /\n" +
                        "\\_|   |_|\\__,_|\\__, |\\___|_|  \\_____/\\___/\\___/\\____/\n" +
                        "                __/ |\n" +
                        "               |___/";

        System.out.println("Hello from\n" + logo);

        String userName = "";
        while (true) {
            System.out.println(MESSAGE_ASK_FOR_NAME);
            userName = in.nextLine();
            if (!userName.isBlank()) {
                break; // if userName != "", " " etc
            }
            System.out.println(MESSAGE_ASK_FOR_NAME_AGAIN);
        }
        String trimmedUserName = userName.trim();
        String userNameToPrint;
        if (isInteger(userName.trim())) {
            userNameToPrint = MESSAGE_NUMBER_USER_NAME + trimmedUserName;
        } else {
            userNameToPrint = trimmedUserName;
        }
        System.out.println("Hello " + userNameToPrint);
        printLine();
    }

    // checks userName, returns true if is an Integer, else false
    private static boolean isInteger(String userName) {
        try {
            Integer.parseInt(userName);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void sayBye() {
        System.out.println(MESSAGE_SAY_BYE);
        printLine();
    }

    //@@author songyuew
    public void printTable(String[] headers, String[][] data) {
        System.out.println(ASCIITable.getInstance().getTable(headers, data));
    }

    //@@author cyhjason29

    /**
     * Asks the user to resume or exit the game.
     */
    public void askForResume() {
        System.out.println(MESSAGE_ASK_RESUME);
    }

    /**
     * Prints to the user that the game has been resumed.
     */
    public void showResume() {
        System.out.println(MESSAGE_RESUME);
    }

    /**
     * Prints to the user that the game cannot be paused in timed mode.
     */
    public static void showCannotPause() {
        System.out.println(MESSAGE_CANNOT_PAUSE);
    }

    /**
     * Prints to the user that the answer is not in the correct format.
     */
    public void showCorrectFormat() {
        System.out.println(MESSAGE_ANSWER_FORMAT);
    }


    //@@author hongyijie06

    /**
     * Ask if user wants to continue paused session
     */
    public void askResumeSessionPrompt() {
        System.out.println("Continue from previous paused session? (yes/no)");
    }

    /**
     * Confirmation for not resuming and disclaimer of discarding results
     */
    public void confirmSelection() {
        System.out.println("Are you sure you don't want to continue the session? (yes/no) ");
        System.out.println("Results from the incomplete attempt will be discarded :0");
    }

    public void printCustomModeMessage() {
        System.out.println("You've selected to practise in custom mode.");
    }

    public int getCustomTopicNum() {
        System.out.println("Which topic do you want to practise?");
        String userInput = in.nextLine();

        // Parse the input to get an integer
        try {
            int topicNum = Integer.parseInt(userInput);
            return topicNum;
        } catch (NumberFormatException error) {
            return -1;
        }
    }

    public int getCustomNumOfQuestions() {
        System.out.println("How many questions would you like to practise?");
        String userInput = in.nextLine();

        try {
            int numOfQuestions = Integer.parseInt(userInput);
            return numOfQuestions;
        } catch (NumberFormatException error) {
            return -1;
        }
    }

    public String getUserAnswerInput() {
        String userInput = in.nextLine();
        return userInput;
    }

    public void displayUserAnswer(String userAnswer) {
        System.out.println("Your answer: " + userAnswer);
    }

    public int getCheckpointGoal() {
        System.out.println("How many custom questions would you like to complete?");
        String userInput = in.nextLine();

        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException error) {
            return -1;
        }
    }
}

