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
    private static final String MESSAGE_ASK_RESUME = "The game is paused.\nInput \"resume\" to continue, " +
            "or \"bye\" to exit.";
    private static final String MESSAGE_RESUME = "The game has been resumed.";
    private static final String MESSAGE_CANNOT_PAUSE = "You cannot pause in timed mode!";
    private static final String MESSAGE_TOPIC_COMPLETED = "You have finished the topic! What will be your " +
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

    public void readCommands(
            Ui ui, TopicList topicList,
            QuestionListByTopic questionListByTopic, ResultsList allResults, Helper helper, AnswerTracker userAnswers,
            Storage storage
    ) {
        Parser parser = new Parser();
        printLine();

        while(isPlaying) {
            ui.askForInput();
            String command = in.nextLine();
            try {
                parser.parseCommand(command, ui, topicList, questionListByTopic, allResults, helper,
                        userAnswers, storage);
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
            AnswerTracker userAnswers, boolean isTimedMode, Storage storage, Ui ui
    ) throws CustomException {
        Results topicResults = new Results();
        QuestionsList qnList;
        boolean[] isTimesUp = {false};
        boolean[] hasCompletedSet = {false};

        System.out.println("Selected topic: " + topicList.getTopic(topicNum - 1));
        System.out.println("Here are the questions: ");
        qnList = questionListByTopic.getQuestionSet(topicNum - 1);
        alLResults.addQuestions(topicNum - 1);
        int numOfQns = qnList.getSize();
        Question questionUnit;
        String[] inputAnswers = new String[numOfQns];
        String answer;
        ArrayList<String> allAnswers = new ArrayList<>();
        ArrayList<Boolean> answersCorrectness = new ArrayList<>();
        for (final int[] index = {0}; index[0] < numOfQns; index[0]++){//go through 1 question set
            questionUnit = qnList.getQuestionUnit(index[0]);
            topicResults.increaseNumberOfQuestions();
            System.out.println(questionUnit.getQuestion());

            if (isTimedMode) {
                if (index[0] == 0) {
                    Timer timer = new Timer();

                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            if (!hasCompletedSet[0]) {
                                printTimesUpMessage();
                                isTimesUp[0] = true;
                                index[0] = numOfQns;
                                timer.cancel();
                            } else {
                                isTimesUp[0] = true;
                                timer.cancel();
                            }
                        }
                    };
                    timer.schedule(task, 5000);
                    if (isTimesUp[0]) {
                        break;
                    }
                }
            }
            Parser parser = new Parser();
            boolean isPaused = false;
            boolean wasPaused;

            do {
                wasPaused = isPaused;
                askForAnswerInput();
                answer = in.nextLine();
                isPaused = parser.checkPause(answer, allResults, topicList, userAnswers, ui, storage, isPaused,
                        isTimedMode, allAnswers, answersCorrectness, topicResults, topicNum, index[0]);
            } while (isPaused || wasPaused);

            if (!isTimesUp[0]) {
                parser.handleAnswerInputs(inputAnswers, index[0], answer, questionUnit, topicResults,
                        answersCorrectness);
                if (index[0] == numOfQns - 1 && isTimedMode){
                    printCongratulatoryMessage();
                    hasCompletedSet[0] = true;
                }
                allAnswers.add(answer);
            }
        }
        topicResults.calculateScore();
        alLResults.addResults(topicResults);
        userAnswers.addUserAnswers(allAnswers);
        userAnswers.addUserCorrectness(answersCorrectness);
    }

    public void printCongratulatoryMessage(){
        System.out.println("Congrats! You beat the timer!");
    }

    public void printTimesUpMessage(){
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

    public void printAllSolutions(String allSolutions) {
        System.out.println("The solutions are :"
                + System.lineSeparator() + allSolutions);
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

    public void printTopicCompleted() {
        System.out.println(MESSAGE_TOPIC_COMPLETED);
    }
}
