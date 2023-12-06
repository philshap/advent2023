package advent2023;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day6 implements Day {
    @Override
    public int number() {
        return 6;
    }

    record Races(List<Integer> times, List<Integer> distances) {
        static Races fromLines(List<String> lines) {
            return new Races(Support.integers(lines.get(0)), Support.integers(lines.get(1)));
        }

        int distanceForTime(int pressTime, int totalTime) {
            return (totalTime - pressTime) * pressTime;
        }

        long numWiningTimes(int race) {
            int time = times.get(race);
            int distance = distances.get(race);
            return IntStream.range(0, time)
                            .map(i -> distanceForTime(i, time))
                            .filter(i -> i > distance)
                            .count();
        }
    }

    @Override
    public String part1(List<String> input) {
        Races races = Races.fromLines(input);
        long product = IntStream.range(0, races.times.size())
                                .mapToLong(races::numWiningTimes)
                                .reduce(1, (x, y) -> x * y);
        return String.valueOf(product);
    }

    record Race2(long time, long distance) {

        static long readLong(String line) {
            return Support.longs(line.replace(" ", "")).get(0);
        }

        static Race2 fromLines(List<String> lines) {
            return new Race2(readLong(lines.get(0)), readLong(lines.get(1)));
        }

        long distanceForTime(long pressTime) {
            return (time - pressTime) * pressTime;
        }

        boolean winning(long pressTime) {
            return distanceForTime(pressTime) > distance;
        }

        boolean lowEdge(long pressTime) {
            return winning(pressTime) && !winning(pressTime - 1);
        }

        boolean highEdge(long pressTime) {
            return winning(pressTime) && !winning(pressTime + 1);
        }


        long findEdge(Function<Long, Boolean> isEdge, Function<Long, Boolean> goLow) {
            long low = 0;
            long high = time;
            while (low <= high) {
                long middle = (low + high) >>> 1;
                if (isEdge.apply(middle)) {
                    return middle;
                }
                if (goLow.apply(middle)) {
                    high = middle - 1;
                } else {
                    low = middle + 1;
                }
            }
            return -1;
        }

        long waysToWin() {
            return findEdge(this::highEdge, x -> !winning(x)) - findEdge(this::lowEdge, this::winning) + 1;
        }
    }


    @Override
    public String part2(List<String> input) {
        return String.valueOf(Race2.fromLines(input).waysToWin());
    }

    public static void main(String[] args) {
        List<String> input = Support.splitInput("""                                   
                                                Time:      7  15   30
                                                Distance:  9  40  200""");
        Day6 day = new Day6();
        System.out.println(day.part1(input).equals("288"));
        System.out.println(day.part2(input).equals("71503"));
    }
}
