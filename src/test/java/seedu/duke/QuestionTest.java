package seedu.duke;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestionTest {

    private static final String OPTION_A = "a. ";
    private static final String OPTION_B = "b. ";
    private static final String OPTION_C = "c. ";
    private static final String OPTION_D = "d. ";

    Question oneQuestion;
    final String question1 = "question1";
    final String solution1 = "solution1";
    final String explanation1 = "explanation1";
    final String[] options1 = new String[]{"optionA","optionB","optionC","optionD"};

    //@@author ngxzs
    void createQuestion() {
        oneQuestion = new Question(question1, solution1, explanation1,options1);
    }
    @Test
    void getExplanation_oneQuestion_expectExplanation() {
        createQuestion();

        assertEquals(explanation1, oneQuestion.getExplanation());
    }
    //@@author cyhjason29
    @Test
    void getQuestion_oneQuestion_expectQuestion() {
        createQuestion();

        assertEquals(question1 + System.lineSeparator() + OPTION_A + "optionA" + System.lineSeparator()
                + OPTION_B + "optionB" + System.lineSeparator() + OPTION_C + "optionC" + System.lineSeparator()
                + OPTION_D + "optionD", oneQuestion.getQuestion());
    }

    //@@author ngxzs
    @Test
    void getSolution_oneQuestion_expectSolution() {
        createQuestion();

        assertEquals(solution1, oneQuestion.getSolution());
    }
}
