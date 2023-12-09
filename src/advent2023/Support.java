package advent2023;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Support {
    Pattern NUMBER = Pattern.compile("(-?\\d+)");

    static Collector<Character, StringBuilder, String> collectToString() {
        return Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString);
    }

    static <T> Stream<List<T>> partition(List<T> source, int length) {
        return partition(source, length, 0);
    }

    static <T> Stream<List<T>> partition(List<T> source, int length, int overlap) {
        return IntStream.range(0, source.size() - length + overlap).mapToObj(
                n -> source.subList(n * (length - overlap), n + length));
    }

    static List<Integer> integers(String input) {
        return NUMBER.matcher(input).results().map(MatchResult::group).map(Integer::parseInt).toList();
    }

    static List<Long> longs(String input) {
        return NUMBER.matcher(input).results().map(MatchResult::group).map(Long::parseLong).toList();
    }

    static List<String> splitInput(String input) {
        return Arrays.asList(input.split("\n"));
    }
}
