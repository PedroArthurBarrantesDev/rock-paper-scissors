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

Then open http://localhost:8080 and click Rock, Paper or Scissors to play. On Windows use `mvnw.cmd` instead of `./mvnw`.

## REST API

The game is played through three JSON endpoints. Each browser gets its own match, tracked by a session cookie.

| Method | Path | What it does | Success | Errors |
| --- | --- | --- | --- | --- |
| GET | `/api/match` | Current score, and the result once the match is over | 200 | |
| POST | `/api/match/rounds` | Plays one round, body `{"choice":"ROCK"}` | 201 | 400 bad or missing choice, 409 match already over |
| POST | `/api/match` | Starts a new match | 201 | |

Try it from a terminal while the app runs (`cookies.txt` keeps your session between commands):

```bash
curl -i -c cookies.txt -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{"choice":"ROCK"}' \
  http://localhost:8080/api/match/rounds

curl -b cookies.txt http://localhost:8080/api/match

curl -i -X POST -b cookies.txt http://localhost:8080/api/match
```

## Test it

```bash
./mvnw verify
```

GitHub Actions runs the same command on every push and pull request.

## Project layout

```
src/main/java/com/example/rps/      Spring Boot app and game code
src/main/resources/templates/       Thymeleaf page
src/main/resources/static/          CSS, JavaScript (game.js) and card images (img/)
src/test/java/com/example/rps/      Tests
```

## Card images

The cards use simple placeholder drawings in `src/main/resources/static/img/` (`rock.svg`, `paper.svg`, `scissors.svg`). To use your own illustrations, replace those files, or add PNGs and change the three `<img>` paths in `templates/index.html`; the game script reads the paths from there.

## Roadmap

0. Project setup and CI
1. Game logic: choices, rules, random computer player
2. REST API with best-of-5 match state
3. Clickable UI (first playable version)
4. Polish: custom card illustrations, animations, round history
5. Extras (optional): Docker, leaderboard, smarter computer, Lizard Spock variant

## License

MIT, see [LICENSE](LICENSE).
