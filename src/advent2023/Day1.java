package advent2023;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day1 implements Day {

    @Override
    public int number() {
        return 1;
    }

    static final Pattern NUMBER = Pattern.compile("(\\d)");

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    int findDigit(String s) {
        return NUMBER.matcher(s).results().map(MatchResult::group).map(Integer::parseInt).findFirst().orElseThrow();
    }

    private int calibrationValue(String s) {
        return findDigit(s) * 10 + findDigit(reverse(s));
    }

    public String part1(List<String> input) {
        int total = input.stream().mapToInt(this::calibrationValue).sum();
        return String.valueOf(total);
    }

    static final List<String> NAMES = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
    static final Pattern NUMBER_OR_NAME = Pattern.compile("(\\d|%s)".formatted(String.join("|", NAMES)));
    static final List<String> NAMES_REVERSED = NAMES.stream().map(Day1::reverse).toList();
    static final Pattern NUMBER_OR_NAME_REVERSED =
            Pattern.compile("(\\d|%s)".formatted(String.join("|", NAMES_REVERSED)));

    int findValue(String s, Pattern pattern, List<String> names) {
        return pattern.matcher(s).results().mapToInt(result ->{
            if (Character.isDigit(result.group().charAt(0))) {
                return Integer.parseInt(result.group());
            }
            return names.indexOf(result.group()) + 1;
        }).findFirst().orElseThrow();
    }

    private int calibrationValue2(String s) {
        return findValue(s, NUMBER_OR_NAME, NAMES) * 10 +
               findValue(reverse(s), NUMBER_OR_NAME_REVERSED, NAMES_REVERSED);
    }

    public String part2(List<String> input) {
        int total = input.stream().mapToInt(this::calibrationValue2).sum();
        return String.valueOf(total);
    }

    public static void main(String[] args) {
        Day day = new Day1();
        System.out.println(day.part1(List.of("1abc2", "pqr3stu8vwx", "a1b2c3d4e5f", "treb7uchet")));
        System.out.println(day.part2(List.of("two1nine", "eightwothree", "abcone2threexyz", "xtwone3four",
                "4nineeightseven2", "zoneight234", "7pqrstsixteen")));
    }
}
