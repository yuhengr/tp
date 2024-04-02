# Player2113

A CLI tool to help COMP2113 student revise conceptual questions in a gamification environment.

## Getting Started

Prerequisites: JDK 11, Player2113 release `JDK` file

Start the programme with the following command:

```
java --jar Player2113.jar
```

You will see the welcome screen upon successful start-up:

```
Hello from
______ _                       _____  __   __   _____
| ___ \ |                     / __  \/  | /  | |____ |
| |_/ / | __ _ _   _  ___ _ __`' / /'`| | `| |     / /
|  __/| |/ _` | | | |/ _ \ '__| / /   | |  | |     \ \
| |   | | (_| | |_| |  __/ |  ./ /____| |__| |_.___/ /
\_|   |_|\__,_|\__, |\___|_|  \_____/\___/\___/\____/
                __/ |
               |___/
What is your name?
```
## Usage

> [!IMPORTANT]
> The current release of Player2113 is a MVP preview version with various proposed functions unimplemented. Please strictly follow this user's guide.

## Features
> **NOTE:**
> + Words in UPPER_CASE are the parameters to be supplied by the user.
    e.g. in `results INDEX`, `INDEX` is a compulsory parameter like `results 1`.
> + Items in square brackets are optional.
    e.g. `results [INDEX] [+questions]` can be used as `results +questions` or `results`.

- [topic](#starting-game-topic)
- [results](#viewing-results-results)
- [solution](#get-solutions-for-a-question--topic-solution)
- [explain](#get-explanations-for-a-question--topic-explain)
- [list](#list---list-all-available-topics-and-their-summaries)
- [help](#viewing-help-help)
- [bye](#exiting-the-game-bye)

### Starting game: `topic`
Start the game by selecting a topic from the menu. Afterwards, questions will start appearing.
There are multiple question banks pre-configured in Player2113.

Format: `topic [TOPIC_INDEX]`

- Gameplay will include questions from the specified topic.
- After start, the system will display a question, and the user will have to key in a response.
- Press Enter to submit the response. System will then key in the next question. This will happen for 10 questions.

Example:
Input index of answer you want to respond with
Press enter to submit response
eg “a” to choose option a. Java (below)
![MCQ question sample](docs/team/img/mcqQuestionType.png)

### Viewing results: `results`

You may check your answer accuracy after attempting a question set. The result is separated from practicing session for concentration consideration.
Format: `results [details] [INDEX]`

- Shows scores from the game round at that specified INDEX. The index must be a  non-zero positive integer (e.g. 1, 2, 3, …).
- If `[INDEX]` is omitted, the results will be listed in chronological order of the rounds.
- Adding `[details]` will allow you to view the questions and your answers from the round. Omitting it will result in showing only the score.
- Anything after `[INDEX]` will be ignored.

### Get solution(s) for a question / topic: `solution`
Shows solution(s) for a specific question or all questions of a topic
Only possible after attempting topic/ question

Format: `solution TOPIC_NUM [INDEX]`

- The `INDEX` must be a non-zero positive integer (e.g. 1, 2, 3, …).
- If `INDEX` is omitted, command will show all solutions for specified TOPIC_NUM

Examples:
` solution 1` shows the solutions for topic 1
` solution 1 1` shows the solution for topic 1 question 1
` solution  ` will be ignored

### Get explanation(s) for a question / topic: `explain`
Shows explanation(s) for a specific question or all questions of a topic
Only possible after attempting topic/ question

Format: `explain TOPIC_NUM [INDEX]`

- The `INDEX` must be a non-zero positive integer (e.g. 1, 2, 3, …).
- If `INDEX` is omitted, command will show all explanations for specified TOPIC_NUM

Examples:
` explain 1` shows all explanations for topic 1
` explain 1 1` shows the explanation for topic 1 question 1
` explain  ` will be ignored

### `list` - List all available topics and their summaries

Example of usage: `list`.

Sample output:

```
+-------+--------+-------------------------------------------------+-----------+
| index |  topic |                     summary                     | attempted |
+-------+--------+-------------------------------------------------+-----------+
|     1 | topic1 | Covers topic 1 notions mentioned in lecture 1-2 |     false |
|     2 | topic2 | Covers topic 2 notions mentioned in lecture 3-4 |     false |
+-------+--------+-------------------------------------------------+-----------+
```

### Viewing help: `help`
Displays commands for the application (start game, choose topic, revise, clear/reset progress). Shows developer credits information.
Usage: type “help” in the main menu.
Sample output:

![Help Sample Output](docs/team/img/ug_usage_help.png)

### Exiting the game `bye`

Example of usage: `bye`.

A goodbye message will be displayed:

```
bye bye, get more sleep zzz
************************************************
```

## FAQ

**Q**: how to do this user guide?

**A**: just do it. rmb update summary below.

## Command Summary

* help `help`
* Start game `topic TOPIC_NUM` eg `topic 1`
* Show Results `results [details] [TOPIC_NUM]` eg results details 2
* Show Solution `solution TOPIC_NUM [QUESTION_NUM]` eg solution 1 1
* Show Explanation `explain TOPIC_NUM [QUESTION_NUM]` eg explain 1 1
* Exit `bye`
