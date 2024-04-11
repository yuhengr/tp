# Ng Xin Zhe Sean - Project Portfolio Page

## Overview

Player2113 is a quiz themed CLI tool to help CS2113/T student revise Java OOP concepts in a gamification environment.
The user chooses a topic, answers the questions, and reviews the solutions all using the CLI.
The application has about 3.6k lines of code and is written in java.

### Summary of Contributions

* **New Feature:** Added the ability to view solutions
    * What it does: allows the user to view all solutions to a previously attempted topic or to view the solution to a
      previously attempted specific question
    * Justification: This feature improves the product significantly as this is part of the product's purpose: for
      users to view solutions to previously attempted questions for learning/ revision.
    * Highlights: Depending on the number of user inputs, this feature allows the user to either view all solutions to
      a topic or view 1 solution to a specific question. The implementation therefore needed additional parsing of user
      inputs to accurately differentiate between displaying all or just one solution.


* **New Feature:** Added the ability to view explanations
    * What it does: allows the user to view all explanations to a previously attempted topic or to view the explanation
      to a previously attempted specific question
    * Justification: This feature improves the product significantly as this extends the usefulness of the product: for
      users to view the explanations to solutions if they do not understand them for learning/ revision.
    * Highlights: Depending on the number of user inputs, this feature allows the user to either view all explanations
      to a
      topic or view 1 explanation to a specific question. The implementation therefore needed additional parsing of user
      inputs to accurately differentiate between displaying all or just one explanation.


* **Feature Enhancement:** Added the ability to choose a random topic (Pull Request #54)
    * What it does: allows the user to choose a random topic amongst the list of topics displayed
    * Justification: This feature improves the product's gamification aspect, as a randomizer to choose topics can help
      add
      variation and challenge to the user experience when using this product.
    * Highlights: This enhancement closely works with the controller responsible for choosing which topic to display
      questions from. Due to the nature of java's Random library, additional checks have to be made to ensure random
      number
      generated is not invalid like 0 or an out-of-bounds index.


* **Code Contributed:** Check it out at my RepoSense
  Link [here](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=ngxzs&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2024-02-23&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)


* **Enhancements to existing features:**
  * Added questions dataset for product to look more like a game (Pull requests #149)
  * Made GUI more user-friendly with line breaks (#40)
  * Added Parser class testing to increase test coverage from % to % (#75)
  * Added scaffolding for main class code (#21)

<div style="page-break-after: always;"></div>

* **UG Contributions:**
  * Added documentation for features `solution` and `explain` (#57)
  * Added links, headings, and standardized format structure for UG (#69)


* **DG Contributions:**
  * Added implementation details for `solution` and `explain` (#58)


* **Team-based Tasks Contributions:** Added questions in database
  * PR reviewed (to resolve GitHub checks errors): (Issue #26)
  * Enabled assertions, modified setup in gradle build (Pull Request #21)
  * Code Refactoring (Pull Request #86, #67)
