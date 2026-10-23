# Rock Paper Scissors

A visual Rock Paper Scissors game. You pick Rock, Paper or Scissors with the mouse, the computer picks at random, and the first to win 3 rounds takes the match (best of 5).

All game logic is written in Java. Spring Boot serves the game and a small web page in the browser is the screen you play on.

## Requirements

- Java 21
- Nothing else: the Maven wrapper (`mvnw`) downloads Maven on first use

## Run it

```bash
./mvnw spring-boot:run
```

Then open http://localhost:8080. On Windows use `mvnw.cmd` instead of `./mvnw`.

## Test it

```bash
./mvnw verify
```

GitHub Actions runs the same command on every push and pull request.

## Project layout

```
src/main/java/com/example/rps/      Spring Boot app and game code
src/main/resources/templates/       Thymeleaf page
src/main/resources/static/          CSS, JavaScript and images
src/test/java/com/example/rps/      Tests
```

## Roadmap

0. Project setup and CI
1. Game logic: choices, rules, random computer player
2. REST API with best-of-5 match state
3. Clickable UI (first playable version)
4. Polish: custom card illustrations, animations, round history
5. Extras (optional): Docker, leaderboard, smarter computer, Lizard Spock variant

## License

MIT, see [LICENSE](LICENSE).
