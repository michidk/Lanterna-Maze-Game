# Computer Science Programming Project (First Semester)

The task was to create a top-down maze game using [Google's Lanterna library](https://code.google.com/archive/p/lanterna/) in Java.
The basic gameplay features were prescribed.

Additionally I used [Lombok](https://projectlombok.org/) and [Maven](https://maven.apache.org/).

Notes:
- to test health-packs, load the level_small_healthpack-level
- the enemys are moving randomly util they reached a distance of 15 units to the player. If the distance is equal or lower to 15 the enemy will use pathfinding ([A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm)) to follow the player.
- if you are experiencing lags, try to turn down the AStar.MAX_SEARCH_DEPTH value

Features beside the minimum requirements:
- extensible and highly compatible math api (position, bounds, size, direction usw) [all of them compatible with the lanterna ones]
- object orientated entity system
- advanced game-loop with target framerate, deltatime etc.
- [A* pathfinding](https://en.wikipedia.org/wiki/A*_search_algorithm)

Additional gameplay features:
- healthpacks
- sounds
- multiple keys

Sounds by kenney.nl (CC0)
