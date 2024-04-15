//@@author cyhjason29
package seedu.duke;

import seedu.duke.exceptions.CustomException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with loading from the save file and saving in that file
 * Also deals with initial loading of dataset
 */
public class Storage {
    private static final String RESULTS_HEADER = "result";
    private static final String TOPIC_HEADER = "topic";
    private static final String ANSWER_HEADER = "answer";
    private static final String CORRECTNESS_HEADER = "correctness";
    private static final String PAUSE_HEADER = "pause";
    private static final String TEMP_RESULT_HEADER = "temp_result";
    private static final String TEMP_ANSWER_HEADER = "temp_answer";
    private static final String TEMP_CORRECTNESS_HEADER = "temp_correctness";
    private static final int STARTING_INDEX_TOPIC = 6;
    private static final int STARTING_INDEX_RESULT = 7;
    private static final int STARTING_INDEX_ANSWER = 7;
    private static final int STARTING_INDEX_CORRECTNESS = 12;
    private static final int STARTING_INDEX_TEMP_RESULT = 12;
    private static final int STARTING_INDEX_TEMP_ANSWER = 12;
    private static final int STARTING_INDEX_TEMP_CORRECTNESS = 17;
    private static final int STARTING_INDEX_TOPIC_NUM = 6;
    private static final int FIRST_ANSWER = 0;
    private static final int RESULTS_INDEX = 0;
    private static final int TOPIC_NUMBER_INDEX = 1;
    private static final int INDEX_NUMBER_OF_CORRECT_ANSWERS = 0;
    private static final int INDEX_TOTAL_NUMBER_OF_QUESTIONS = 1;
    private static final int INDEX_SCORE = 2;
    private static final int INDEX_TOPIC_NUM = 0;
    private static final int INDEX_INDEX = 1;
    private static final int TWO_PARAMETERS = 2;
    private static final String RESULTS_SEPARATOR = "\\+";
    private static final String ARG_SEPARATOR = "\\|";
    private static final String SAVE_PAUSE_HEADER = "pause ";
    private static final String SAVE_ARG_SEPARATOR = " | ";
    private static final String FILE_PATH = "data/player2113.txt";
    private static final String FOLDER_NAME = "data";
    private static final String MESSAGE_ERROR_INIT = "There was an error initiating the save file.";
    private static final String MESSAGE_ERROR_WRITING = "There was an error writing to the save file.";
    private static final String TEMP_RESULT = "temp_result ";
    private static final String TEMP_ANSWER = "temp_answer ";
    private static final String TEMP_CORRECTNESS = "temp_correctness ";
    private static final String SAVE_RESULT_HEADER = "result ";
    private static final String SAVE_RESULTS_SEPARATOR = " + ";
    private static final String SAVE_TOPIC_HEADER = "topic ";
    private static final String SAVE_ANSWER_HEADER = "answer ";
    private static final String SAVE_CORRECTNESS_HEADER = "correctness ";

    private static boolean isPaused;


    /**
     * Constructs the Storage object.
     */
    public Storage() {
        isPaused = false;
    }

