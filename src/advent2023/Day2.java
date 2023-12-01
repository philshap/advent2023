package advent2023;

import java.util.List;

public class Day2 implements Day {
    public void run(Support support) throws Exception {
        var input = support.readLines(1);
        part1(input);
        part2(input);
    }
    private void part1(List<String> input) {
        System.out.printf("day 2 part 1: %s%n", input.size());
    }
    private void part2(List<String> input) {
        System.out.printf("day 2 part 2: %s%n", input.size());
    }
}
