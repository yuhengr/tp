package seedu.duke;

public class Question {
    private static final int FIRST_OPTION = 0;
    private static final int SECOND_OPTION = 1;
    private static final int THIRD_OPTION = 2;
    private static final int FOURTH_OPTION = 3;
    private static final String OPTION_A = "a. ";
    private static final String OPTION_B = "b. ";
    private static final String OPTION_C = "c. ";
    private static final String OPTION_D = "d. ";
    private String question;
    private String solution;
    private String explanation;
    private String[] options;

    //@@author ngxzs
    public Question(String question, String solution, String explanation, String[] options){
        this.question = question;
        this.solution = solution;
        this.options = options;
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }
    //@@author cyhjason29
    public String getQuestion() {
        String optionA = options[FIRST_OPTION];
        String optionB = options[SECOND_OPTION];
        String optionC = options[THIRD_OPTION];
        String optionD = options[FOURTH_OPTION];
        return question + System.lineSeparator() + OPTION_A + optionA + System.lineSeparator() + OPTION_B + optionB +
                System.lineSeparator() + OPTION_C + optionC + System.lineSeparator() + OPTION_D + optionD;
    }
    //@@author ngxzs
    public String getSolution() {
        return solution;
    }
}

