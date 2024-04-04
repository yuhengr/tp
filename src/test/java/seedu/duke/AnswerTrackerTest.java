package seedu.duke;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import seedu.duke.exceptions.CustomException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AnswerTrackerTest {
    Ui ui;
    ResultsList allResults;
    Parser parser;
    AnswerTracker userAnswersList;
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
    private ArrayList<String> userAnswers;
    private ArrayList<String> userAnswers2;
    private ArrayList<Boolean> correctness1;
    private ArrayList<Boolean> correctness2;


    void setUp() {
        createFourQuestions();
        createTwoUserAnswers();

        correctness1 = new ArrayList<>();
        correctness2 = new ArrayList<>();
        ui = new Ui();
        allResults = new ResultsList();
        parser = new Parser();
        userAnswersList = new AnswerTracker();
        createUserAnswerList();
        checkCorrectnessForAllAttempts(userAnswersList, correctness1, correctness2);

    }

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

    void createTwoUserAnswers() {
        userAnswers = new ArrayList<>();
        String answer1 = "solution1";
        String answer2 = "solution2";
        userAnswers.add(answer1);
        userAnswers.add(answer2);

        userAnswers2 = new ArrayList<>();
        String answer3 = "wrong answer";
        String answer4 = "solution4";
        userAnswers2.add(answer3);
        userAnswers2.add(answer4);
    }

    void createUserAnswerList(){
        userAnswersList = new AnswerTracker();
        userAnswersList.addUserAnswers(userAnswers);
        userAnswersList.addUserAnswers(userAnswers2);
    }

    void checkCorrectnessForAllAttempts(AnswerTracker userAnswersList, ArrayList<Boolean> correctness1,
                                        ArrayList<Boolean> correctness2){

        checkCorrectnessForOneAttempt(userAnswers, 0, correctness1);
        userAnswersList.addUserCorrectness(correctness1);

        checkCorrectnessForOneAttempt(userAnswers2, 1, correctness2);
        userAnswersList.addUserCorrectness(correctness2);
    }

    void checkCorrectnessForOneAttempt(ArrayList<String> userAnswers, int attemptNumber,
                                       ArrayList<Boolean> correctness){
        if (attemptNumber == 0) {
            if (userAnswers.get(0).contentEquals(question1.getSolution())) {
                correctness.add(true);
            } else {
                correctness.add(false);
            }
        } else if (attemptNumber == 1){
            if (userAnswers.get(1).contentEquals(question3.getSolution())) {
                correctness.add(true);
            } else {
                correctness.add(false);
            }
        }
    }

    @Test
    void testGetUserAnswers() throws CustomException{
        setUp();
        assertEquals("wrong answer", userAnswersList.getUserAnswers(0,1));
    }

    @Test
    void testGetIsCorrect() throws CustomException {
        setUp();
        assertEquals(true, userAnswersList.getIsCorrect(0,0));
    }
}
