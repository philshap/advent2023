package advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day5 implements Day {
    @Override
    public int number() {
        return 5;
    }

    record Range(long destination, long source, long length) {
        static final Pattern RANGE = Pattern.compile("(\\d+) (\\d+) (\\d+)");
        public static Range fromLine(String line) {
            return RANGE.matcher(line).results()
                        .map(range -> new Range(Long.parseLong(range.group(1)),
                                                Long.parseLong(range.group(2)),
                                                Long.parseLong(range.group(3))))
                    .findFirst().orElseThrow();
        }

        Long toDest(long i) {
            if (source <= i && i < (source + length)) {
                return destination + (i - source);
            }
            return null;
        }
    }

    record RangeMap(String name, List<Range> ranges) {
        long mapSeed(long seed) {
            return ranges.stream()
                         .map(range -> range.toDest(seed))
                         .filter(Objects::nonNull)
                         .findFirst().orElse(seed);
        }
    }

    record Data(List<Long> seeds, List<RangeMap> maps) {
        static final Pattern NUMBER = Pattern.compile("(\\d+)");
        static Data fromLines(List<String> lines) {
            List<Long> seeds = NUMBER.matcher(lines.get(0)).results().map(MatchResult::group).map(Long::parseLong).toList();
            RangeMap rangeMap = null;
            List<RangeMap> maps = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isEmpty()) {
                    rangeMap = new RangeMap(lines.get(++i), new ArrayList<>());
                    maps.add(rangeMap);
                } else {
                    rangeMap.ranges.add(Range.fromLine(line));
                }
            }
            return new Data(seeds, maps);
        }

        long plantSeed(long seed) {
            long location = seed;
            for (RangeMap map : maps) {
                location = map.mapSeed(location);
            }
            return location;
        }

        LongStream locations() {
            return seeds.stream().mapToLong(i -> i).map(this::plantSeed).sorted();
        }
    }

    // 147655913 too low

    @Override
    public String part1(List<String> input) {
        Data data = Data.fromLines(input);
        return String.valueOf(data.locations().findFirst().orElseThrow());
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    public static void main(String[] args) {
        List<String> input = Arrays.asList("""
                seeds: 79 14 55 13
                                
                seed-to-soil map:
                50 98 2
                52 50 48
                                
                soil-to-fertilizer map:
                0 15 37
                37 52 2
                39 0 15
                                
                fertilizer-to-water map:
                49 53 8
                0 11 42
                42 0 7
                57 7 4
                                
                water-to-light map:
                88 18 7
                18 25 70
                                
                light-to-temperature map:
                45 77 23
                81 45 19
                68 64 13
                                
                temperature-to-humidity map:
                0 69 1
                1 0 69
                                
                humidity-to-location map:
                60 56 37
                56 93 4""".split("\\n"));
        System.out.println(new Day5().part1(input));
    }
}
