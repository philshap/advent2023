package advent2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day3 implements Day {
    @Override
    public int number() {
        return 3;
    }

    record Pos(int x, int y) {
        Stream<Pos> neighbors() {
            return IntStream.range(-1, 2).boxed()
                    .flatMap(dx -> IntStream.range(-1, 2).boxed()
                            .map(dy -> new Pos(x + dx, y + dy)));
        }
    }

    // Need the part's origin point to determine part uniqueness.
    record Part(Pos origin, int number) { }

    record Data(Map<Pos, Part> parts, Map<Pos, Character> symbols) {
        // parse data into part numbers and symbols
        static Data fromInput(List<String> input) {
            Map<Pos, Part> parts = new HashMap<>();
            Map<Pos, Character> symbols = new HashMap<>();
            for (int y = 0; y < input.size(); y++) {
                String row = input.get(y);
                for (int x = 0; x < input.get(0).length(); x++) {
                    char c = row.charAt(x);
                    if (Character.isDigit(c)) {
                        int val = 0;
                        List<Pos> positions = new ArrayList<>();
                        Pos partOrigin = new Pos(x, y);
                        int partX = x;
                        for (; partX < row.length(); partX++) {
                            c = row.charAt(partX);
                            if (!Character.isDigit(c)) {
                                break;
                            }
                            val = (val * 10) + c - '0';
                            positions.add(new Pos(partX, y));
                        }
                        Part part = new Part(partOrigin, val);
                        positions.forEach(pos -> parts.put(pos, part));
                        x = partX - 1;
                    } else if (c != '.') {
                        symbols.put(new Pos(x, y), c);
                    }
                }
            }
            return new Data(parts, symbols);
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