    /**
     * Loads in all the save data from the save file.
     *
     * @param f       Save file.
     * @param results List of all results.
     * @param topics  List of all topics.
     * @param answers List of all user answers to questions.
     * @return If the user previously paused and exited the game.
     * @throws FileNotFoundException If there was an error finding the save file.
     */
    public boolean loadProgress(File f, ResultsList results, TopicList topics, AnswerTracker answers)
            throws FileNotFoundException {
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String line = s.nextLine();
            processLine(line, results, topics, answers);
        }
        return isPaused;
    }

    /**
     * Process each line within the save file.
     *
     * @param line    The line read in from the save file.
     * @param results List of all results.
     * @param topics  List of all topics.
     * @param answers List of all user answers to questions.
     */
    private static void processLine(String line, ResultsList results, TopicList topics, AnswerTracker answers) {
        if (line.startsWith(RESULTS_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_RESULT).split(ARG_SEPARATOR);

            String result = processedLine[RESULTS_INDEX].trim();
            Results temp = createResults(result);
            results.addResults(temp);

            int topicNum = Integer.parseInt(processedLine[TOPIC_NUMBER_INDEX].trim());
            results.addQuestions(topicNum);
        } else if (line.startsWith(TOPIC_HEADER)) {
            String topicName = line.substring(STARTING_INDEX_TOPIC).trim();
            ArrayList<Topic> topicList = topics.getTopicList();
            for (Topic topic : topicList) {
                if (topic.getTopicName().equals(topicName)) {
                    topic.markAsAttempted();
                    break;
                }
            }
        } else if (line.startsWith(ANSWER_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_ANSWER).split(ARG_SEPARATOR);
            ArrayList<String> userAnswers = new ArrayList<>();
            for (String answer : processedLine) {
                userAnswers.add(answer.trim());
            }
            answers.addUserAnswers(userAnswers);
        } else if (line.startsWith(CORRECTNESS_HEADER)) {
            String[] processedLine = line.substring(STARTING_INDEX_CORRECTNESS).split(ARG_SEPARATOR);
            ArrayList<Boolean> answerCorrectness = new ArrayList<>();
            for (String correctness : processedLine) {
                answerCorrectness.add(Boolean.parseBoolean(correctness.trim()));
            }
            answers.addUserCorrectness(answerCorrectness);
        } else if (line.startsWith(PAUSE_HEADER)) {
            isPaused = true;
        }
    }

    /**
     * Resumes the game if the user previously paused and exited the game.
     *
     * @param file         The save file
     * @param topicResults User results within the current attempt.
     * @param userAnswers  User answers within the current attempt.
     * @param correctness  User answer correctness within the current attempt.
     * @return The topic number and question number that the user previously paused on.
     * @throws FileNotFoundException If there was an error locating the save file.
     */
    public int[] resumeGame(File file, Results topicResults, ArrayList<String> userAnswers,
                            ArrayList<Boolean> correctness)
            throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int[] pausedQuestion = new int[TWO_PARAMETERS];
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith(PAUSE_HEADER)) {
                pausedQuestion = getPausedQuestion(line.substring(STARTING_INDEX_TOPIC_NUM).trim());
            } else if (line.startsWith(TEMP_RESULT_HEADER)) {
                createTempResult(topicResults, line.substring(STARTING_INDEX_TEMP_RESULT).trim());
            } else if (line.startsWith(TEMP_ANSWER_HEADER)) {
                createTempAnswers(userAnswers, line.substring(STARTING_INDEX_TEMP_ANSWER).trim());
            } else if (line.startsWith(TEMP_CORRECTNESS_HEADER)) {
                createTempCorrectness(correctness, line.substring(STARTING_INDEX_TEMP_CORRECTNESS).trim());
            }
        }
        return pausedQuestion;
    }

    /**
     * Retrieves the topic number and question number which the user previously paused on.
     *
     * @param line The line read in from the save file.
     * @return An array containing the topic number and question number.
     */
    private static int[] getPausedQuestion(String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        int[] pausedQuestion = new int[TWO_PARAMETERS];
        pausedQuestion[INDEX_TOPIC_NUM] = Integer.parseInt(processedLine[INDEX_TOPIC_NUM].trim());
        pausedQuestion[INDEX_INDEX] = Integer.parseInt(processedLine[INDEX_INDEX].trim());
        return pausedQuestion;
    }

    /**
     * Create a temporary result for the paused attempt.
     *
     * @param topicResults User result within the current attempt.
     * @param line         The line read in from the save file.
     */
    private static void createTempResult(Results topicResults, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        int numberOfCorrectAnswers = Integer.parseInt(processedLine[INDEX_NUMBER_OF_CORRECT_ANSWERS].trim());
        topicResults.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        int totalNumberOfQuestions = Integer.parseInt(processedLine[INDEX_TOTAL_NUMBER_OF_QUESTIONS].trim());
        topicResults.setTotalNumberOfQuestions(totalNumberOfQuestions);
    }

    /**
     * Create a temporary list of user answers for the paused attempt.
     *
     * @param userAnswers User answers within the current attempt.
     * @param line        The line read in from the save file.
     */
    private static void createTempAnswers(ArrayList<String> userAnswers, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        for (String answer : processedLine) {
            answer = answer.trim();
            userAnswers.add(answer);
        }
    }

    /**
     * Create a temporary list of user answer correctness for the paused attempt.
     *
     * @param correctness User answer correctness within the current attempt.
     * @param line        The line read in from the save file.
     */
    private static void createTempCorrectness(ArrayList<Boolean> correctness, String line) {
        String[] processedLine = line.split(ARG_SEPARATOR);
        for (String answerCorrectness : processedLine) {
            boolean isCorrect = Boolean.parseBoolean(answerCorrectness.trim());
            correctness.add(isCorrect);
        }
    }

    /**
     * Create the user result loaded in from the save file.
     *
     * @param result User result from a previous attempt.
     * @return User results with the necessary information.
     */
    private static Results createResults(String result) {
        String[] processedResult = result.split(RESULTS_SEPARATOR);
        Results temp = new Results();
        int numberOfCorrectAnswers = Integer.parseInt(processedResult[INDEX_NUMBER_OF_CORRECT_ANSWERS].trim());
        temp.setNumberOfCorrectAnswers(numberOfCorrectAnswers);
        int totalNumberOfQuestions = Integer.parseInt(processedResult[INDEX_TOTAL_NUMBER_OF_QUESTIONS].trim());
        temp.setTotalNumberOfQuestions(totalNumberOfQuestions);
        String score = processedResult[INDEX_SCORE].trim();
        temp.setScore(score);
        return temp;
    }

    /**
     * Initialises the save file.
     *
     * @param file The save file.
     * @throws CustomException If there was an error writing to the save file.
     */
    public void initSaveFile(File file) throws CustomException {
        try {
            new File(FOLDER_NAME).mkdir();
            file.createNewFile();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_INIT);
        }
    }

    /**
     * Saves the game data.
     *
     * @param results List of all results.
     * @param topics  List of all topics.
     * @param answers List of all user answers to questions.
     * @throws CustomException If there was an error writing to the save file.
     */
    public void saveProgress(ResultsList results, TopicList topics, AnswerTracker answers)
            throws CustomException {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            writeToFile(results, topics, answers, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_WRITING);
        }
    }

    /**
     * Saves the game data when the user is exiting from a paused game.
     *
     * @param results            List of all results.
     * @param topics             List of all topics.
     * @param answers            List of all user answers to questions.
     * @param allAnswers         User answers within the current attempt.
     * @param answersCorrectness User answer correctness within the current attempt.
     * @param topicResults       User results within the current attempt.
     * @param topicNum           The topic number.
     * @param index              The question number.
     * @throws CustomException If there was an error writing to the save file.
     */
    public void pauseGame(ResultsList results, TopicList topics, AnswerTracker answers, ArrayList<String> allAnswers,
                          ArrayList<Boolean> answersCorrectness, Results topicResults, int topicNum, int index)
            throws CustomException {
        try {
            FileWriter fileWriter = new FileWriter(FILE_PATH);
            savePoint(allAnswers, answersCorrectness, topicResults, fileWriter, topicNum, index);
            writeToFile(results, topics, answers, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new CustomException(MESSAGE_ERROR_WRITING);
        }
    }

    /**
     * Writes to the file a save point in the current attempt.
     *
     * @param allAnswers         User answers within the current attempt.
     * @param answersCorrectness User answers correctness within the current attempt.
     * @param topicResults       User results within the current attempt.
     * @param fileWriter         File writer to write to the save file.
     * @param topicNum           The topic number.
     * @param index              The question number.
     * @throws IOException If there was an error writing to the save file.
     */
    private static void savePoint(ArrayList<String> allAnswers, ArrayList<Boolean> answersCorrectness,
                                  Results topicResults, FileWriter fileWriter, int topicNum, int index)
            throws IOException {

        fileWriter.write(SAVE_PAUSE_HEADER + topicNum + SAVE_ARG_SEPARATOR + index + System.lineSeparator());

        StringBuilder listOfAnswers = new StringBuilder();
        StringBuilder listOfCorrectness = new StringBuilder();

        if (!allAnswers.isEmpty()) {
            listOfAnswers.append(allAnswers.get(FIRST_ANSWER));
            listOfCorrectness.append(answersCorrectness.get(FIRST_ANSWER));
        }

        for (int i = 1; i < allAnswers.size(); i++) {
            String answer = allAnswers.get(i);
            boolean correctness = answersCorrectness.get(i);
            listOfAnswers.append(SAVE_ARG_SEPARATOR).append(answer);
            listOfCorrectness.append(SAVE_ARG_SEPARATOR).append(correctness);
        }

        int numberOfCorrectAnswers = topicResults.getNumberOfCorrectAnswers();
        int totalNumberOfQuestions = topicResults.getTotalNumberOfQuestions();

        fileWriter.write(TEMP_RESULT + numberOfCorrectAnswers + SAVE_ARG_SEPARATOR + totalNumberOfQuestions
                + System.lineSeparator());
        fileWriter.write(TEMP_ANSWER + listOfAnswers + System.lineSeparator());
        fileWriter.write(TEMP_CORRECTNESS + listOfCorrectness + System.lineSeparator());
    }

    /**
     * Writes to the save file current game data.
     *
     * @param results    List of all results.
     * @param topics     List of all topics.
     * @param answers    List of all user answers to questions.
     * @param fileWriter File writer to write to the save file.
     * @throws IOException If there was an error writing to the save file.
     */
    private static void writeToFile(ResultsList results, TopicList topics, AnswerTracker answers,
                                    FileWriter fileWriter)
            throws IOException {

        ArrayList<Results> resultList = results.getSessionResults();
        ArrayList<Integer> topicsChosen = results.getTopicsChosen();
        for (int i = 0; i < resultList.size(); i++) {
            Results result = resultList.get(i);
            int numberOfCorrectAnswers = result.getNumberOfCorrectAnswers();
            int totalNumberOfQuestions = result.getTotalNumberOfQuestions();
            String score = result.getScore();
            int topicNum = topicsChosen.get(i);
            fileWriter.write(SAVE_RESULT_HEADER + numberOfCorrectAnswers + SAVE_RESULTS_SEPARATOR
                    + totalNumberOfQuestions + SAVE_RESULTS_SEPARATOR + score + SAVE_ARG_SEPARATOR + topicNum
                    + System.lineSeparator());
        }

        ArrayList<Topic> topicList = topics.getTopicList();
        for (Topic topic : topicList) {
            if (topic.hasAttempted()) {
                fileWriter.write(SAVE_TOPIC_HEADER + topic.getTopicName() + System.lineSeparator());
            }
        }

        ArrayList<ArrayList<String>> userAnswers = answers.getAllAnswers();
        ArrayList<ArrayList<Boolean>> isCorrect = answers.getAllCorrectness();
        for (int i = 0; i < userAnswers.size(); i++) {
            StringBuilder listOfAnswers = new StringBuilder();
            StringBuilder listOfCorrectness = new StringBuilder();

            listOfAnswers.append(userAnswers.get(i).get(FIRST_ANSWER));
            listOfCorrectness.append(isCorrect.get(i).get(FIRST_ANSWER));
            for (int j = 1; j < userAnswers.get(i).size(); j++) {
                String answer = userAnswers.get(i).get(j);
                boolean correctness = isCorrect.get(i).get(j);
                listOfAnswers.append(SAVE_ARG_SEPARATOR).append(answer);
                listOfCorrectness.append(SAVE_ARG_SEPARATOR).append(correctness);
            }
            fileWriter.write(SAVE_ANSWER_HEADER + listOfAnswers + System.lineSeparator());
            fileWriter.write(SAVE_CORRECTNESS_HEADER + listOfCorrectness + System.lineSeparator());
        }
    }

    //@@author ngxzs
    // creates question list1,2,3
    public void updateQuestionList(int questionListIndex, QuestionsList questionList) throws CustomException {
        int questionListNum = questionListIndex + 1; // +1 coz 0 index
        switch (questionListNum) {
        case (1):   // creates questionsList1 for topic1
            updateQuestionList1(questionList);
            break;
        case (2):   // creates questionsList2 for topic2
            updateQuestionList2(questionList);
            break;
        case (3):   // creates questionsList3 for topic3
            updateQuestionList3(questionList);
            break;
        default:
            throw new CustomException("unable to create questionList" + questionListNum);
        }
    }

    // Adds questions for questionsList1 "Software Engineering Concepts I"
    private void updateQuestionList1(QuestionsList questionsList) {
        Question question1 = new Question(
                "What language does CS2113 use?",
                "a",
                "CS2113 teaches OOP in Java",
                new String[]{"Java",
                             "C++",
                             "C",
                             "Python"});

        Question question2 = new Question(
                "As per the textbook, brown-field projects are usually harder than green-field projects.",
                "a",
                "Brown-field projects refers to a product to replace/ update an existing software, " +
                        "while green-field projects refers to a totally new system from scratch",
                new String[]{"True",
                             "False",
                             "Only got grass fields",
                             "There is no textbook"});

        Question question3 = new Question(
                "What is NFR?",
                "c",
                "Requirements can be divided as functional and non-functional requirements",
                new String[]{"No-such-thing, For Real",
                             "Nets For Rent",
                             "Non-Functional Requirements",
                             "NUS Forest Registry"});

        Question question4 = new Question(
                "What is an example of a NFR?",
                "c",
                "Non-functional requirements (NFR) specify the constraints " +
                        "under which the system is developed and operated",
                new String[]{"Your imagination",
                             "Fishing nets",
                             "should work on 32 and 64 bit systems",
                             "Recycle bins"});

        Question question5 = new Question(
                "Requirements should be as close to implementation as possible, " +
                        "so as to minimize errors in implementing it",
                "c",
                "In contrast, requirements should be implementation-free.",
                new String[]{"True",
                             "True sometimes",
                             "False",
                             "I give up!"});

        Question question6 = new Question(
                "Ideally, a requirement should not be divisible any further",
                "d",
                "True, textbook uses the term 'atomic' to describe this quality",
                new String[]{"False: a requirement is not a number",
                             "NA: not possible in real life",
                             "Idk",
                             "True"});

        Question question7 = new Question(
                "Which of the following is WRONG?",
                "a",
                "A private method cannot be a part of the API as it is not accessible by other classes.",
                new String[]{"Any method of a class is part of its API",
                             "API forms the contract between the component developer and the component user",
                             "A software component can have an API",
                             "Private methods of a class are not part of its API"});

        Question question8 = new Question(
                "What is substitutability in OOP?",
                "d",
                "Inheritance allows for substitutability: the ability to substitute a child class object where a " +
                        "parent class object is expected",
                new String[]{"We can substitute int for Integer",
                             "We can substitute a superclass object where a subclass object is expected",
                             "Similar to how we SU our grades, we can substitute a char as a String with length 1",
                             "Every instance of a subclass is an instance of the superclass, but not vice-versa"});

        Question question9 = new Question(
                "What are the 2 aspects of encapsulation in OOP?",
                "c",
                "packaging aspect refers to packaging data and related behaviour together in 1 self-contained unit.\n" +
                        "The information hiding aspect refers to an object, hidden from the outside world and only " +
                        "accessible from the object's interface",
                new String[]{"manufacturing aspect and packaging aspect",
                             "abstracting aspect and packaging aspect",
                             "packaging aspect and information hiding aspect",
                             "abstracting aspect and substitution aspect"});

        Question question10 = new Question(
                "What is abstraction in OOP?",
                "b",
                "Abstraction is a technique for dealing with complexity.\n" +
                        "It works by establishing a level of complexity we are interested in, and suppressing the " +
                        "more complex details below that level.",
                new String[]{"If you can't convince, confuse.",
                             "Hide the lower level details, work with the bigger granularity entities.",
                             "SLAP your code such that you are seeing it in the big picture view",
                             "Make the class private"});

        questionsList.addQuestion(question1);
        questionsList.addQuestion(question2);
        questionsList.addQuestion(question3);
        questionsList.addQuestion(question4);
        questionsList.addQuestion(question5);
        questionsList.addQuestion(question6);
        questionsList.addQuestion(question7);
        questionsList.addQuestion(question8);
        questionsList.addQuestion(question9);
        questionsList.addQuestion(question10);
    }

    // Adds questions for questionList2 "Software Engineering Concepts II"
    private void updateQuestionList2(QuestionsList questionsList) {
        Question question1 = new Question(
                "One should never prioritize efficiency or performance over readability",
                "a",
                "From textbook: there are cases when optimizing takes priority over other things",
                new String[]{"False: not always the case",
                             "False: efficiency == readability",
                             "True: readability is always more important",
                             "True: the compiler will optimize for you"});

        Question question2 = new Question(
                "As per the KISS principle, one should always prefer the simpler solution over clever ones",
                "b",
                "False: not always. Rather, one should not discard the simple solutions just because there " +
                        "is a more 'clever' solution. Instead, 'clever' solution should only be chosen only if " +
                        " the additional cost of complexity is justifiable",
                new String[]{"True: simple == elegant",
                             "False: sometimes we want the clever solutions",
                             "Both are irrelevant to KISS",
                             "I give up!"});

        Question question3 = new Question(
                "Which of the following follows the correct coding standard?",
                "d",
                "Constants should be named using ALL_CAPS_LIKE_THIS",
                new String[]{"int my_var = 2;",
                             "int my_var = 2;",
                             "int MyVar = 2;",
                             "final static int MY_VAR = 2;"});

        Question question4 = new Question(
                "Constants often follow this naming convention: ALL_CAPS_LIKE_THIS. What is it called?",
                "a",
                "Constants should be named using ALL_CAPS_LIKE_THIS",
                new String[]{"SCREAMING_SNAKE_CASE",
                             "camelCase1",
                             "PascalCase",
                             "Train-Case"});

        Question question5 = new Question(
                "What is a programming paradigm?",
                "c",
                "For example, OOP is a programming paradigm ie a way of analyzing the code.",
                new String[]{"it separates code into 2 parts: data and operations on data",
                             "it is laying out all the data structures in the programme",
                             "it guides users to analyze programming problems and structure programming solutions in " +
                                     "a specific way, like OOP",
                             "it is how you work with others in a software engineer project setting"});

        Question question6 = new Question(
                "Which is NOT a programming paradigm listed in the textbook?",
                "b",
                "Hardware Description Language is a programming language, not a paradigm",
                new String[]{"Procedural Programming paradigm (C)",
                             "Hardware Description Language paradigm (Verilog)",
                             "Functional Programming paradigm (F#, Haskell, Scala)",
                             "Logic Programming Paradigm (Prolog)"});

        Question question7 = new Question(
                "Which of the following is FALSE?",
                "c",
                "While many languages support the OO paradigm, OO is not a language itself.",
                new String[]{"OO is a programming paradigm",
                             "OO is mainly an abstraction mechanism",
                             "OO is a programming language",
                             "OO guides us in how to structure the solution"});

        Question question8 = new Question(
                "Which of the following is FALSE?",
                "c",
                "C follows the procedural paradigm. Yes, we can also write procedural code using OO language.",
                new String[]{"Java and C++ are OO languages",
                             "Java can be used to write procedural code",
                             "C language follows the Functional Programming paradigm",
                             "Prolog follows the Logic Programming paradigm"});

        Question question9 = new Question(
                "What does SLAP stands for?",
                "d",
                "See the course website!",
                new String[]{"when you feel pain on your face",
                             "when another person feels pain on their face",
                             "Studying-lit Acceleration Program",
                             "Single Level of Abstraction Per method"});

        Question question10 = new Question(
                "What is polymorphism in OOP?",
                "b",
                "Recall the lecture example Cat.speak() v Dog.speak().",
                new String[]{"Method overloading as the same function name returns different results based on the " +
                                     "type sequence of the parameters",
                             "ability to write code targeting superclasses objects, use that code on subclasses " +
                                     "objects, and achieve possibly different results based on the actual class of " +
                                     "the object",
                             "Hide all non-relevant details and only display the relevant ones",
                             "A subclass inheriting from a superclass. In this case, the superclass is being reused"});

        questionsList.addQuestion(question1);
        questionsList.addQuestion(question2);
        questionsList.addQuestion(question3);
        questionsList.addQuestion(question4);
        questionsList.addQuestion(question5);
        questionsList.addQuestion(question6);
        questionsList.addQuestion(question7);
        questionsList.addQuestion(question8);
        questionsList.addQuestion(question9);
        questionsList.addQuestion(question10);
    }

    // "All about Java"
    private void updateQuestionList3(QuestionsList questionsList) {
        Question question1 = new Question(
                "Which command checks your current java version",
                "a",
                "Read the textbook/ java manual.",
                new String[]{"java -version",
                             "java --version",
                             "java -v",
                             "java -v"});

        Question question2 = new Question(
                "Which command compiles fileName.java?",
                "d",
                "Read the textbook/ java manual.",
                new String[]{"java -c fileName.java",
                             "java -compile fileName.java",
                             "java --compile fileName.java",
                             "javac fileName.java"});

        Question question3 = new Question(
                "Which commands runs the compiled java program after compiling fileName.java?",
                "d",
                "java fileName (without .java) is the correct way to run the program after compiling.",
                new String[]{"java --run fileName.java",
                             "fileName.java",
                             "java fileName.java",
                             "java fileName"});

        Question question4 = new Question(
                "Which of the following is not a primitive data type in Java?",
                "a",
                "While Strings are used often, it is not a primitive data type.",
                new String[]{"String",
                             "short",
                             "boolean",
                             "float"});

        Question question5 = new Question(
                "What is method overloading?",
                "b",
                "For example, me.speak(Word word) v me.speak(Number number) have the same number but different type " +
                        "signatures",
                new String[]{"Using a method repeatedly in more than 1 class",
                             "When multiple methods have the same name but different type signatures (parameters)",
                             "When you take 24MCs instead of the recommended 20MC workload",
                             "When multiple methods have the same name but in different classes"});

        Question question6 = new Question(
                "Which of the following is TRUE? (Here, int x = 2)",
                "a",
                "Type conversion must be explicit in strongly-typed languages.",
                new String[]{"Java is a strongly-typed language: long y = new Long(x) will pass",
                             "Java is a strongly-typed language: String must start with capital S",
                             "Java is a weakly-typed language: long y = x will pass.",
                             "Java is a weakly-typed language: int or Integer both works"});


        Question question7 = new Question(
                "Assume you have a String s = \"Hi\". How do we capitalize it?",
                "c",
                "Invoking the method toUpperCase() has no effect if you don't assign the return value to a variable.",
                new String[]{"s.toUpperCase()",
                             "s.toUpperCase",
                             "s = s.toUpperCase()",
                             "s = s.toUpperCase"});

        Question question8 = new Question(
                "How to compare 2 strings, string1 and string2, in Java?",
                "b",
                "The correct way is string1.equals(string2).",
                new String[]{"string1.compareTo(string2);",
                             "string1.equals(string2);",
                             "string1 = string2;",
                             "string1 == string2;"});

        Question question9 = new Question(
                "After calling new in Java, one does not need to call delete. Why?",
                "b",
                "In Java, the system automatically looks for stranded objects and deletes them.",
                new String[]{"Java has enough memory in the heap",
                             "Java has auto garbage collection",
                             "Memory leak does not exist in Java",
                             "As long as it compiles, one does not need to care."});

        Question question10 = new Question(
                "How do we specify class-level methods/ attributes in Java?",
                "c",
                "The keyword static is used to specify class-level entities.",
                new String[]{"public",
                             "new",
                             "static",
                             "final"});

        questionsList.addQuestion(question1);
        questionsList.addQuestion(question2);
        questionsList.addQuestion(question3);
        questionsList.addQuestion(question4);
        questionsList.addQuestion(question5);
        questionsList.addQuestion(question6);
        questionsList.addQuestion(question7);
        questionsList.addQuestion(question8);
        questionsList.addQuestion(question9);
        questionsList.addQuestion(question10);
    }
}
