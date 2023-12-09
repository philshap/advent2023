package advent2023;

import java.util.List;

public class Day9 implements Day {
    @Override
    public int number() {
        return 9;
    }

    interface Extend {
        long extend(List<Long> seq, long prev);
    }

    long computeNext(List<Long> sequence, Extend extend) {
        if (sequence.stream().allMatch(l -> l == 0)) {
            return 0;
        }
        return extend.extend(sequence, computeNext(Support.partition(sequence, 2, 1)
                                                           .map(pair -> pair.get(1) - pair.get(0))
                                                           .toList(),
                                                   extend));
    }

    private long getTotal(List<String> input, Extend extend) {
        return input.stream().map(Support::longs)
                .mapToLong(longs -> computeNext(longs, extend))
                .sum();
    }

    @Override
    public String part1(List<String> input) {
        return String.valueOf(getTotal(input, (seq, prev) -> seq.getLast() + prev));
    }

    @Override
    public String part2(List<String> input) {
        return String.valueOf(getTotal(input, (seq, prev) -> seq.getFirst() - prev));
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                       0 3 6 9 12 15
                                       1 3 6 10 15 21
                                       10 13 16 21 30 45""");
        var day = new Day9();
        System.out.println(day.part1(input));
        System.out.println(day.part2(input));
    }
}
