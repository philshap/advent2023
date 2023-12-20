package advent2023;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Day14 implements Day {
    @Override
    public int number() {
        return 14;
    }

    record Pos(int row, int col) {
        boolean inRange(int cols, int rows) {
            return 0 <= col && col < cols && 0 <= row && row < rows;
        }
    }

    enum Dir {
        N(0, -1),
        S(0, 1),
        E(1, 0),
        W(-1, 0);
        final int dx;
        final int dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Dir opposite() {
            return switch (this) {
                case N -> S;
                case S -> N;
                case E -> W;
                case W -> E;
            };
        }

        Pos move(Pos pos) {
            return new Pos(pos.row + dy, pos.col + dx);
        }
    }

    record Platform(Set<Pos> cubes, Set<Pos> spheres, int rows, int cols) {
        static Platform fromInput(List<String> input) {
            var platform = new Platform(new HashSet<>(), new HashSet<>(), input.size(), input.getFirst().length());
            platform.loadData(input);
            return platform;
        }

        void loadData(List<String> input) {
            IntStream.range(0, rows)
                     .forEach(row -> IntStream.range(0, cols).forEach(col -> {
                         switch (input.get(row).charAt(col)) {
                         case '#' -> cubes.add(new Pos(row, col));
                         case 'O' -> spheres.add(new Pos(row, col));
                         }
                     }));
        }

        void tilt(Pos start, Dir direction) {
            Pos pos = start;
            while (pos.inRange(cols, rows)) {
                if (spheres.remove(pos)) {
                    Pos place = pos;
                    while (place.inRange(cols, rows) && !cubes.contains(place) && !spheres.contains(place)) {
                        place = direction.move(place);
                    }
                    spheres.add(direction.opposite().move(place));
                }
                pos = direction.opposite().move(pos);
            }
        }

        void tiltNorth(int col) {
            tilt(new Pos(0, col), Dir.N);
        }

        void tiltSouth(int col) {
            tilt(new Pos(rows - 1, col), Dir.S);
        }

        void tiltWest(int row) {
            tilt(new Pos(row, 0), Dir.W);
        }

        void tiltEast(int row) {
            tilt(new Pos(row, cols - 1), Dir.E);
        }

        void tiltNorth() {
            IntStream.range(0, cols).forEach(this::tiltNorth);
        }

        int totalLoad() {
            return spheres.stream().mapToInt(Pos::row).map(row -> rows - row).sum();
        }

        Platform cycle() {
            var platform = new Platform(cubes, new HashSet<>(spheres), rows, cols);
            IntStream.range(0, cols).forEach(platform::tiltNorth);
            IntStream.range(0, rows).forEach(platform::tiltWest);
            IntStream.range(0, cols).forEach(platform::tiltSouth);
            IntStream.range(0, rows).forEach(platform::tiltEast);
            return platform;
        }
    }

    @Override
    public String part1(List<String> input) {
        var platform = Platform.fromInput(input);
        platform.tiltNorth();
        return String.valueOf(platform.totalLoad());
    }

    @Override
    public String part2(List<String> input) {
        Map<Platform, Platform> cache = new LinkedHashMap<>();
        var platform = Platform.fromInput(input);
        int size = 0;
        int N = 1_000_000_000;
        var loopEnd = 0;
        for (int i = 0; i < N; i++) {
            platform = cache.computeIfAbsent(platform, Platform::cycle);
            if (cache.size() == size) {
                loopEnd = platform.hashCode();
                break;
            }
            size = cache.size();
        }
        var keys = cache.keySet().toArray(new Platform[0]);
        int loopStart = 0;
        for (int i = 0; i < keys.length; i++) {
            if (loopEnd == keys[i].hashCode()) {
                loopStart = i - 1;
                break;
            }
        }
        int index = ((N - loopStart) % (((keys.length - 1) - loopStart) + 1) - 1);
        return String.valueOf(cache.get(keys[(index) + loopStart]).totalLoad());
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               O....#....
                                               O.OO#....#
                                               .....##...
                                               OO.#O....O
                                               .O.....O#.
                                               O.#..O.#.#
                                               ..O..#O..O
                                               .......O..
                                               #....###..
                                               #OO..#....""");
        var day = new Day14();
        System.out.println(day.part1(input).equals("136"));
        System.out.println(day.part2(input).equals("64"));
    }
}
