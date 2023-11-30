package advent2023;

public class Day1 implements Day {

    public void run(Support support) throws Exception {
        var input = support.readString(1);
        part1(input);
        part2(input);
    }

    private static void part1(String input) {
        System.out.printf("day 1 part 1: %s%n", input.length());
    }

    private static void part2(String input) {
        System.out.printf("day 1 part 2: %s%n", input.length());
    }
}
