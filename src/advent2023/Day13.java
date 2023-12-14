package advent2023;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13 implements Day {
    @Override
    public int number() {
        return 13;
    }

    record Pattern(List<String> rows, List<String> columns, int mirrorRow, int mirrorCol) {
        static Pattern fromLines(List<String> lines) {
            List<String> columns =
                    IntStream.range(0, lines.getFirst().length())
                             .mapToObj(i -> lines.stream().map(s -> s.charAt(i)).collect(Support.collectToString()))
                             .toList();
            return new Pattern(lines, columns, findMirror(lines, 0), findMirror(columns, 0));
        }

        static Integer mirrorAtIndex(int i, List<String> values) {
            int before = i - 1;
            int after = i;
            while (values.get(before--).equals(values.get(after++))) {
                if (before == -1 || after == values.size()) {
                    return i;
                }
            }
            return null;
        }

        static int findMirror(List<String> list, int skip) {
            return IntStream.range(1, list.size())
                            .filter(i -> i != skip)
                            .mapToObj(i -> mirrorAtIndex(i, list))
                            .filter(Objects::nonNull)
                            .findFirst().orElse(0);
        }

        int mirrorVal() {
            return mirrorCol + mirrorRow * 100;
        }

        // Part 2: smudged mirror

        String flip(String s, int i) {
            var sb = new StringBuilder(s);
            sb.setCharAt(i, s.charAt(i) == '#' ? '.' : '#');
            return sb.toString();
        }

        private List<String> flipAt(List<String> data, int i1, int i2) {
            return IntStream.range(0, data.size()).mapToObj(i -> {
                String s = data.get(i);
                return i == i1 ? flip(s, i2) : s;
            }).toList();
        }

        private Pattern withSmudgeAt(int row, int column) {
            List<String> smudgeRows = flipAt(rows, row, column);
            List<String> smudgeCols = flipAt(columns, column, row);
            return new Pattern(smudgeRows, smudgeCols, findMirror(smudgeRows, mirrorRow), findMirror(smudgeCols, mirrorCol));
        }

        Stream<Pattern> withSmudges() {
            return IntStream.range(0, rows.size())
                            .boxed()
                            .flatMap(row -> IntStream.range(0, columns.size())
                                                     .mapToObj(column -> withSmudgeAt(row, column)));
        }

        int smudgeMirrorVal() {
            return withSmudges().mapToInt(Pattern::mirrorVal).filter(val -> val != 0).findFirst().orElseThrow();
        }
    }

    List<Pattern> readPatterns(List<String> input) {
        List<Pattern> patterns = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isEmpty()) {
                patterns.add(Pattern.fromLines(input.subList(start, i)));
                start = i + 1;
            }
        }
        patterns.add(Pattern.fromLines(input.subList(start, input.size())));
        return patterns;
    }

    @Override
    public String part1(List<String> input) {
        int total = readPatterns(input).stream().mapToInt(Pattern::mirrorVal).sum();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        long total = readPatterns(input).stream().mapToInt(Pattern::smudgeMirrorVal).sum();
        return String.valueOf(total);
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               #.##..##.
                                               ..#.##.#.
                                               ##......#
                                               ##......#
                                               ..#.##.#.
                                               ..##..##.
                                               #.#.##.#.
                                                                                               
                                               #...##..#
                                               #....#..#
                                               ..##..###
                                               #####.##.
                                               #####.##.
                                               ..##..###
                                               #....#..#""");
        var day = new Day13();
        System.out.println(day.part1(input).equals("405"));
        System.out.println(day.part2(input).equals("400"));
    }
}
