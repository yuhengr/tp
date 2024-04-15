//@@author ngxzs
package seedu.duke;

import org.junit.jupiter.api.Test;
import seedu.duke.exceptions.CustomException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {
    Ui ui;
    ResultsList allResults;
    Helper helper;
    Parser parser;
    AnswerTracker userAnswers;

    ProgressManager progressManager;
    private Question question1;
    private Question question2;
    private Question question3;
    private Question question4;
    private QuestionsList questionsList1;
    private QuestionsList questionsList2;
    private QuestionListByTopic questionListByTopic;
    private Topic topicOne;
    private Topic topicTwo;
    private TopicList topicList;
    private Results roundOneResults;
    private Results roundTwoResults;
    private Storage storage;

    void setUp() {
        createFourQuestions();
        createTwoQuestionLists();
        createTwoTopics();
        createOneTopicList();
        createQuestionListByTopic();
        createResultsForTwoRounds();
        createResultsList();
        ui = new Ui();
        allResults = new ResultsList();
        helper = new Helper();
        parser = new Parser();
        userAnswers = new AnswerTracker();
        storage = new Storage();
        progressManager = new ProgressManager(allResults);
    }
    void createResultsList() {
        allResults = new ResultsList();
        allResults.addResults(roundOneResults);

    }
    void createResultsForTwoRounds() {
        // for roundOne
        roundOneResults = new Results();
        int roundOneTotalNumOfQuestions = 5;
        for (int i = 0; i < roundOneTotalNumOfQuestions; i += 1) {
            roundOneResults.increaseNumberOfQuestions();
        }
        int roundOneCorrectNumOfQuestions = 3;
        for (int i = 0; i < roundOneCorrectNumOfQuestions; i += 1) {
            roundOneResults.increaseCorrectAnswers(); // 1 correct answer
        } // roundOne: 3 correct out of 5 questions (60%)
        roundOneResults.calculateScore(); // updates roundOneResults.score = "3/5 (60%)"

        // for roundTwo
        roundTwoResults = new Results();
        int roundTwoTotalNumOfQuestions = 10;
        for (int i = 0; i < roundTwoTotalNumOfQuestions; i += 1) {
            roundTwoResults.increaseNumberOfQuestions();
            roundTwoResults.increaseCorrectAnswers();
        } // roundTwo: 10 correct out of 10 questions (100%)
        roundTwoResults.calculateScore(); // updates roundTwoResults.score = "10/10 (100%)"
    }
    void createQuestionListByTopic() {
        questionListByTopic = new QuestionListByTopic();
        questionListByTopic.addQuestionSet(questionsList1);
        questionListByTopic.addQuestionSet(questionsList2);
    }
    void createOneTopicList() {
        topicList = new TopicList();
        topicList.addTopic(topicOne);
        topicList.addTopic(topicTwo);
    }
    void createTwoTopics() {
        boolean hasNotAttempted = false;
        topicOne = new Topic(questionsList1, "topic1", hasNotAttempted, "topicSummary1");
        topicTwo = new Topic(questionsList2, "topic2", hasNotAttempted, "topicSummary2");
    }
    void createTwoQuestionLists() {
        questionsList1 = new QuestionsList();
        questionsList1.addQuestion(question1);
        questionsList1.addQuestion(question2);

        questionsList2 = new QuestionsList();
        questionsList2.addQuestion(question3);
        questionsList2.addQuestion(question4);
    }

    //@@author cyhjason29
    void createFourQuestions() {
        question1 = new Question("question1", "solution1", "explanation1",
                new String[]{"optionA","optionB","optionC","optionD"});
        question2 = new Question("question2", "solution2", "explanation2",
                new String[]{"optionA","optionB","optionC","optionD"});
        question3 = new Question("question3", "solution3", "explanation3",
                new String[]{"optionA","optionB","optionC","optionD"});
        question4 = new Question("question4", "solution4", "explanation4",
                new String[]{"optionA","optionB","optionC","optionD"});
    }

    void runParserCommand(String command) throws CustomException {
        parser.parseCommand(command, ui, topicList,
                questionListByTopic, allResults, helper, userAnswers, storage, progressManager);
    }

    @Test
    void parseCommand_solutionCommand_noException() throws CustomException {
        setUp();
        final String validCommand = "solution 1 2";

        assertDoesNotThrow(() -> runParserCommand(validCommand));
    }

    @Test
    void parseCommand_outOfBoundsParameter_customError() {
        setUp();
        final String command = "solution 99 2";

        assertThrows(CustomException.class,
                () -> runParserCommand(command));
    }

    @Test
    void parseCommand_wrongNumOfParameters_customError() {
        setUp();
        final String command = "solution a D 3 99 no";

        assertThrows(CustomException.class,
                () -> runParserCommand(command));
    }
    @Test
    void parseCommand_invalidCommand_customError() {
        setUp();
        final String command = "solution2299sdf 2";

        assertThrows(CustomException.class,
                () -> runParserCommand(command));
    }

    @Test
    void parseCommand_overflowingParam_customError() {
        setUp();
        final String command = "topic 1000000000000";

        assertThrows(CustomException.class,
                () -> runParserCommand(command));
    }

    @Test
    void parseCommand_invalidTopicNumParamForCustom_customError() {
        setUp();
        final String command = "custom 10 1";

        assertThrows(CustomException.class,
                () -> runParserCommand(command));
    }
}
