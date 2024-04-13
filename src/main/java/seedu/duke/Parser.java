package seedu.duke;

import seedu.duke.exceptions.CustomException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;

public class Parser {
    // parameters
    private static final int NO_PARAMETER_LENGTH = 1;
    private static final int ONE_PARAMETER_LENGTH = 2;
    private static final int TWO_PARAMETER_LENGTH = 3;
    private static final int TIMER_ONE_PARAMETER_LENGTH = 3;
    private static final int FIRST_PARAMETER = 1;
    private static final int SECOND_PARAMETER = 2;
    private static final int THIRD_PARAMETER = 3;
    private static final int THIRD_PARAMETER_INDEX = THIRD_PARAMETER - 1; //due to index 0
    private static final String DUMMY_QUESTION_PARAMETER = "1";

    private static final String COMMAND_SPLITTER = " ";
    private static final String HELP_PARAMETER = "help";
    private static final String LIST_PARAMETER = "list";
    private static final String TOPIC_PARAMETER = "topic";
    private static final String TIMED_MODE_PARAMETER = "timed mode";

    private static final String DETAILS_PARAMETER = "details";
    private static final String SOLUTION_PARAMETER = "solution";
    private static final String EXPLAIN_PARAMETER = "explain";
    private static final String RESULTS_PARAMETER = "results";
    private static final String BYE_PARAMETER = "bye";

    // states
    private static final int NO_RESULTS = 0;
    private static final String PAUSE_GAME = "pause";
    private static final String RESUME = "resume";
    private static final int NORMAL_TERMINATION = 0;

    private static final boolean INCLUDES_DETAILS = true;
    private static final boolean IS_CORRECT_ANSWER = true;

    // CustomError messages
    private static final String MESSAGE_NO_RESULTS = "There are no results.";
    private static final String MESSAGE_INVALID_PARAMETERS = "Invalid parameters.";
    private static final String MESSAGE_INDEX_OUT_OF_BOUNDS = "Index is out of bounds.";
    private static final String MESSAGE_INDEX_NOT_INTEGER = "Index must be an integer.";

    private static final String MESSAGE_INVALID_TOPIC_NUM = "Topic number is invalid.";
    private static final String MESSAGE_INVALID_QUESTION_NUM = "Question number is invalid.";

    private static final String MESSAGE_INVALID_COMMAND_FORMAT = "command format is invalid.";

    private static final String MESSAGE_UNSPECIFIED_TIME = "Please specify a time limit";
    private static final String MESSAGE_INVALID_TIME = "Time limit must be more than 0 seconds";
    private static final String MESSAGE_INVALID_COMMAND = "That's an invalid command.";

    private static final String OPTION_A = "a";
    private static final String OPTION_B = "b";
    private static final String OPTION_C = "d";
    private static final String OPTION_D = "d";


    // non-constant attributes
    private boolean isTimedMode = false;

    private int timeLimit = 0;



    public void parseCommand(

            String command, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic,
            ResultsList allResults, Helper helper, AnswerTracker userAnswers, Storage storage,
            ProgressManager progressManager
    ) throws CustomException {

        String lowerCaseCommand = command.toLowerCase();

        CommandList commandToken = CommandList.getCommandToken(command);
        if (ui.isPlaying) {

            if (lowerCaseCommand.startsWith(TIMED_MODE_PARAMETER)) {
                timeLimit = processTimedMode(lowerCaseCommand);
                isTimedMode = true;
            } else if (commandToken == CommandList.TOPIC) {
                // Still under testing.
                // beginStartCommand(command, ui, topicList, questionListByTopic, allResults, userAnswers);
                processStartCommand(lowerCaseCommand, ui, topicList, questionListByTopic,
                        allResults, userAnswers, isTimedMode, storage);
                isTimedMode = false;
            } else if (lowerCaseCommand.startsWith(SOLUTION_PARAMETER)) {
                // processSolutionCommand(lowerCaseCommand, ui, topicList, questionListByTopic);
                handleSolutionCommandRegEx(command, ui, topicList, questionListByTopic);
            } else if (commandToken == CommandList.CUSTOM) {
                handleCustomCommand(command, ui, topicList, questionListByTopic, allResults,
                        userAnswers, progressManager);
            } else if (commandToken == CommandList.CHECKPOINT) {
                handleCheckpointCommand(command, ui, topicList, questionListByTopic, allResults,
                        userAnswers, progressManager);
            } else if (commandToken == CommandList.EXPLAIN) {
                //processExplainCommand(lowerCaseCommand, ui, topicList, questionListByTopic);
                handleExplainCommandRegEx(command, ui, topicList, questionListByTopic);
            } else if (lowerCaseCommand.startsWith(RESULTS_PARAMETER)) {
                processResultsCommand(lowerCaseCommand, allResults, ui, questionListByTopic, userAnswers);
            } else if (lowerCaseCommand.contentEquals(BYE_PARAMETER)) {
                storage.saveProgress(allResults, topicList, userAnswers);
                ui.isPlaying = false;
            } else if (lowerCaseCommand.contentEquals(HELP_PARAMETER)) {
                processHelpCommand(lowerCaseCommand, ui, helper);
            } else if (lowerCaseCommand.contentEquals(LIST_PARAMETER)) {
                processListCommand(topicList, ui);
            } else {
                throw new CustomException(MESSAGE_INVALID_COMMAND);
            }
        }

    }

