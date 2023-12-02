package advent2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 implements Day {
    public void run(Support support) throws Exception {
        var input = support.readLines(2);
        part1(input);
        part2(input);
    }

    static final String RED = "red";
    static final String GREEN = "green";
    static final String BLUE = "blue";
    record Turn(Map<String, Integer> colors) {
        static Pattern PATTERN = Pattern.compile("(\\d+) (red|green|blue)");

        static Turn fromLine(String line) {
            Map<String, Integer> colors = new HashMap<>();
            Matcher matcher = PATTERN.matcher(line);
            while (matcher.find()) {
                colors.put(matcher.group(2), Integer.parseInt(matcher.group(1)));
            }
            return new Turn(colors);
        }

        public boolean valid(Map<String, Integer> max) {
            return colors.keySet().stream().allMatch(color -> colors.get(color) <= max.get(color));
        }
    }

    record Game(int id, List<Turn> turns) {
        static Pattern PATTERN = Pattern.compile("Game (\\d+): (.*)");

        static Game fromLine(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                return new Game(Integer.parseInt(matcher.group(1)),
                        Arrays.stream(matcher.group(2).split("; "))
                                .map(Turn::fromLine).toList());
            }
            return new Game(0, List.of());
        }

        // Part 2
        public int getPower() {
            Map<String, Integer> min = new HashMap<>();
            for (Turn turn : turns) {
                turn.colors.keySet().forEach(color -> min.merge(color, turn.colors.get(color), Math::max));
            }
            return min.values().stream().reduce(1, (i, j) -> i * j);
        }
    }

    private void part1(List<String> input) {
        Map<String, Integer> max = Map.of(RED, 12, GREEN, 13, BLUE, 14);
        int sum = input.stream().map(Game::fromLine)
                .filter(game -> game.turns.stream().allMatch(turn -> turn.valid(max)))
                .mapToInt(Game::id).sum();
        System.out.printf("day 2 part 1: %s%n", sum);
    }

    private void part2(List<String> input) {
        int sum = input.stream().map(Game::fromLine).mapToInt(Game::getPower).sum();
        System.out.printf("day 2 part 2: %s%n", sum);
    }

    public static void main(String[] args) throws Exception {
        Day day = new Day2();
        day.run(new Support() {
            @Override
            public List<String> readLines(int day) {
                return List.of("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                        "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                        "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                        "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                        "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green");
            }
        });
    }
}
