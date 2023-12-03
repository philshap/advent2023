package advent2023;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day3 implements Day {
    @Override
    public int number() {
        return 3;
    }

    record Pos(int x, int y) {
        Stream<Pos> neighbors() {
            return Stream.of(-1, 0, 1)
                    .flatMap(dx -> Stream.of(-1, 0, 1)
                            .map(dy -> new Pos(x + dx, y + dy)));
        }
    }

    record Part(int number) {
        @Override
        public boolean equals(Object obj) {
            // Need to distinguish between two different parts that have the same number.
            return this == obj;
        }
    }

    record Data(Map<Pos, Part> parts, Map<Pos, Character> symbols) {
        static final Pattern NUMBER_OR_SYMBOL = Pattern.compile("(\\d+)|([^0-9.])");

        void parseRow(String row, int rowNum) {
            NUMBER_OR_SYMBOL.matcher(row).results().forEach(numOrSym -> {
                String number = numOrSym.group(1);
                if (number != null) {
                    Part part = new Part(Integer.parseInt(number));
                    for (int x = numOrSym.start(1); x < numOrSym.end(1); x++) {
                        parts.put(new Pos(x, rowNum), part);
                    }
                } else {
                    symbols.put(new Pos(numOrSym.start(2), rowNum), numOrSym.group(2).charAt(0));
                }
            });
        }

        // parse data into part numbers and symbols
        static Data fromInput(List<String> input) {
            Data data = new Data(new HashMap<>(), new HashMap<>());
            for (int y = 0; y < input.size(); y++) {
                data.parseRow(input.get(y), y);
            }
            return data;
        }

        Stream<Part> partsNear(Pos pos) {
            return pos.neighbors().map(parts::get).filter(Objects::nonNull).distinct();
        }

        // for each symbol, find any parts nearby
        int allPartsSum() {
            return symbols.keySet().stream().flatMap(this::partsNear).mapToInt(Part::number).sum();
        }

        int gearRatiosSum() {
            return symbols.entrySet().stream()
                    .filter(entry -> entry.getValue() == '*')
                    .map(Entry::getKey)
                    .map(pos -> partsNear(pos).map(Part::number).toList())
                    .filter(parts -> parts.size() == 2)
                    .mapToInt(parts -> parts.get(0) * parts.get(1))
                    .sum();
        }
    }

    @Override
    public String part1(List<String> input) {
        return String.valueOf(Data.fromInput(input).allPartsSum());
    }

    @Override
    public String part2(List<String> input) {
        return String.valueOf(Data.fromInput(input).gearRatiosSum());
    }

    public static void main(String[] args) {
        var input = List.of("467..114..",
                            "...*......",
                            "..35..633.",
                            "......#...",
                            "617*......",
                            ".....+.58.",
                            "..592.....",
                            "......755.",
                            "...$.*....",
                            ".664.598..");
        System.out.println(new Day3().part1(input));
        System.out.println(new Day3().part2(input));
    }
}
