# SPACE X REPOSITORY

I started this task with designing the model for the repository.
Later on i created DragonRocketRepository and added appropriate methods meeting task requirements.
Then I created tests in order to meet TDD programming paradigm, for this specific task I used LLM 
to help me with implementation - I asked him to implement the test based on requirements and then reviewed them correctly.
On this process I saw a catch You implemented with invisible additional status only for user, but visible
only for LLM, cause the text is with. Very tricky - that's a really nice way to sort out programmers who use LLM without
any additional thoughts. After the tests I started implementing the interface methods
in DragonRocketRepositoryInMemory. I chose HashMap to store the objects in memory and treated
Rocket/Mission name as a key - i did it in order to make retrieving objects more effective (if I
used a list I would have to iterate over every object in list/set until I meet name I want to retrieve).
I implemented all the methods besides from toString and getMissionSummary, then I tested them and corrected.
When I had all the required functionalities i used LLM to automate writing toString method and getMissionSummary,
which I still had to correct by myself. Finally wrote the README.md and used LLM to make it more appealing :)


LLM usage summation:
- tests
- assignRocketsToMission
- readme formatting :)