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

Please refer to [User Guide](./docs/UserGuide.md) for complete usage instructions.

## Command Summary

- help `help`
- Start game `topic TOPIC_NUM` eg `topic 1`
- Show Results `results [details] [TOPIC_NUM]` eg results details 2
- Show Solution `solution TOPIC_NUM [QUESTION_NUM]` eg solution 1 1
- Show Explanation `explain TOPIC_NUM [QUESTION_NUM]` eg explain 1 1
- Exit `bye`
