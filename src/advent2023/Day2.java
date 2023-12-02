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

    @Override
    public int number() {
        return 2;
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

        private Map<String, Integer> maxColors() {
            return turns.stream().flatMap(turn -> turn.cubes.entrySet().stream())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::max));
        }

        static Map<String, Integer> MAX = Map.of("red", 12, "green", 13, "blue", 14);
        // Part 1
        boolean valid() {
            return maxColors().entrySet().stream().allMatch(cube -> cube.getValue() <= MAX.get(cube.getKey()));
        }

        // Part 2
        int getPower() {
            return maxColors().values().stream().reduce(1, (i, j) -> i * j);
        }
    }

    public String part1(List<String> input) {
        int sum = input.stream().map(Game::fromLine).filter(Game::valid).mapToInt(Game::id).sum();
        return String.valueOf(sum);
    }

    @Override
    public String part2(List<String> input) {
        int sum = input.stream().map(Game::fromLine).mapToInt(Game::getPower).sum();
        return String.valueOf(sum);
    }

    public static void main(String[] args) {
        Day day = new Day2();
        List<String> input = List.of("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green");
        System.out.println(day.part1(input));
        System.out.println(day.part2(input));
    }
}
