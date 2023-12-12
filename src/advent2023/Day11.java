package advent2023;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day11 implements Day {
    @Override
    public int number() {
        return 11;
    }

    record Pos(int x, int y) { }

    record Image(Set<Pos> galaxies, Set<Integer> nonEmptyRows, Set<Integer> nonEmptyCols, int emptySize) {
        static Image fromInput(List<String> input, int emptySize) {
            var image = new Image(new HashSet<>(), new HashSet<>(), new HashSet<>(), emptySize);
            for (int y = 0; y < input.size(); y++) {
                String line = input.get(y);
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '#') {
                        image.galaxies.add(new Pos(x, y));
                        image.nonEmptyCols.add(x);
                        image.nonEmptyRows.add(y);
                    }
                }
            }
            return image;
        }

        long lineLength(Pair<Pos, Pos> points) {
            long length = 0;
            int x = points.l().x;
            int y = points.l().y;
            Pos p2 = points.r();
            while (x != p2.x || y != p2.y) {
                int dx = p2.x - x;
                int dy = p2.y - y;
                if (Math.abs(dx) > Math.abs(dy)) {
                    x += (int) Math.signum(dx);
                    length += nonEmptyCols.contains(x) ? 1 : emptySize;
                } else {
                    y += (int) Math.signum(dy);
                    length += nonEmptyRows.contains(y) ? 1 : emptySize;
                }
            }
            return length;
        }

        private long allPathsSum() {
            return pairCombos(galaxies).mapToLong(this::lineLength).sum();
        }
    }

    static <T> Stream<Pair<T, T>> pairCombos(Collection<T> elements) {
        List<T> list = List.copyOf(elements);
        return IntStream.range(0, elements.size()).boxed()
                        .flatMap(i -> IntStream.range(i + 1, elements.size()).boxed()
                                               .map(j -> Pair.of(list.get(i), list.get(j))));
    }

    @Override
    public String part1(List<String> input) {
        Image image = Image.fromInput(input, 2);
        return String.valueOf(image.allPathsSum());
    }

    @Override
    public String part2(List<String> input) {
        Image image = Image.fromInput(input, 1_000_000);
        return String.valueOf(image.allPathsSum());
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               ...#......
                                               .......#..
                                               #.........
                                               ..........
                                               ......#...
                                               .#........
                                               .........#
                                               ..........
                                               .......#..
                                               #...#.....""");
        var day = new Day11();
        System.out.println(day.part1(input).equals("374"));
        System.out.println(day.part2(input).equals("82000210"));
    }
}
