package advent2023;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class Main {

    private static final String INPUT_URL = "https://adventofcode.com/2023/day/%d/input";

    String readString(int day) {
        try {
            return CachingHttpReader.getData(new URI(INPUT_URL.formatted(day)).toURL()).trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    List<String> readLines(int day) {
        return Arrays.asList(readString(day).split("\n"));
    }

    private void runDays() {
        List<Day> days = List.of(new Day1(), new Day2());
        days.stream()
                .sorted(Comparator.comparing(Day::number))
                .forEach(this::runDay);
    }

    record PartRun(String result, String duration) {
        static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("s.SSSSS");
        static PartRun run(Supplier<String> part) {
            Instant start = Instant.now();
            String result = part.get();
            Duration between = Duration.between(start, Instant.now());
            // https://stackoverflow.com/a/65586659
            return new PartRun(result, LocalTime.ofNanoOfDay(between.toNanos()).format(FORMAT));
        }
    }

    private void runDay(Day day) {
        List<String> input = readLines(day.number());
        PartRun part1 = PartRun.run(() -> day.part1(input));
        PartRun part2 = PartRun.run(() -> day.part2(input));
        System.out.printf("day %s part 1: (%s) %s%n", day.number(), part1.duration, part1.result);
        System.out.printf("day %s part 2: (%s) %s%n", day.number(), part2.duration, part2.result);
    }

    public static void main(String[] args) {
        new Main().runDays();
    }
}
