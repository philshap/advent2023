package advent2023;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day2 implements Day {

    @Override
    public int number() {
        return 2;
    }

    record Game(int id, Map<String, Integer> maxColor) {
        static Pattern GAME = Pattern.compile("Game (\\d+): (.*)");
        static Pattern CUBES = Pattern.compile("(\\d+) (red|green|blue)");

        static Game fromLine(String line) {
            return GAME.matcher(line).results()
                    .map(game -> new Game(Integer.parseInt(game.group(1)),
                                          CUBES.matcher(game.group(2)).results()
                                                  .collect(Collectors.toMap(cubes -> cubes.group(2),
                                                                            cubes -> Integer.parseInt(cubes.group(1)),
                                                                            Math::max))))
                    .findFirst().orElseThrow();
        }

        static Map<String, Integer> MAX = Map.of("red", 12, "green", 13, "blue", 14);

        // Part 1
        boolean valid() {
            return maxColor.entrySet().stream().allMatch(entry -> entry.getValue() <= MAX.get(entry.getKey()));
        }

        // Part 2
        int getPower() {
            return maxColor.values().stream().reduce(1, (i, j) -> i * j);
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
