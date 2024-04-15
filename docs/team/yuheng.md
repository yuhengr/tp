# Ren Yuheng - Project Portfolio Page

## Overview
Player2113 is a CLI revision tool that allows the user to revise materials in CS2113. The user can choose a topic to revise and get instant feedback after their attempt. The application is written in Java and has approximately 3.6k lines of code.

### Summary of Contributions to Features
* **New Feature:** Customise the question set
    * What does it do exactly?
        * User can input the `custom TOPIC_NUM NUM_OF_QUESTIONS` command to get a subset of random questions from a particular topic.
    * Why the need?
        * This feature is almost like flash cards which the user can use to practise a particular topic with questions being displayed in random order to facilitate learning experience.

* **New Feature:** Add study checkpoints
    * What does it do exactly?
        * User can input the `checkpoint NUM_OF_QUESTIONS` command set a checkpoint goal for the current study session.
        * Upon reaching the goal, Player2113 will congratulate the user for reaching their target.
    * Why the need?
        * Revision can be tiring and setting small goals can help to make learning easier.

* **New Feature:** Clear progress
    * What does it do exactly?
        * User can input the `clear` command and their progress on Player2113 will be cleared.
    * Why the need?
        * The user can restart their progress on Player2113 and challenge themselves to the quizzes again.
        * More practice means they can do better in the actual exam.

* **Implementation Enhancement:** Modify Parser to better handle inputs
    * The HOW:
        * I used regular expressions to parse inputs.
    * The WHY:
        * It is able to capture inputs more accurately such that they fit a particular pre-determined format.
        * It also allows relevant parameters to be extracted more easily.

### Contributions to Developer Guide
* **Implementation of `custom` command**
    * Explanation of how the method handling the command works.
    * Drew the sequence diagram as an overview of the underlying method.

* **Implementation of `checkpoint` command**
    * Explanation of how the method handling the command works.
    * Drew the sequence diagram as an overview of the underlying method.

* **Implementation of `clear` command**
    * Explanation of how the method handling the command works.
    * Drew the sequence diagram as an overview of the underlying method.

### Contributions to User Guide
* **Documented the usage of the following commands:**
    * `custom`, `checkpoint` and `clear`

### Contributions to Bug Fixing
* **Fixed bugs found during the mock practical exam**
