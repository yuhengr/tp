# Hong Yi Jie - Project Portfolio Page

## Project: Player2113

Player 2113 is a game application used for revising CS2113 concepts and strengthening understanding in the CS2113 module.
The user plays the game through a CLI. The application has about 3.6k lines of code and is written in java.

## Summary of Contributions
### Code Contributed
- [Link to tP Code Dashboard](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=hongyijie06&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2024-02-23&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&tabOpen=false)
### Enhancements Implemented
- **V1.0: Added the ability to attempt questions by topic**
    - **What it does:** Allows the user to attempt questions from specific topics they want to revise. 
      Users can also check if they have attempted the topic.
    - **Justification:** This feature allows users to get more targeted practice to those topics they are weaker in
      so that users can enjoy a more efficient study session.
    -  **Highlights:** There are many layers of classes that needed to be used in the design and implementation of this feature so it was easy to get confused with using the different classes and remember which methods belonged to which class.
       Applying concepts taught in the module like drawing class diagrams reduced the confusion while using the different classes.
- **V2.0: Added a timed mode**
    - **What it does:** Allows the user to set a time limit for the question set they want to attempt.
    - **Justification:** This feature allows users to work on their speed in answering the questions to gauge their familiarity with the concepts.
      The ultimate goal would be to simulate exam conditions to answer within the time limit. 
    - **Highlights:** The method to implement the timed mode involved usage of an inner class, in which the method in the timer task class cannot take in variables to update.
      It was challenging implementing the inner class as the implementation details are different from regular classes practiced in the module.
    - **Credits:** Watched a [YouTube tutorial](https://youtu.be/QEF62Fm81h4?si=GPR9-J0K1zdsS588) on how to use timer and timer task abstract classes in the Java library. 
- **Enhancement of Existing features: Integration using UI of pause feature when starting a new session** (e.g. [#84](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/84))
    - **What it does:** Users get to decide if they want to continue their previous session
    - **Justification:** Users do not feel so restricted with the freedom of choice to start a new session.
      They will also be warned that their incomplete game progress will be discarded if they decide to end the paused session before they confirm their decision.
      This allows users to better take charge of their learning to create a more flexible experience.
- **Enhancement of Existing features: access to solutions and explanations only after attempting** (e.g. [#44](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/44))
    - **Justification:** Allows the user to practice active recall to attempt the questions first rather than taking a passive approach to solely read the solutions and explanations in their revision.
### Contributions to the UG
- Added the `topic` feature and how to use
- Added the `timed mode` feature and how to use (e.g. [commit #362e139](https://github.com/hongyijie06/tp/commit/362e13952340b0a687aee3f469773c2880ed8495))
- Standardise presentation of the different command headers (e.g. [#85](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/85))
- Standardise presentation of `Command Summary` (e.g. [#85](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/85))
### Contributions to the DG
- Added documentation for the `Topics Feature` (e.g. [#59](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/59))
- Added class diagrams for the `Topics Feature` (e.g. [#59](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/59))
- Added `Non-functional Requirements`
### Contributions to Team-based Tasks
- Standardise format of features introduction and command summary (e.g. [commit #d67fa27](https://github.com/hongyijie06/tp/commit/d67fa275aa3822488c23fc723702170a95af2c8d))
- Added non-functional requirements in the DG
### Review/ Mentoring Contributions
- Testing of other commands and resolving minor bugs related to teammates code (e.g. [#157](https://github.com/AY2324S2-CS2113-F15-1/tp/pull/157))
### Contributions beyond the Project Team
- Testing other teams for bugs in their product and giving improvement suggestions (e.g. [#151](https://github.com/AY2324S2-CS2113-T13-4/tp/issues/151), [#158](https://github.com/AY2324S2-CS2113-T13-4/tp/issues/158), [#163](https://github.com/AY2324S2-CS2113-T13-4/tp/issues/163), [#173](https://github.com/AY2324S2-CS2113-T13-4/tp/issues/173), [#174](https://github.com/AY2324S2-CS2113-T13-4/tp/issues/174))
- Reviewing other teams DG and gave improvement suggestions (e.g. [#91](https://github.com/AY2324S2-CS2113-W14-1/tp/pull/91/files))