    private void processListCommand(TopicList topicList, Ui ui) {
        topicList.displayProgressBar();
        String[][] printData = topicList.listAllTopics();
        String[] tableHeader = {"index", "topic", "summary", "attempted"};
        ui.printTable(tableHeader, printData);
    }

    //@@author cyhjason29

    /**
     * Breaks the user's results command input into its parameters before processing them.
     *
     * @param lowerCaseCommand The input of the user altered to all lower case.
     * @param allResults List of all results.
     * @param ui User interface.
     * @param questionListByTopic List of questions sorted by topic.
     * @param userAnswers List of all user answers to questions.
     * @throws CustomException If invalid parameters.
     */
    private void processResultsCommand(String lowerCaseCommand, ResultsList allResults, Ui ui,
                                       QuestionListByTopic questionListByTopic, AnswerTracker userAnswers)
            throws CustomException {

        if (allResults.getSizeOfAllResults() == NO_RESULTS) {
            throw new CustomException(MESSAGE_NO_RESULTS);
        }
        String[] commandParts = lowerCaseCommand.trim().split(COMMAND_SPLITTER, TWO_PARAMETER_LENGTH);
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
                throw new CustomException(MESSAGE_INDEX_NOT_INTEGER);
            } catch (IndexOutOfBoundsException e) {
                throw new CustomException(MESSAGE_INDEX_OUT_OF_BOUNDS);
            }
        default:
            throw new CustomException(MESSAGE_NO_RESULTS);
        }
    }

    //@@author
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
                        storage, ui, timeLimit);

                topicList.get(topicNum - 1).markAsAttempted();
                topicList.displayProgressBar();
                ui.printFinishedTopic();
                ui.printTopicList(topicList, ui);

            } else if (isRandomTopicNum) {
                Helper helper = new Helper();
                topicNum = helper.generateRandomNumber(upperLimit);
            } else {
                throw new CustomException(MESSAGE_INVALID_TOPIC_NUM);
            }
        } catch (NumberFormatException error) {
            throw new CustomException(TOPIC_PARAMETER + " " + MESSAGE_INVALID_COMMAND_FORMAT);
        } catch (IllegalStateException error) {
            throw new CustomException(MESSAGE_INVALID_TOPIC_NUM);
        } catch(CustomException e) {
            throw e;
        }
    }

    //@@author hongyijie06

    /**
     * Handles timed mode
     *
     * @param lowerCaseCommand a String of the user's input in lower case
     * @return timeLimit, the time limit in seconds set by the user
     * @throws CustomException if command is not in the correct format
     */
    private int processTimedMode(String lowerCaseCommand) throws CustomException{
        checkTimingValidity(lowerCaseCommand);

        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER, TIMER_ONE_PARAMETER_LENGTH);
        Character last = lowerCaseCommand.charAt(lowerCaseCommand.length() - 1);
        if (last == ' '){
            commandParts[THIRD_PARAMETER_INDEX] =
                    commandParts[THIRD_PARAMETER_INDEX].substring(0, commandParts[THIRD_PARAMETER_INDEX].length() - 1);
        }
        int timeLimit = Integer.parseInt(commandParts[THIRD_PARAMETER_INDEX]);

        Ui.printTimedModeSelected();
        return timeLimit;
    }

    //@@author hongyijie06

    /**
     * Checks if format of user input is correct to enter timed mode
     *
     * @param lowerCaseCommand a String of the user's input in lower case
     * @throws CustomException if command is not in the correct format
     */
    private static void checkTimingValidity(String lowerCaseCommand) throws CustomException {
        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER);

        if (commandParts.length == ONE_PARAMETER_LENGTH) {
            throw new CustomException(MESSAGE_UNSPECIFIED_TIME);
        } else if (commandParts.length > TIMER_ONE_PARAMETER_LENGTH){
            throw new CustomException(MESSAGE_INVALID_COMMAND_FORMAT);
        }

        try {
            if (commandParts.length == TIMER_ONE_PARAMETER_LENGTH) {

                int timeLimit = Integer.parseInt(commandParts[THIRD_PARAMETER_INDEX]);

            }
        } catch (NumberFormatException e){
            throw new CustomException(MESSAGE_INDEX_NOT_INTEGER);
        }

        try {
            if (commandParts.length < TIMER_ONE_PARAMETER_LENGTH || commandParts[THIRD_PARAMETER_INDEX].equals("")) {
                throw new CustomException(MESSAGE_UNSPECIFIED_TIME);
            } else if (Integer.parseInt(commandParts[THIRD_PARAMETER_INDEX]) <= 0) {
                throw new CustomException(MESSAGE_INVALID_TIME);
            }

        } catch (NumberFormatException e) {
            throw new CustomException(MESSAGE_INVALID_PARAMETERS);
        }
    }

    //@@author hongyijie06

    /**
     * handles starting the chosen topic
     *
     * @param lowerCaseCommand a String of the user's input in lower case
     * @param ui user interface
     * @param topicList a TopicList of the list of topics, each containing their question sets
     * @param questionListByTopic the QuestionListByTopic of the questions sorted by topics
     * @param allResults the list of all results
     * @param userAnswers list of all user answers to questions
     * @param isTimedMode whether the game is in timed mode
     * @param storage storage that deals with game data
     * @throws CustomException if the command is not in the correct format
     */
    private void processStartCommand(
            String lowerCaseCommand, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic,
            ResultsList allResults, AnswerTracker userAnswers, boolean isTimedMode, Storage storage
    ) throws CustomException {
        assert (topicList.getSize() != NO_RESULTS) : MESSAGE_NO_RESULTS;

        String[] commandParts = lowerCaseCommand.split(COMMAND_SPLITTER);
        if (commandParts.length != ONE_PARAMETER_LENGTH) {
            throw new CustomException("invalid " + lowerCaseCommand + " command");
        }
        String commandParameter = commandParts[FIRST_PARAMETER];
        try {
            // if parameter is an Integer
            int topicNum = Integer.parseInt(commandParameter);
            // checks validity of parameter
            if (topicNum < 1 || topicNum > topicList.getSize() + 1) {
                throw new CustomException(MESSAGE_INVALID_TOPIC_NUM);
            }
            // checks if user wants a random topic num
            final int upperLimit = topicList.getSize() + 1;
            if (topicNum == upperLimit) {
                Helper helper = new Helper();
                topicNum = helper.generateRandomNumber(upperLimit);
            }
            assert (topicNum != 0) : MESSAGE_INVALID_TOPIC_NUM;
            assert (topicNum != upperLimit) : MESSAGE_INVALID_TOPIC_NUM;

            // prints questions
            ui.printChosenTopic(topicNum, topicList, questionListByTopic, allResults, userAnswers, isTimedMode,
                    storage, ui, timeLimit);

            topicList.get(topicNum - 1).markAsAttempted();

            ui.printFinishedTopic();
            ui.printTopicList(topicList, ui);

        } catch (NumberFormatException e) {
            throw new CustomException("invalid " + lowerCaseCommand + " parameter");
        }

    }

    // solution command
    //@@author
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
            throw new CustomException(SOLUTION_PARAMETER + " " + MESSAGE_INVALID_COMMAND_FORMAT);
        }

        // Keep track of the parameters provided.
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
            if (topicNum == 0 || topicNum > topicList.getSize()) {
                throw new CustomException(MESSAGE_INVALID_TOPIC_NUM);
            } else {
                hasAttemptedTopicBefore = topicList.get(topicNum - 1).hasAttempted();
                qnList = questionListByTopic.getQuestionSet(topicNum - 1);
                System.out.println("You've chosen topic number " + topicNum);
            }
        } catch (NumberFormatException error) {
            throw new CustomException(MESSAGE_INVALID_TOPIC_NUM);
        }

        // Extract question number
        try {
            String questionNumParameter = matcher.group(SECOND_PARAMETER);
            boolean questionNumParamProvided = !questionNumParameter.isEmpty();
            if (questionNumParamProvided) {
                questionNum = Integer.parseInt(questionNumParameter);
                if (questionNum <= 0 || questionNum > qnList.getSize()) {
                    throw new CustomException(MESSAGE_INVALID_QUESTION_NUM);
                } else {
                    hasQuestionNum = true;
                    validQuestionNum = true;
                    System.out.println("You've chosen qn number " + questionNum);
                }
            } else {
                emptyQuestionNumParam = true;
            }
        } catch (NumberFormatException error) {
            throw new CustomException(MESSAGE_INVALID_QUESTION_NUM);
        }

        if (hasAttemptedTopicBefore) {
            if (hasQuestionNum) {
                String solution = qnList.getOneSolution(questionNum);
                ui.printOneSolution(questionNum, solution);

            } else if(emptyQuestionNumParam) {

                String allSolution = qnList.getAllSolutions();
                ui.printAllSolutions(allSolution);
            } else {
                System.out.println(MESSAGE_INVALID_QUESTION_NUM);
            }
        } else {
            ui.printNoSolutionAccess();
        }
    }

    private void handleExplainCommandRegEx(
            String command, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic)
            throws CustomException {

        Pattern explainPattern = Pattern.compile(CommandList.getExplainPattern());
        Matcher matcher = explainPattern.matcher(command);
        boolean foundMatch = matcher.find();

        if(!foundMatch) {
            throw new CustomException("Exception caught! You've entered an invalid format.");
        }

        String topicNumParam = matcher.group(FIRST_PARAMETER);
        String questionNumParam = matcher.group(SECOND_PARAMETER);

        boolean isTopicNumParamOverflowing = isParamOverflowing(topicNumParam);
        boolean isQuestionNumParamOverflowing = isParamOverflowing(questionNumParam);
        boolean isQuestionNumParamProvided = !questionNumParam.isEmpty();

        if(isTopicNumParamOverflowing || isQuestionNumParamOverflowing) {
            throw new CustomException("Exception caught! You've entered a parameter that is too long.");
        }

        final int numOfTopics = topicList.getSize();
        int topicNum = getTopicNum(topicNumParam, numOfTopics);

        final int indexOfTopicNum = topicNum - 1;
        QuestionsList qnList = questionListByTopic.getQuestionSet(indexOfTopicNum);

        final int uninitializedQuestionNum = -1;
        int questionNum = uninitializedQuestionNum;
        final int numOfQuestions = qnList.getSize();

        if(isQuestionNumParamProvided) {
            questionNum = getQuestionNum(questionNumParam, numOfQuestions);
        }

        boolean hasAttemptedTopicBefore = topicList.get(indexOfTopicNum).hasAttempted();

        if(hasAttemptedTopicBefore) {
            if(isQuestionNumParamProvided) {
                String selectedExplanation = qnList.getOneExplanation(questionNum);
                ui.printOneExplanation(questionNum, selectedExplanation);
            } else {
                String allExplanations = qnList.getAllExplanations();
                ui.printAllExplanations(allExplanations);
            }
        } else {
            ui.printNoSolutionAccess();
        }
    }

    //@@author ngxzs
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

    //@@author
    private void handleCustomCommand(
            String command, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic,
            ResultsList allResults, AnswerTracker userAnswers, ProgressManager progressManager)
            throws CustomException {

        ui.printCustomModeMessage();

        int numOfTopics = topicList.getSize();
        System.out.println("There are " + numOfTopics + " topics to choose from.");
        int topicNum = ui.getCustomTopicNum();
        if(topicNum <= 0 || topicNum > numOfTopics) {
            throw new CustomException("That topic number does not exist.");
        }

        QuestionsList chosenQuestionsList = questionListByTopic.getQuestionSet(topicNum - 1);
        int numOfQnInChosenTopic = chosenQuestionsList.getSize();

        System.out.println("There are " + numOfQnInChosenTopic + " questions in this topic.");
        int numOfQuestions = ui.getCustomNumOfQuestions();
        if(numOfQuestions <= 0 || numOfQuestions > numOfQnInChosenTopic) {
            throw new CustomException("That's not a valid number of questions.");
        }

        ArrayList<Integer> randomQuestionNumbers = new ArrayList<Integer>();
        for(int i = 0; i < numOfQuestions; i++) {
            randomQuestionNumbers.add(i);
        }
        Collections.shuffle(randomQuestionNumbers);

        QuestionsList customQuestionsList = new QuestionsList();
        for(int i = 0; i < numOfQuestions; i++) {
            int randomQuestionNumber = randomQuestionNumbers.get(i);
            Question randomQuestion = chosenQuestionsList.getQuestionUnit(randomQuestionNumber);
            customQuestionsList.addQuestion(randomQuestion);
        }

        System.out.println("Here are your custom questions.");
        boolean isInCheckpointMode = progressManager.isInCheckpointMode();
        for(int i = 0; i < numOfQuestions; i++) {

            ui.printQuestion(customQuestionsList.getQuestionUnit(i));
            ui.askForAnswerInput();
            String userAnswerInput = ui.getUserAnswerInput();
            ui.displayUserAnswer(userAnswerInput);

            if(isInCheckpointMode) {
                progressManager.incrementNumOfAttemptedCustomQuestions();
            }
        }

        System.out.println("You have completed " + numOfQuestions + " questions from topic " + topicNum);

        if(isInCheckpointMode) {
            int checkpointModeGoal = progressManager.getCheckpointModeGoal();
            int numOfAttemptedCustomQuestions = progressManager.getNumOfAttemptedCustomQuestions();

            if(numOfAttemptedCustomQuestions >= checkpointModeGoal) {
                System.out.println("Congrats, you've reached your checkpoint goal.");
                progressManager.clearCheckpointModeGoal();
                progressManager.clearNumOfAttemptedCustomQuestions();
                progressManager.clearCheckpointMode();
            }
        }
    }

    private void handleCheckpointCommand(
            String command, Ui ui, TopicList topicList, QuestionListByTopic questionListByTopic,
            ResultsList allResults, AnswerTracker userAnswers, ProgressManager progressManager)
            throws CustomException {
        // For now, checkpoint command is only for custom mode.

        boolean isAlreadyInCheckpointMode = progressManager.isInCheckpointMode();
        if(isAlreadyInCheckpointMode) {
            int goal = progressManager.getCheckpointModeGoal();
            int numOfAttemptedCustomQuestions = progressManager.getNumOfAttemptedCustomQuestions();
            int numOfQuestionsToHitGoal = goal - numOfAttemptedCustomQuestions;
            System.out.println("You're already in checkpoint mode.");
            System.out.println("Your goal is to attempt " + goal + " questions.");
            System.out.println("You have " + numOfQuestionsToHitGoal + " more to go.");
            return;
        }

        int checkpointGoal = ui.getCheckpointGoal();

        int totalNumOfTopics = topicList.getSize();
        int totalNumOfQuestions = 0;
        for(int i = 0; i < totalNumOfTopics; i++) {
            QuestionsList currentQuestionsList = questionListByTopic.getQuestionSet(i);
            int numOfQuestions = currentQuestionsList.getSize();
            totalNumOfQuestions += numOfQuestions;
        }
        if(checkpointGoal > totalNumOfQuestions) {
            System.out.println("There aren't that many questions available.");
            System.out.println("Pick a goal that is lesser or equals to " + totalNumOfQuestions);
        } else if (checkpointGoal <= 0) {
            System.out.println("That is an invalid goal.");
        } else {
            progressManager.setCheckpointMode();
            progressManager.setCheckpointModeGoal(checkpointGoal);
            System.out.println("You've chosen a goal of " + progressManager.getCheckpointModeGoal() + " questions.");
        }
    }
    //@@author ngxzs
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
            throw new CustomException(MESSAGE_INDEX_OUT_OF_BOUNDS);
        }
        return parameterNum;
    }

    //@@author
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
            throw new CustomException(HELP_PARAMETER + " " + MESSAGE_INVALID_COMMAND_FORMAT);
        }

        if (commandParts.length == 1) {
            String[][] printData = helper.listAllCommands();
            String[] tableHeader = {"command", "function", "usage"};
            ui.printTable(tableHeader, printData);
        } else {
            // TODO: given a command, find and print the detailed usage for that command
        }
    }

    //@@author cyhjason29

    /**
     * Checks if the user wants to pause, and resume or exit the game when already paused.
     *
     * @param answer User input.
     * @param allResults List of all results.
     * @param topicList List of topics.
     * @param userAnswers List of all user answers to questions.
     * @param ui User interface.
     * @param storage Storage that deals with save data.
     * @param isPaused If the game is currently paused.
     * @param isTimedMode If the game is currently in timed mode.
     * @param allAnswers User answers within the current attempt.
     * @param answersCorrectness User answer correctness within the current attempt.
     * @param topicResults User results within the current attempt.
     * @param topicNum The number of the topic which the user is currently attempting.
     * @param index The question number the user is currently at
     * @return The pause status of the game
     * @throws CustomException If there is error saving the game data.
     */
    public boolean checkPause(String answer, ResultsList allResults, TopicList topicList,
                              AnswerTracker userAnswers, Ui ui, Storage storage, boolean isPaused, boolean isTimedMode,
                              ArrayList<String> allAnswers, ArrayList<Boolean> answersCorrectness,
                              Results topicResults, int topicNum, int index)
            throws CustomException {
        if (isTimedMode && answer.equalsIgnoreCase(PAUSE_GAME)) {
            Ui.showCannotPause();
            return false;
        }
        if (!isPaused && !answer.equalsIgnoreCase(PAUSE_GAME)) {
            return false;
        }
        if (isPaused && answer.equalsIgnoreCase(BYE_PARAMETER)) {
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

    /**
     * Checks the format of the user answer.
     *
     * @param answer User input.
     * @param ui User interface.
     * @return Whether the user answer is of the right format.
     */
    public boolean checkFormat(String answer, Ui ui) {
        if (answer.equalsIgnoreCase(OPTION_A) || answer.equalsIgnoreCase(OPTION_B)
                || answer.equalsIgnoreCase(OPTION_C) || answer.equalsIgnoreCase(OPTION_D)) {
            return true;
        }
        ui.showCorrectFormat();
        return false;
    }

    private boolean isParamOverflowing(String param) {
        final int maxParamLength = 5;
        int paramLength = param.length();

        if(paramLength > maxParamLength) {
            return true;
        } else {
            return false;
        }
    }

    private int getTopicNum(String topicNumParam, int numOfTopics) throws CustomException {

        try {
            int topicNum = Integer.parseInt(topicNumParam);

            if(topicNum <=0 || topicNum > numOfTopics) {
                throw new CustomException("Exception caught! You've entered an invalid topic number.");
            }

            return topicNum;
        } catch (NumberFormatException error) {
            throw new CustomException("Exception caught! Unable to parse topic number provided.");
        }
    }

    private int getQuestionNum(String questionNumParam, int numOfQuestions) throws CustomException {

        try {
            int questionNum = Integer.parseInt(questionNumParam);

            if(questionNum <= 0 || questionNum > numOfQuestions) {
                throw new CustomException("Exception caught! You've entered an invalid question number.");
            }

            return questionNum;
        } catch (NumberFormatException error) {
            throw new CustomException("Exception caught! Unable to parse question number provided.");
        }
    }
}

