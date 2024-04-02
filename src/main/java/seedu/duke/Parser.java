package seedu.duke;

import seedu.duke.exceptions.CustomException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final int NO_RESULTS = 0;
    private static final int NO_PARAMETER_LENGTH = 1;
    private static final int ONE_PARAMETER_LENGTH = 2;
    private static final int TWO_PARAMETER_LENGTH = 3;
    private static final int FIRST_PARAMETER = 1;
    private static final int SECOND_PARAMETER = 2;
    private static final String DUMMY_QUESTION_PARAMETER = "1";

    private static final String COMMAND_SPLITTER = " ";

    private static final String DETAILS_PARAMETER = "details";
    private static final String SOLUTION_PARAMETER = "solution";
    private static final String EXPLAIN_PARAMETER = "explain";

    private static final String MESSAGE_NO_RESULTS = "There are no results.";
    private static final String MESSAGE_ERROR = "An error has occurred.";
    private static final String MESSAGE_INVALID_PARAMETERS = "Invalid parameters.";
    private static final String MESSAGE_INDEX_OUT_OF_BOUNDS = "Index is out of bounds.";
    private static final String MESSAGE_INVALID_INDEX = "Index must be an integer.";

    private static final String MESSAGE_INVALID_TOPIC_NUM = "Topic number is invalid.";

    private static final String MESSAGE_INVALID_TOPIC_COMMAND_FORMAT = "Topic command format is invalid.";
    private static final String PAUSE_GAME = "pause";
    private static final String RESUME = "resume";
    private static final String BYE = "bye";
    private static final int NORMAL_TERMINATION = 0;

    private static final boolean INCLUDES_DETAILS = true;
    private static final boolean IS_CORRECT_ANSWER = true;
    private boolean isTimedMode = false;


    public void parseCommand(

            String command, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic,
            ResultsList allResults, Helper helper, AnswerTracker userAnswers, Storage storage
    ) throws CustomException {

        String lowerCaseCommand = command.toLowerCase();

        CommandList commandToken = CommandList.getCommandToken(command);
        if (ui.isPlaying) {

            if (lowerCaseCommand.contentEquals("timed mode")) {
                ui.printTimedModeSelected();
                isTimedMode = true;
            }
            if (commandToken == CommandList.TOPIC) {
                // Still under testing.
                // beginStartCommand(command, ui, topicList, questionListByTopic, allResults, userAnswers);
                processStartCommand(lowerCaseCommand, ui, topicList, questionListByTopic,
                        allResults, userAnswers, isTimedMode, storage);
                isTimedMode = false;
            } else if (lowerCaseCommand.startsWith("solution")) {
                processSolutionCommand(lowerCaseCommand, ui, topicList, questionListByTopic);
                // handleSolutionCommandRegEx(command, ui, topicList, questionListByTopic);
            } else if (lowerCaseCommand.startsWith("explain")) {
                processExplainCommand(lowerCaseCommand, ui, topicList, questionListByTopic);
            } else if (lowerCaseCommand.startsWith("results")) {
                processResultsCommand(lowerCaseCommand, allResults, ui, questionListByTopic, userAnswers);
            } else if (lowerCaseCommand.contentEquals("bye")) {
                storage.saveProgress(allResults, topicList, userAnswers);
                ui.isPlaying = false;
            } else if (lowerCaseCommand.contentEquals("help")) {
                processHelpCommand(lowerCaseCommand, ui, helper);
            } else if (lowerCaseCommand.contentEquals("list")) {
                processListCommand(topicList, ui);
            } else if (!lowerCaseCommand.contentEquals("timed mode")) {
                throw new CustomException("-1 HP coz invalid command");
            }
        }

    }

    private void processListCommand(TopicList topicList, Ui ui) {
        String[][] printData = topicList.listAllTopics();
        String[] tableHeader = {"index", "topic", "summary", "attempted"};
        ui.printTable(tableHeader, printData);
    }

    private void processResultsCommand(String lowerCaseCommand, ResultsList allResults, Ui ui,
                                       QuestionListByTopic questionListByTopic, AnswerTracker userAnswers)
            throws CustomException {

        if (allResults.getSizeOfAllResults() == NO_RESULTS) {
            throw new CustomException(MESSAGE_NO_RESULTS);
        }
        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER, TWO_PARAMETER_LENGTH);
        assert commandParts.length <= TWO_PARAMETER_LENGTH;
        switch (commandParts.length) {
        case (NO_PARAMETER_LENGTH):
            ui.printAllResults(!INCLUDES_DETAILS, allResults, questionListByTopic, userAnswers);
            break;
        case (ONE_PARAMETER_LENGTH):
            if (commandParts[FIRST_PARAMETER].equals(DETAILS_PARAMETER)) {
                ui.printAllResults(INCLUDES_DETAILS, allResults, questionListByTopic, userAnswers);
            } else {
                try {
                    int index = Integer.parseInt(commandParts[FIRST_PARAMETER]);
                    String score = allResults.getSpecifiedResult(index - 1).getScore();
                    int topicNum = allResults.getTopicNum(index - 1);
                    ui.printOneResult(!INCLUDES_DETAILS, topicNum, score, questionListByTopic, userAnswers, index);
                } catch (NumberFormatException e) {
                    throw new CustomException(MESSAGE_INVALID_PARAMETERS);
                } catch (IndexOutOfBoundsException e) {
                    throw new CustomException(MESSAGE_INDEX_OUT_OF_BOUNDS);
                }
            }
            break;
        case (TWO_PARAMETER_LENGTH):
            if (!commandParts[FIRST_PARAMETER].equals(DETAILS_PARAMETER)) {
                throw new CustomException(MESSAGE_INVALID_PARAMETERS);
            }
            try {
                int index = Integer.parseInt(commandParts[SECOND_PARAMETER]);
                String score = allResults.getSpecifiedResult(index - 1).getScore();
                int topicNum = allResults.getTopicNum(index - 1);
                ui.printOneResult(INCLUDES_DETAILS, topicNum, score, questionListByTopic, userAnswers, index);
                break;
            } catch (NumberFormatException e) {
                throw new CustomException(MESSAGE_INVALID_INDEX);
            } catch (IndexOutOfBoundsException e) {
                throw new CustomException(MESSAGE_INDEX_OUT_OF_BOUNDS);
            }
        default:
            throw new CustomException(MESSAGE_ERROR);
        }
    }

    private void beginStartCommand(
            String command, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic,
            ResultsList allResults, AnswerTracker userAnswers, Storage storage
    ) throws CustomException {

        Pattern topicPattern = Pattern.compile(CommandList.getTopicPattern());
        Matcher matcher = topicPattern.matcher(command);
        boolean foundMatch = matcher.find();

        if (!foundMatch) {
            throw new CustomException("Can't find a match.");
        }

        try {
            int topicNum = Integer.parseInt(matcher.group(1));
            System.out.println("You've chosen topic number " + topicNum);
            final int upperLimit = topicList.getSize() + 1;
            boolean validTopicNum = (topicNum < upperLimit) && topicNum != 0;
            boolean isRandomTopicNum = topicNum == upperLimit;

            if (validTopicNum) {
                ui.printChosenTopic(topicNum, topicList, questionListByTopic, allResults, userAnswers, isTimedMode,
                        storage, ui);
                System.out.println("You've finished the topic. What will be your next topic?");
                topicList.get(topicNum - 1).markAsAttempted();
                ui.printTopicList(topicList, ui);
            } else if (isRandomTopicNum) {
                Helper helper = new Helper();
                topicNum = helper.generateRandomNumber(upperLimit);
            } else {
                throw new CustomException(MESSAGE_INVALID_TOPIC_NUM);
            }
        } catch (NumberFormatException error) {
            throw new CustomException(MESSAGE_INVALID_TOPIC_COMMAND_FORMAT);
        } catch (IllegalStateException error) {
            throw new CustomException(MESSAGE_INVALID_TOPIC_NUM);
        } catch(CustomException e) {
            throw e;
        }
    }

    private void processStartCommand(
            String lowerCaseCommand, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic,
            ResultsList allResults, AnswerTracker userAnswers, boolean isTimedMode, Storage storage
    ) throws CustomException {
        assert (topicList.getSize() != NO_RESULTS) : "Size of topicList should never be 0";

        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER);
        if (commandParts.length != 2) {
            throw new CustomException("invalid " + lowerCaseCommand + " command");
        }
        String commandParameter = commandParts[FIRST_PARAMETER];
        try {
            // if parameter is an Integer
            int topicNum = Integer.parseInt(commandParameter);
            // checks validity of parameter
            if (topicNum < 1 || topicNum > topicList.getSize() + 1) {
                throw new CustomException("No such topic");
            }
            // checks if user wants a random topic num
            final int upperLimit = topicList.getSize() + 1;
            if (topicNum == upperLimit) {
                Helper helper = new Helper();
                topicNum = helper.generateRandomNumber(upperLimit);
            }
            assert (topicNum != 0) : "topicNum should not be 0";
            assert (topicNum != upperLimit) : "topicNum should not be upperLimit";

            // prints questions
            ui.printChosenTopic(topicNum, topicList, questionListByTopic, allResults, userAnswers, isTimedMode,
                    storage, ui);
            ui.printTopicCompleted();
            topicList.get(topicNum - 1).markAsAttempted();
            ui.printTopicList(topicList, ui);

        } catch (NumberFormatException e) {
            throw new CustomException("invalid " + lowerCaseCommand + " parameter");
        }

    }

    // solution and explain commands
    private void processSolutionCommand(
            String lowerCaseCommand, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic)
            throws CustomException {
        // process command
        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER);
        boolean hasTwoParameters = checkIfTwoParameters(SOLUTION_PARAMETER, commandParts);

        // process parameters
        String commandParameterTopic = commandParts[FIRST_PARAMETER];
        String commandParameterQn = hasTwoParameters ? commandParts[SECOND_PARAMETER] : DUMMY_QUESTION_PARAMETER;

        int topicNum = getTopicOrQuestionNum(commandParameterTopic, topicList.getSize());
        QuestionsList qnList = questionListByTopic.getQuestionSet(topicNum - 1);
        int questionNum = getTopicOrQuestionNum(commandParameterQn, qnList.getSize());

        // checks if attempted topic before
        if (!topicList.get(topicNum - 1).hasAttempted()) {
            ui.printNoSolutionAccess(); // has not attempted
            return;
        }

        if (hasTwoParameters) {
            // get specific solution
            String solution = qnList.getOneSolution(questionNum);
            ui.printOneSolution(questionNum, solution);
        } else {
            // get all solutions
            String allSolutions = qnList.getAllSolutions();
            ui.printAllSolutions(allSolutions);
        }
    }

    private void handleSolutionCommandRegEx(
            String command, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic)
            throws CustomException {

        Pattern solutionPattern = Pattern.compile(CommandList.getSolutionPattern());
        Matcher matcher = solutionPattern.matcher(command);
        boolean foundMatch = matcher.find();

        if (!foundMatch) {
            throw new CustomException("Invalid format for solution command.");
        }

        // Keep track of the parameters provided.
        final int FIRST_PARAMETER = 1; //checkstyleError
        final int SECOND_PARAMETER = 2;
        int topicNum;
        int questionNum = 0;
        boolean emptyQuestionNumParam = false;
        boolean hasQuestionNum = false;
        boolean validQuestionNum = false;
        boolean hasAttemptedTopicBefore = false;

        // For storing the topic to be displayed.
        QuestionsList qnList;

        // Extract the topic number from the command.
        try {
            String topicNumParam = matcher.group(FIRST_PARAMETER);
            topicNum = Integer.parseInt(topicNumParam);
            if (topicNum == 0) {
                throw new CustomException("Topic number cannot be 0");
            } else {
                hasAttemptedTopicBefore = topicList.get(topicNum - 1).hasAttempted();
                qnList = questionListByTopic.getQuestionSet(topicNum - 1);
                System.out.println("You've chosen topic number " + topicNum);
            }
        } catch (NumberFormatException error) {
            throw new CustomException("NumberFormatException error for topic number");
        }

        // Extract question number
        try {
            String questionNumParameter = matcher.group(SECOND_PARAMETER);
            boolean questionNumParamProvided = !questionNumParameter.isEmpty();
            if (questionNumParamProvided) {
                questionNum = Integer.parseInt(questionNumParameter);
                if (questionNum <= 0) {
                    throw new CustomException("Question number is invalid.");
                } else {
                    hasQuestionNum = true;
                    validQuestionNum = true;
                    System.out.println("You've chosen qn number " + questionNum);
                }
            } else {
                emptyQuestionNumParam = true;
            }
        } catch (NumberFormatException error) {
            throw new CustomException("NumberFormatException error for question number");
        }

        if (hasAttemptedTopicBefore) {
            if (hasQuestionNum) {
                String solution = qnList.getOneSolution(questionNum);
                ui.printOneSolution(questionNum, solution);
            } else if (emptyQuestionNumParam) {
                String allSolution = qnList.getAllSolutions();
                ui.printAllSolutions(allSolution);
            } else {
                System.out.println("You've provided an invalid question number.");
            }
        } else {
            ui.printNoSolutionAccess();
        }
    }

    private void processExplainCommand(
            String lowerCaseCommand, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic)
            throws CustomException {
        // process command
        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER);
        boolean hasTwoParameters = checkIfTwoParameters(EXPLAIN_PARAMETER, commandParts);

        // process parameters
        String commandParameterTopic = commandParts[FIRST_PARAMETER];
        String commandParameterQn = hasTwoParameters ? commandParts[SECOND_PARAMETER] : DUMMY_QUESTION_PARAMETER;

        int topicNum = getTopicOrQuestionNum(commandParameterTopic, topicList.getSize());
        QuestionsList qnList = questionListByTopic.getQuestionSet(topicNum - 1);
        int questionNum = getTopicOrQuestionNum(commandParameterQn, qnList.getSize());

        // checks if attempted topic before
        if (!topicList.get(topicNum - 1).hasAttempted()) {
            ui.printNoSolutionAccess(); // has not attempted
            return;
        }

        if (hasTwoParameters) {
            // get specific explanation
            String explanation = qnList.getOneExplanation(questionNum);
            ui.printOneExplanation(questionNum, explanation);
        } else {
            // get all explanations
            String allExplanations = qnList.getAllExplanations();
            ui.printAllExplanations(allExplanations);
        }
    }

    // checks valid command type and parameters: returns true if 2 parameters, else false (1 param only)
    private static boolean checkIfTwoParameters(
            String expectedCommandType, String[] commandParts) throws CustomException {
        int commandPartsLength = commandParts.length;
        String actualCommandType = commandParts[0];

        // checks validity of command
        if (!actualCommandType.contentEquals(expectedCommandType)) {
            throw new CustomException("Do you mean " + expectedCommandType + " instead?");
        }

        // checks correct number of parameters (1 or 2 only)
        if (commandPartsLength == NO_PARAMETER_LENGTH || commandPartsLength > TWO_PARAMETER_LENGTH) {
            throw new CustomException(MESSAGE_INVALID_PARAMETERS);
        }

        return (commandPartsLength == TWO_PARAMETER_LENGTH);
    }

    // convert String commandParameter to int topicNum/ questionNum and check validity
    private int getTopicOrQuestionNum(String commandParameter, int maxSize) throws CustomException {
        int parameterNum;
        // check if topic/ questionNum is a number
        try {
            parameterNum = Integer.parseInt(commandParameter);
        } catch (NumberFormatException e) {
            throw new CustomException(MESSAGE_INVALID_PARAMETERS);
        }
        // checks validity
        if (parameterNum < 1 || parameterNum > maxSize) {
            throw new CustomException("No such topic or question");
        }
        return parameterNum;
    }

    public void handleAnswerInputs(String[] inputAnswers, int index, String answer, Question questionUnit,
                                   Results topicResults, ArrayList<Boolean> correctness) {
        inputAnswers[index] = answer;
        String correctAnswer = questionUnit.getSolution();
        if (answer.equals(correctAnswer)) {
            topicResults.increaseCorrectAnswers();
            correctness.add(IS_CORRECT_ANSWER);
        } else {
            correctness.add(!IS_CORRECT_ANSWER);
        }
    }

    private void processHelpCommand(String lowerCaseCommand, Ui ui, Helper helper) throws CustomException {
        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER);
        if (commandParts.length != 1 && commandParts.length != 2) {
            throw new CustomException("invalid help command parameter");
        }

        if (commandParts.length == 1) {
            String[][] printData = helper.listAllCommands();
            String[] tableHeader = {"command", "function", "usage"};
            ui.printTable(tableHeader, printData);
        } else {
            // TODO: given a command, find and print the detailed usage for that command
        }
    }

    public boolean checkPause(String answer, ResultsList allResults, TopicList topicList,
                              AnswerTracker userAnswers, Ui ui, Storage storage, boolean isPaused, boolean isTimedMode,
                              ArrayList<String> allAnswers, ArrayList<Boolean> answersCorrectness,
                              Results topicResults, int topicNum, int index)
            throws CustomException {
        if (isTimedMode) {
            ui.showCannotPause();
            return false;
        }
        if (!isPaused && !answer.equalsIgnoreCase(PAUSE_GAME)) {
            return false;
        }
        if (isPaused && answer.equalsIgnoreCase(BYE)) {
            storage.pauseGame(allResults, topicList, userAnswers, allAnswers, answersCorrectness, topicResults,
                    topicNum, index);
            ui.sayBye();
            System.exit(NORMAL_TERMINATION);
        }
        if (isPaused && answer.equalsIgnoreCase(RESUME)) {
            ui.showResume();
            return false;
        }
        ui.askForResume();
        return true;
    }
}

