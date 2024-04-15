package seedu.duke;

import seedu.duke.exceptions.CustomException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Player2113 {
    private static final String FILE_PATH_STORAGE = "data/player2113.txt";
    private static final String MESSAGE_FILE_ERROR = "There was an error locating the save file.";
    private static final String YES = "yes";
    private static final String NO = "no";

    private Ui ui;
    private QuestionsList questionsList1;
    private QuestionsList questionsList2;
    private QuestionsList questionsList3;
    private TopicList topicList;
    private QuestionListByTopic questionListByTopic;
    private ResultsList allResults;
    private AnswerTracker userAnswers;
    private Storage storage;
    private ProgressManager progressManager;
    private final Helper helper;

    //@@author ngxzs
    public Player2113() {
        questionsList1 = new QuestionsList();
        questionsList2 = new QuestionsList();
        questionsList3 = new QuestionsList();
        questionListByTopic = new QuestionListByTopic();
        topicList = new TopicList();
        allResults = new ResultsList();
        userAnswers = new AnswerTracker();
        helper = new Helper();
        storage = new Storage();
        ui = new Ui();
        progressManager = new ProgressManager(allResults);

        ArrayList<QuestionsList> arrayOfQuestionsLists = new ArrayList<>();
        arrayOfQuestionsLists.add(questionsList1);
        arrayOfQuestionsLists.add(questionsList2);
        arrayOfQuestionsLists.add(questionsList3);

        try {
            for (int questionList = 0; questionList < arrayOfQuestionsLists.size(); questionList++) {
                QuestionsList currentQuestionList = arrayOfQuestionsLists.get(questionList);
                storage.updateQuestionList(questionList, currentQuestionList);
                questionListByTopic.addQuestionSet(currentQuestionList);
            }
        } catch (CustomException e) {
            ui.handleException(e);
        }

        Topic topic1 = new Topic(questionsList1, "Software Engineering Concepts I", false, "SE & OOP concepts");
        Topic topic2 = new Topic(questionsList2, "Software Engineering Concepts II", false, "SE & OOP concepts II");
        Topic topic3 = new Topic(questionsList3, "All About Java", false, "General info on Java");
        topicList.addTopic(topic1);
        topicList.addTopic(topic2);
        topicList.addTopic(topic3);
    }

    //@@author
    public void run() {
        ui.sayHi();
        File saveFile = new File(FILE_PATH_STORAGE);
        boolean isPaused = false;
        try {
            isPaused = storage.loadProgress(saveFile, allResults, topicList, userAnswers);
        } catch (FileNotFoundException e) {
            try {
                storage.initSaveFile(saveFile);
            } catch (CustomException exception) {
                ui.handleException(exception);
            }
        }
        if (isPaused) {
            try {
                ui.askResumeSessionPrompt();

                String decision;
                String userInput;
                do {
                    Scanner in = new Scanner(System.in);
                    Parser parser = new Parser();
                    userInput = in.nextLine();
                    decision = userInput.toLowerCase();
                    if (!(decision.equals(YES)) && !(decision.equals(NO))){
                        ui.printInvalidForResume();
                    }

                } while (!(decision.equals(YES)) && !(decision.equals(NO)));
                if (decision.equals(YES)) {
                    loadQuestion(saveFile);
                    topicList.displayProgressBar();
                    ui.printFinishedTopic();
                } else if (decision.equals(NO)) {
                    ui.confirmSelection();
                    String confirmDecision;
                    do {
                        Scanner input = new Scanner(System.in);
                        Parser parser = new Parser();
                        confirmDecision = input.nextLine();
                        if (!(confirmDecision.equals(YES)) && !(confirmDecision.equals(NO))){
                            ui.printInvalidForResume();
                            ui.printInstructions();
                        }

                    } while (!(confirmDecision.equals(YES)) && !(confirmDecision.equals(NO)));
                    if (confirmDecision.equals(NO)) {
                        ui.showResume();
                        loadQuestion(saveFile);
                        topicList.displayProgressBar();
                        ui.printFinishedTopic();
                    }
                }
            } catch (CustomException e) {
                ui.handleException(e);
            }
        }

        topicList.displayProgressBar();
        ui.printTopicList(topicList, ui);


        while (ui.isPlaying) {
            ui.readCommands(ui, topicList, questionListByTopic, allResults, helper,
                    userAnswers, storage, progressManager);
        }

    }

    //@@author cyhjason29

    /**
     * Loads the question of which the user last left off before exiting the game while paused.
     *
     * @param saveFile The save file containing game data.
     * @throws CustomException If there was an error finding the file.
     */
    private void loadQuestion(File saveFile) throws CustomException {
        Results topicResults = new Results();
        ArrayList<String> answers = new ArrayList<>();
        ArrayList<Boolean> correctness = new ArrayList<>();
        int[] pausedQuestion;
        try {
            pausedQuestion = storage.resumeGame(saveFile, topicResults, answers, correctness);
        } catch (FileNotFoundException e) {
            throw new CustomException(MESSAGE_FILE_ERROR);
        }

        ui.resumeTopic(pausedQuestion, topicList, questionListByTopic, allResults, userAnswers, storage, ui,
                answers, correctness, topicResults);
    }

    //@@author ngxzs
    public static void main(String[] args) {
        new Player2113().run();
    }
}
