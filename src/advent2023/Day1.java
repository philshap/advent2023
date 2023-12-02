package advent2023;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

public class Day1 implements Day {

    @Override
    public int number() {
        return 1;
    }

    record Match(int index, int value) {
        private static Match findMatch(String s, List<String> toMatch, int i) {
            for (int j = 0; j < toMatch.size(); j++) {
                if (s.substring(i).startsWith(toMatch.get(j))) {
                    return new Match(i, j + 1);
                }
            }
            return null;
        }

        static Match firstMatch(String s, List<String> toMatch) {
            Match match = null;
            for (int i = 0; i < s.length() && match == null; i++) {
                match = findMatch(s, toMatch, i);
            }
            return match != null ? match : new Match(s.length(), 0);
        }

        static Match lastMatch(String s, List<String> toMatch) {
            Match match = null;
            for (int i = s.length() - 1; i >= 0 && match == null; i--) {
                match = findMatch(s, toMatch, i);
            }
            return match != null ? match : new Match(-1, 0);
        }
    }

    static final List<String> DIGITS = IntStream.range(1, 10).mapToObj(String::valueOf).toList();

    private int calibrationValue(String s) {
        int tens = Match.firstMatch(s, DIGITS).value;
        int ones = Match.lastMatch(s, DIGITS).value;
        return tens * 10 + ones;
    }

    public String part1(List<String> input) {
        int total = input.stream().mapToInt(this::calibrationValue).sum();
        return String.valueOf(total);
    }

    static final List<String> NAMES = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

    private int calibrationValue2(String s) {
        Match firstDigit = Match.firstMatch(s, DIGITS);
        Match lastDigit = Match.lastMatch(s, DIGITS);
        Match firstName = Match.firstMatch(s, NAMES);
        Match lastName = Match.lastMatch(s, NAMES);
        int tens = (firstDigit.index < firstName.index) ? firstDigit.value : firstName.value;
        int ones = (lastDigit.index > lastName.index) ? lastDigit.value : lastName.value;
        return tens * 10 + ones;
    }

    public String part2(List<String> input) {
        int total = input.stream().mapToInt(this::calibrationValue2).sum();
        return String.valueOf(total);
    }

    public static void main(String[] args) {
        Day day = new Day1();
        System.out.println(day.part1(List.of("1abc2", "pqr3stu8vwx", "a1b2c3d4e5f", "treb7uchet")));
        System.out.println(day.part2(List.of("two1nine", "eightwothree", "abcone2threexyz", "xtwone3four", "4nineeightseven2", "zoneight234", "7pqrstsixteen")));
    }
}
