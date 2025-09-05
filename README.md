# 🚀 SpaceX Repository

This project is a simple implementation of a repository for managing rockets and missions.

## Development Process

1. **Repository Design**
    - Started by designing the repository model.

2. **Implementation**
    - Created `DragonRocketRepository` with the required methods.
    - Developed `DragonRocketRepositoryInMemory` using a `HashMap` to store objects.
        - Keys: Rocket/Mission names.
        - Reason: Enables efficient lookups compared to iterating through a list or set.

3. **Test-Driven Development (TDD)**
    - Wrote tests first, following the TDD paradigm.
    - Used an LLM to help generate initial tests based on the requirements, then reviewed and refined them.
    - Discovered a subtle trick in the test generation process: an additional hidden status intended to catch developers who rely solely on LLM output without critical review.

4. **Further Implementation**
    - Implemented all required repository methods.
    - Left `toString` and `getMissionSummary` for last.
    - Used an LLM to draft those methods, then manually corrected and refined them.

5. **Documentation**
    - Finally, wrote this README and improved its formatting with LLM assistance.

## LLM Usage Summary

- ✅ Generating initial **tests**
- ✅ Drafting **assignRocketsToMission**
- ✅ Assisting with **README formatting**  