package advent2023;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day12 implements Day {
    @Override
    public int number() {
        return 12;
    }

    // For pattern, find indexes of all ?s = missing[]
    // For sequence, split into numbers and find total number = T
    // Find number of dots to place by subtracting total sequence numbers from number of # = N
    // Then choose N values from missing[] and place in line
    // Create a regex from the sequence and filter against the built line.

    long solutionsForLine(String line) {
        var split = line.split(" ");
        var springs = split[0];
        var groups = Support.integers(split[1]);
        var groupTotal = groups.stream().mapToInt(i -> i).sum();
        var groupPattern = Pattern.compile(groups.stream()
                                                 .map("#{%s}"::formatted)
                                                 .collect(Collectors.joining("\\.+", "\\.*", "\\.*")));
        List<Integer> missing = new ArrayList<>();
        var template = new StringBuilder();
        var numFixed = 0;
        for (int i = 0; i < springs.length(); i++) {
            char c = springs.charAt(i);
            if (c == '?') {
                missing.add(i);
                c = '.';
            } else if (c == '#') {
                numFixed++;
            }
            template.append(c);
        }
        if (groupTotal - numFixed == 0) {
            return 1;
        }
        return Support.combinations(missing, groupTotal - numFixed)
                      .map(indexes -> generateData(template, indexes))
                      .filter(data -> groupPattern.matcher(data).matches())
                      .count();

    }

    private String generateData(StringBuilder template, List<Integer> indexes) {
        var data = new StringBuilder(template);
        for (var index : indexes) {
            data.setCharAt(index, '#');
        }
        return data.toString();
    }


    @Override
    public String part1(List<String> input) {
        long sum = input.stream().mapToLong(this::solutionsForLine).sum();
        return String.valueOf(sum);
    }

    @Override
    public String part2(List<String> input) {
        return "??";
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               ???.### 1,1,3
                                               .??..??...?##. 1,1,3
                                               ?#?#?#?#?#?#?#? 1,3,1,6
                                               ????.#...#... 4,1,1
                                               ????.######..#####. 1,6,5
                                               ?###???????? 3,2,1
                                               ?.#.?...#.?.? 1,1""");
        var day = new Day12();
        System.out.print(day.part1(input).equals("22"));
    }
}
