package advent2023;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Support {
    Pattern NUMBER = Pattern.compile("(\\d+)");

    static Collector<Character, StringBuilder, String> collectToString() {
        return Collector.of(StringBuilder::new, StringBuilder::append, StringBuilder::append, StringBuilder::toString);
    }

    // https://stackoverflow.com/a/32435407
    static <T> Stream<List<T>> partition(List<T> source, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length = " + length);
        }
        int size = source.size();
        if (size == 0) {
            return Stream.empty();
        }
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
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
