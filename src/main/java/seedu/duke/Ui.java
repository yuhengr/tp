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

    private static final int NEW_LINE = 48;
    public boolean isPlaying = true;

    public boolean hasStartedGame = false;
    public TopicList topicList;
    public QuestionListByTopic questionListByTopic;

    public String[] inputAnswers;

    private  boolean isTimesUp;
    private boolean hasCompletedSet;

    private int indexGlobal;

    public void readCommands(
            Ui ui, TopicList topicList,
            QuestionListByTopic questionListByTopic, ResultsList allResults, Helper helper, AnswerTracker userAnswers
    ) {
        Parser parser = new Parser();
        printLine();

        while(isPlaying) {
            ui.askForInput();
            String command = in.nextLine();
            try {
                parser.parseCommand(command, ui, topicList, questionListByTopic, allResults, helper,
                        userAnswers);
            } catch (CustomException e) {
                ui.handleException(e);
            }
        }

        sayBye();
    }

    private void askForInput() {
        System.out.println("Input a command player!"); // TODO: show possible commands
    }

    private void askForAnswerInput(){
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
            int topicNum, TopicList topicList, QuestionListByTopic questionListByTopic, ResultsList alLResults,
            AnswerTracker userAnswers, boolean isTimedMode
    ){
        Results topicResults = new Results();
        QuestionsList qnList;
        hasCompletedSet = false;

        printSelectedTopic(topicList, topicNum);
        int topicNumIndex = topicNum - 1; //-1 due to zero index
        qnList = questionListByTopic.getQuestionSet(topicNumIndex);
        alLResults.addQuestions(topicNumIndex);
        int numOfQns = qnList.getSize();
        Question questionUnit;
        String[] inputAnswers = new String[numOfQns];
        String answer;
        ArrayList<String> allAnswers = new ArrayList<>();
        ArrayList<Boolean> answersCorrectness = new ArrayList<>();

        for (indexGlobal = 0; indexGlobal < numOfQns; indexGlobal++){//go through 1 question set
            questionUnit = qnList.getQuestionUnit(indexGlobal);
            topicResults.increaseNumberOfQuestions();
            System.out.println(questionUnit.getQuestion());

            if (isTimedMode) {
                timerBegin(hasCompletedSet, allAnswers, numOfQns, answersCorrectness);
            }
            askForAnswerInput();
            Parser parser = new Parser();
            answer = in.nextLine();

            if (!isTimesUp) {
                parser.handleAnswerInputs(inputAnswers, indexGlobal, answer, questionUnit,
                        topicResults, answersCorrectness);
                finishBeforeTimerChecker(numOfQns, isTimedMode);
                allAnswers.add(answer);
            }
        }
        topicResults.calculateScore();
        alLResults.addResults(topicResults);
        userAnswers.addUserAnswers(allAnswers);
        userAnswers.addUserCorrectness(answersCorrectness);
        isTimesUp = false;
    }

    public void timerBegin(boolean hasCompletedSet, ArrayList<String> allAnswers, int numOfQns,
                           ArrayList<Boolean> answersCorrectness){
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
            timer.schedule(task, 5000);
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
        int QnNumberIndex = numOfQns - 1;//-1 due to zero index
        if (indexGlobal == QnNumberIndex && isTimedMode){
            printCongratulatoryMessage();
            hasCompletedSet = true;
        }
    }

    public void printFinishedTopic(){
        System.out.println("You have finished the topic! What will be your next topic?");
    }
    public void printSelectedTopic(TopicList topicList, int topicNum){
        System.out.println("Selected topic: " + topicList.getTopic(topicNum - 1));
        System.out.println("Here are the questions: ");
    }

    public void printCongratulatoryMessage(){
        System.out.println("Congrats! You beat the timer!");
    }

    public static void printTimesUpMessage(){
        System.out.println("Time is up!");
        System.out.println(" Press enter to go back to topic selection. ");
    }
    public void printTimedModeSelected(){
        System.out.println("Timed mode selected. Please enter the topic you would like to try. ");
    }

    public void printNoSolutionAccess(){
        System.out.println("Attempt the topic first!");
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

    private void handleException(CustomException e) {
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

}
