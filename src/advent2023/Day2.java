package advent2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day2 implements Day {
    public void run(Support support) throws Exception {
        var input = support.readLines(2);
        part1(input);
        part2(input);
    }

    record Turn(Map<String, Integer> cubes) {
        static Pattern PATTERN = Pattern.compile("(\\d+) (red|green|blue)");

        static Turn fromLine(String line) {
            Map<String, Integer> cubes = new HashMap<>();
            Matcher matcher = PATTERN.matcher(line);
            while (matcher.find()) {
                cubes.put(matcher.group(2), Integer.parseInt(matcher.group(1)));
            }
            return new Turn(cubes);
        }

        static Map<String, Integer> MAX = Map.of("red", 12, "green", 13, "blue", 14);

        public boolean valid() {
            return cubes.entrySet().stream().allMatch(cube -> cube.getValue() <= MAX.get(cube.getKey()));
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

        // Part 1
        boolean valid() {
            return turns.stream().allMatch(Turn::valid);
        }

        // Part 2
        int getPower() {
            return turns.stream().flatMap(turn -> turn.cubes.entrySet().stream())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max))
                    .values().stream().reduce(1, (i, j) -> i * j);
        }
    }

    private void part1(List<String> input) {
        int sum = input.stream().map(Game::fromLine).filter(Game::valid).mapToInt(Game::id).sum();
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
