# Chan Yu Heng, Jason - Project Portfolio Page

## Overview

Player2113 is a desktop game application for CS2113/T students
to learn and revise OOP concepts through a gamified approach.
The user interacts with it using a CLI. It is written in Java, 
and has about 3.6 kLoC.

### Summary of Contributions

* **Code Contributed**: [Reposense Link](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=cyhjason29&breakdown=true)


* **New Feature**: Added the ability to view results with details.
  * What it does: It allows the users to view their results from
previous attempts, and gives them the option to view details such
as the questions answered as well as their answers to each of them.
  * Justification: This feature improves the product significantly
because a user should be able to track their scores and see which
type of questions they tend to make mistakes to aid in their
learning journey.
  * Highlights: This implementation 
* **New Feature**: Added the ability to save user data.
  * What it does: It creates a local save file in the user's
machine that saves all results with their respective details,
as well as the topics that have been attempted already, which 
is then automatically loaded up the next time the user opens 
the application. 
  * Justification: This feature improves the product significantly
because a user should be able to keep track of all their progress
from previous sessions, so that the work that they have put in
is not lost, allowing for better awareness of their learning.
  * Highlights: This enhancement required extensive management of
all the game data that needed to be saved as there were many things
to track and record. Thus, the conversion process (reading from and 
writing to the file) was challenging and required much meticulousness.
* **New Feature**: Added the ability to pause and resume.
  * What it does: It allows the users to pause in the middle of
attempting the questions, so that they may choose to exit the
application halfway.
  * Justification: This feature improves the product significantly
because the users, which are targeted to be students, can be busy
and should be given the option to leave mid-game. 
  * Highlights: This enhancement required an in-depth understanding
of the game flow as many conditions have to be implemented to execute
this feature, which can result in many bugs. Thus, much work needs to be put in order to ensure that
the game flow is not disrupted.


* **Enhancements to Existing Features**:
  * Changed Question class to MCQ style for a more complete product
  * Added answer format checks (must be a, b, c or d).


* **UG Contributions**:
  * Added documentation for the features `results`, `pause`, and `resume`.
  * Added more detailed notes on how to interpret upper case and lower case parameters.
  * Aesthetic fixes.


* **DG Contributions**:
  * Added implementation details for the features `results` and `Storage` for saving.
  * Added sequence diagrams to `results` feature
  * Added class diagram for `Storage`


* **Team-based Tasks Contributions**:
  * Set up GitHub issues and milestones with labels
  * Added v1 user stories as issues


* **Review/Mentoring Contributions**:
  * Checked and fixed Gradle violations in team members' code ([#26](https://github.com/AY2324S2-CS2113-F15-1/tp/issues/26))


* **Contributions beyond the Project Team**:
  * Gave suggestions on a DG from another team ([#63](https://github.com/nus-cs2113-AY2324S2/tp/pull/63))
  * Tested another team's product for bugs ([#118](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/118), [#120](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/120), [#121](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/121), [#123](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/123), [#126](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/126), [#129](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/129), [#131](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/131), [#133](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/133), [#134](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/134), [#137](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/137), [#138](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/138), [#139](https://github.com/AY2324S2-CS2113-T11-3/tp/issues/139))