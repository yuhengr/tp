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

> **IMPORTANT**
> The current release of Player2113 is a MVP preview version with various proposed functions unimplemented. Please strictly follow this user's guide.

## Features

> **NOTE:**
>
> - Words in UPPER_CASE are COMPULSORY parameters to be supplied by the user.
> - Items in square brackets are optional

    e.g. in `solution TOPIC_NUM [INDEX]`, `TOPIC_NUM` is a compulsory parameter like `solution 1 1`,

> while `INDEX` is an optional parameter like `solution 1`

1. [topic](#1-starting-game-topic)
2. [results](#2-viewing-results-results)
3. [solution](#3-get-solutions-for-a-question--topic-solution)
4. [explain](#4-get-explanations-for-a-question--topic-explain)
5. [timed mode](#5-timed-mode---attempt-the-questions-under-a-time-limit)
6. [list](#5-list---list-all-available-topics-and-their-summaries)
7. [help](#6-viewing-help-help)
8. [pause](#8-pausing-the-game-pause)
9. [resume](#9-resuming-the-game-resume)
10. [bye](#10-exiting-the-game-bye)

### 1. Starting game: `topic`

There are multiple question banks pre-configured in Player2113.
Start the game by selecting a topic from the menu.
After choosing a topic, questions will start appearing.

Format: `topic [TOPIC_INDEX]`

- Gameplay will include questions from the specified topic.
- After start, the system will display a question, and the user will have to key in a response.
- Press Enter to submit the response. System will then key in the next question. This will happen for 10 questions.

Example:
Input index of answer you want to respond with
Press enter to submit response
eg “a” to choose option "a. Java" (below)
![MCQ question sample](./team/img/mcqQuestionType.png)

### 2. Viewing results: `results`

You may check your answer accuracy after attempting a question set. The result is separated from practicing session for concentration consideration.

Format: `results [details] [INDEX]`

- Shows scores from the game round at that specified INDEX. The index must be a non-zero positive integer (e.g. 1, 2, 3, …).
- If `[INDEX]` is omitted, the results will be listed in chronological order of the rounds.
- Adding `[details]` will allow you to view the questions and your answers from the round. Omitting it will result in showing only the score.
- Anything after `[INDEX]` will be ignored.

Example usage: `results details 2`

### 3. Get solution(s) for a question / topic: `solution`

Shows solution(s) for a specific question or all questions of a topic
Only possible after attempting topic/ question

Format: `solution TOPIC_NUM [INDEX]`

- The `INDEX` must be a non-zero positive integer (e.g. 1, 2, 3, …).
- If `INDEX` is omitted, command will show all solutions for specified TOPIC_NUM

Examples:
` solution 1` shows the solutions for topic 1
` solution 1 1` shows the solution for topic 1 question 1
`solution ` will be ignored

### 4. Get explanation(s) for a question / topic: `explain`

Shows explanation(s) for a specific question or all questions of a topic
Only possible after attempting topic/ question

Format: `explain TOPIC_NUM [INDEX]`

- The `INDEX` must be a non-zero positive integer (e.g. 1, 2, 3, …).
- If `INDEX` is omitted, command will show all explanations for specified TOPIC_NUM

Examples:
` explain 1` shows all explanations for topic 1
` explain 1 1` shows the explanation for topic 1 question 1
`explain ` will be ignored

### 5. Attempt the questions under a time limit: `timed mode`

Format: `timed mode [TIME_LIMIT]`

- `TIME_LIMIT` represents the time limit set for the question set in seconds
- The`TIME_LIMIT` must be a non-zero positive integer (e.g. 1, 2, 3, …).

### 6. List all available topics and their summaries: `list`

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

### 7. Viewing help: `help`

Displays commands for the application (start game, choose topic, revise, clear/reset progress). Shows developer credits information.
Usage: type “help” in the main menu.
Sample output:

![Help Sample Output](./docs/team/img/ug_usage_help.png)

### 8. Pausing the game: `pause`

Pauses the game so that the user may choose to exit the game in the middle of answering the questions.

Format: `pause`

- This command is valid only when the user is answering the topic questions.

### 9. Resuming the game: `resume`

Resumes the game when the game is paused.

Format: `resume`

- This command is valid only when the game is paused.

### 10. Exiting the game: `bye`

All results will be saved to a local save file, which will be loaded up when the application is opened next time.

- If exited after pausing the game, you can choose whether to continue from where you left off or
  discard the results for that topic.

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

- View help `help`
- Start game `topic TOPIC_NUM` e.g. `topic 1`
- Pause game `pause`
- Resume game `resume`
- Show Results `results [details] [ATTEMPT_INDEX]` e.g. `results details 2`
- Show Solution `solution TOPIC_NUM [QUESTION_NUM]` e.g. `solution 1 1`
- Show Explanation `explain TOPIC_NUM [QUESTION_NUM]` e.g. `explain 1 1`
- Timed Mode `timed mode [TIME_LIMIT]` e.g. `timed mode 5`
- Exit `bye`
