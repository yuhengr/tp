package seedu.duke;


import com.bethecoder.ascii_table.ASCIITable;
import seedu.duke.exceptions.CustomException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Ui {

    private ProgressBar questionProgressBar = new ProgressBar(0);
    private static final Scanner in = new Scanner(System.in);

    private static final String HEADER_ALL_RESULTS = "These are all your results so far:\n";
    private static final String MESSAGE_ASK_RESUME = "The game is paused.\nInput \"resume\" to continue, " +
            "or \"bye\" to exit.";
    private static final String MESSAGE_RESUME = "The game has been resumed.";
    private static final String MESSAGE_CANNOT_PAUSE = "You cannot pause in timed mode!";
    private static final String MESSAGE_TOPIC_FINISHED = "You have finished the topic! What will be your " +
            "next topic?";

    private static final int INDEX_TOPIC_NUM = 0;
    private static final int INDEX_INDEX = 1;

    private static final int NEW_LINE = 48;
    private static final boolean IS_TIMED_MODE = true;
    public boolean isPlaying = true;

    public boolean hasStartedGame = false;
    public TopicList topicList;
    public QuestionListByTopic questionListByTopic;

    public String[] inputAnswers;

    private  boolean isTimesUp;
    private boolean isActivated;
    private boolean hasCompletedSet;

    private int indexGlobal;

    public void displayProgressBar(int current, int total) {
        questionProgressBar = new ProgressBar(total, current);
        questionProgressBar.display();
        System.out.print(" " + current + "/" + total + " questions attempted");
        System.out.println();
    }

    public void readCommands(
            Ui ui, TopicList topicList,
            QuestionListByTopic questionListByTopic, ResultsList allResults, Helper helper, AnswerTracker userAnswers,
            Storage storage, ProgressManager progressManager
    ) {
        Parser parser = new Parser();
        printLine();

        while(isPlaying) {
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

    private void askForInput() {
        System.out.println("Input a command player!"); // TODO: show possible commands
    }

    public void askForAnswerInput(){
        System.out.print("Enter your answer: ");
    }

    public void printTopicList(TopicList topicList, Ui ui){
        int topicListSize = topicList.getSize();
        System.out.println("Here are the topics in CS2113:");
        for (int index = 0; index < topicListSize; index++) {
            System.out.println((index + 1) + ". " + topicList.getTopic(index));
        }
        System.out.println((topicListSize + 1) + ". " + "Randomly select a topic for me ;)");
        System.out.println("Please choose a topic to play:");//input command in the form "start [INDEX]
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

        for (indexGlobal = 0; indexGlobal < numOfQns; indexGlobal++){//go through 1 question set
            displayProgressBar(indexGlobal, numOfQns);
            questionUnit = qnList.getQuestionUnit(indexGlobal);
            topicResults.increaseNumberOfQuestions();
            printQuestion(questionUnit);

            if (isTimedMode) {
                timerBegin(hasCompletedSet, allAnswers, numOfQns, answersCorrectness, timeLimit);
            }

            Parser parser = new Parser();
            boolean isPaused = false;
            boolean wasPaused;

            do {
                wasPaused = isPaused;
                askForAnswerInput();
                answer = in.nextLine();
                isPaused = parser.checkPause(answer, allResults, topicList, userAnswers, ui, storage, isPaused,
                        isTimedMode, allAnswers, answersCorrectness, topicResults, topicNum, indexGlobal);
            } while (isPaused || wasPaused);

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

    public void timerBegin(boolean hasCompletedSet, ArrayList<String> allAnswers, int numOfQns,
                           ArrayList<Boolean> answersCorrectness, int timeLimit){
        
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

    public void timeOut(ArrayList<String> allAnswers, int numOfQns, ArrayList<Boolean> answersCorrectness){
        if (!hasCompletedSet) {
            assert allAnswers.size() <= numOfQns :
                    "Number of questions answered needs to be <= available number or questions";
            assert answersCorrectness.size() <= allAnswers.size() :
                    "Number of questions correct needs to be <= number of answered questions";
            indexGlobal = numOfQns;
            printTimesUpMessage();
        }
    }

    public void finishBeforeTimerChecker(int numOfQns, boolean isTimedMode){
        int qnNumberIndex = numOfQns - 1;//-1 due to zero index
        if (indexGlobal == qnNumberIndex && isTimedMode){
            printCongratulatoryMessage();
            hasCompletedSet = true;
        }
    }

    public void printRemainingTime(int timeLeft){
        System.out.println(timeLeft);
    }

    public void printTimeLimit(int timeLimit){
        System.out.println("timeLimit: " + timeLimit + "seconds");
    }

    public void printFinishedTopic(){
        System.out.println(MESSAGE_TOPIC_FINISHED);
    }
  
    public void printSelectedTopic(TopicList topicList, int topicNum){
        System.out.println("Selected topic: " + topicList.getTopic(topicNum - 1));
        System.out.println("Here are the questions: ");
    }

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
        for (int index = qnNum; index < numOfQns; index++){ //go through 1 question set
            questionUnit = qnList.getQuestionUnit(index);
            System.out.println(questionUnit.getQuestion());

            Parser parser = new Parser();
            boolean isPaused = false;
            boolean wasPaused;

            do {
                wasPaused = isPaused;
                askForAnswerInput();
                answer = in.nextLine();
                isPaused = parser.checkPause(answer, allResults, topicList, userAnswers, ui, storage, isPaused,
                        !IS_TIMED_MODE, answers, correctness, topicResults, topicNum, index);
            } while (isPaused || wasPaused);

            parser.handleAnswerInputs(inputAnswers, index, answer, questionUnit, topicResults, correctness);
            answers.add(answer);
        }
        topicResults.calculateScore();
        allResults.addResults(topicResults);
        userAnswers.addUserAnswers(answers);
        userAnswers.addUserCorrectness(correctness);
    }

    public void printCongratulatoryMessage(){
        System.out.println("Congrats! You beat the timer!");
    }

    public static void printTimesUpMessage(){
        System.out.println("Time is up!");
        System.out.println("Press enter to go back to topic selection. ");
    }
    public static void printTimedModeSelected(){
        System.out.println("Timed mode selected. Please enter the topic you would like to try. ");
        System.out.println(MESSAGE_RESUME);
    }

    public void printNoSolutionAccess(){
        System.out.println("Attempt the topic first!");
    }

    public void printQuestion(Question questionUnit){
        System.out.println(questionUnit.getQuestion());
    }

    public void printOneSolution(int questionNum, String solution) {
        System.out.println("The solution for question " + questionNum + ":"
                + System.lineSeparator() + solution);
    }

    public void printOneExplanation(int questionNum, String explanation) {
        System.out.println("The explanation for question " + questionNum + ":"
                + System.lineSeparator() + explanation);
    }
    public void printAllSolutions(String allSolutions) {
        System.out.print("The solutions are :"
                + System.lineSeparator() + allSolutions);
    }

    public void printAllExplanations(String allExplanations) {
        System.out.print("The explanations are :"
                + System.lineSeparator() + allExplanations);
    }
    public void printOneResult(boolean includesDetails, int topicNum, String score,
                               QuestionListByTopic questionListByTopic, AnswerTracker userAnswers, int index) {
        System.out.println("Your results for Topic " + (topicNum + 1) + ":\n" + score + "\n");
        if (includesDetails) {
            printResultDetails(questionListByTopic, topicNum, index-1, userAnswers);
        }
    }

    public void printAllResults(boolean includesDetails, ResultsList allResults,
                                QuestionListByTopic questionListByTopic, AnswerTracker userAnswers) {
        int numberOfResults = allResults.getSizeOfAllResults();
        System.out.println(HEADER_ALL_RESULTS);
        for (int i = 0; i < numberOfResults; i++) {
            int topicNum = allResults.getTopicNum(i);
            System.out.println("Your results for Topic " + (topicNum + 1) + ":\n"
                    + allResults.getSpecifiedResult(i).getScore() + "\n");
            if (includesDetails) {
                printResultDetails(questionListByTopic, topicNum, i, userAnswers);
            }
        }
    }

    private void printResultDetails(QuestionListByTopic questionListByTopic, int topicNum, int index,
                                    AnswerTracker userAnswers) {
        QuestionsList listOfQuestions = questionListByTopic.getQuestionSet(topicNum);
        for (int i = 0; i < listOfQuestions.getSize(); i++) {
            Question questionUnit = listOfQuestions.getQuestionUnit(i);
            boolean isCorrectAnswer = userAnswers.getIsCorrect (i,index);
            System.out.println(questionUnit.getQuestion() + "\nYou answered:\n" + userAnswers.getUserAnswers(i, index)
                + "\nYou got it " + ((isCorrectAnswer) ? "right!\n" : "wrong!\n"));
        }
    }

    public void handleException(CustomException e) {
        System.out.println(e.getMessage()); //TODO
    }
    public void printLine() {
        for (int i = 0; i < NEW_LINE; i += 1) {
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
        System.out.println("What is your name?");
        System.out.println("Hello " + in.nextLine());
        printLine();
    }

    public void sayBye() {
        System.out.println("bye bye, get more sleep zzz");
        printLine();
    }

    public void printTable(String[] headers, String[][] data) {
        System.out.println(ASCIITable.getInstance().getTable(headers, data));
    }

    public void askForResume() {
        System.out.println(MESSAGE_ASK_RESUME);
    }

    public void showResume() {
        System.out.println(MESSAGE_RESUME);
    }

    public void showCannotPause() {
        System.out.println(MESSAGE_CANNOT_PAUSE);
    }

    public void askResumeSessionPrompt(){
        System.out.println("Continue from previous paused session? (yes/no)");
    }

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
            final int INVALID_TOPICNUM = -1;
            return INVALID_TOPICNUM;
        }
    }

    public int getCustomNumOfQuestions() {
        System.out.println("How many questions would you like to practise?");
        String userInput = in.nextLine();

        try {
            int numOfQuestions = Integer.parseInt(userInput);
            return numOfQuestions;
        } catch (NumberFormatException error) {
            final int INVALID_NUM_OF_QUESTIONS = -1;
            return INVALID_NUM_OF_QUESTIONS;
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
            int checkpointGoal = Integer.parseInt(userInput);
            return checkpointGoal;
        }
        catch (NumberFormatException error) {
            final int INVALID_CHECKPOINT_GOAL = -1;
            return INVALID_CHECKPOINT_GOAL;
        }
    }

}
