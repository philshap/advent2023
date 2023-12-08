package advent2023;

import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 implements Day {
    @Override
    public int number() {
        return 8;
    }

    record Exits(String left, String right) {
        String get(char turn) {
            return turn == 'L' ? left : right;
        }
    }

    static final Pattern NODE = Pattern.compile("[A-Z0-9]{3}");

    Map<String, Exits> parseMap(List<String> input) {
        return input.stream()
                    .map(line -> NODE.matcher(line).results().map(MatchResult::group).toList())
                    .collect(Collectors.toMap(List::getFirst,
                                              entry -> new Exits(entry.get(1), entry.get(2)), (a, b) -> b));
    }

    @Override
    public String part1(List<String> input) {
        String turns = input.getFirst();
        var map = parseMap(input.subList(2, input.size()));
        int step = 0;
        String node = "AAA";
        while (!node.equals("ZZZ")) {
            node = map.get(node).get(turns.charAt(step++ % turns.length()));
        }
        return String.valueOf(step);
    }

    static long GCF(long x1, long x2) {
        if (x2 == 0) {
            return x1;
        }
        return GCF(x2, x1 % x2);
    }

    static long LCM(long x1, long x2) {
        return (x1 * x2) / GCF(x1, x2);
    }

    int stepsToEnd(String node, String turns, Map<String, Exits> map) {
        int step = 0;
        while (!node.endsWith("Z")) {
            node = map.get(node).get(turns.charAt(step++ % turns.length()));
        }
        return step;
    }

    @Override
    public String part2(List<String> input) {
        String turns = input.getFirst();
        var map = parseMap(input.subList(2, input.size()));
        List<String> starts = map.keySet().stream().filter(node -> node.endsWith("A")).toList();
        List<Integer> steps = starts.stream().map(node -> stepsToEnd(node, turns, map)).toList();
        long lcm = 1;
        for (long step : steps) {
            lcm = LCM(lcm, step);
        }

        return String.valueOf(lcm);
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               LR
                                                                                              
                                               11A = (11B, XXX)
                                               11B = (XXX, 11Z)
                                               11Z = (11B, XXX)
                                               22A = (22B, XXX)
                                               22B = (22C, 22C)
                                               22C = (22Z, 22Z)
                                               22Z = (22B, 22B)
                                               XXX = (XXX, XXX)""");
        var day = new Day8();
        System.out.println(day.part2(input).equals("6"));
    }
}
