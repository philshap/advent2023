package advent2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day10 implements Day {
    @Override
    public int number() {
        return 10;
    }

    record Pos(int x, int y) {}

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

        Pos move(Pos pos) {
            return new Pos(pos.x + dx, pos.y + dy);
        }

        static final Map<Dir, Map<Character, Dir>> TURNS =
                Map.of(Dir.W, Map.of('-', Dir.W, 'F', Dir.S, 'L', Dir.N),
                       Dir.E, Map.of('-', Dir.E, '7', Dir.S, 'J', Dir.N),
                       Dir.S, Map.of('|', Dir.S, 'J', Dir.W, 'L', Dir.E),
                       Dir.N, Map.of('|', Dir.N, '7', Dir.W, 'F', Dir.E));

        Dir turn(char pipe) {
            return TURNS.get(this).get(pipe);
        }
    }

    record Data(Map<Pos, Character> map, Pos start) {
        static Data fromInput(List<String> input) {
            Map<Pos, Character> map = new HashMap<>();
            Pos start = new Pos(0, 0);
            for (int y = 0; y < input.size(); y++) {
                String line = input.get(y);
                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    if (c != '.') {
                        if (c == 'S') {
                            start = new Pos(x, y);
                        } else {
                            map.put(new Pos(x, y), c);
                        }
                    }
                }
            }
            return new Data(map, start);
        }

        private List<Dir> getStartDirs() {
            return Arrays.stream(Dir.values()).filter(d -> {
                Character next = map.get(d.move(start));
                return next != null && d.turn(next) != null;
            }).toList();
        }

        // Should generate this data from Dir.TURNS
        static final Map<Set<Dir>, Character> PIPES =
                Map.of(Set.of(Dir.N, Dir.S), '|',
                       Set.of(Dir.N, Dir.E), 'L',
                       Set.of(Dir.N, Dir.W), 'J',
                       Set.of(Dir.S, Dir.E), 'F',
                       Set.of(Dir.S, Dir.W), '7');

        private Map<Pos, Character> findPath() {
            Map<Pos, Character> path = new HashMap<>();
            List<Dir> dirs = getStartDirs();
            path.put(start, PIPES.get(Set.copyOf(dirs)));
            Dir dir = dirs.getFirst();
            Pos pos = dir.move(start);
            while (!pos.equals(start)) {
                char pipe = map.get(pos);
                path.put(pos, pipe);
                dir = dir.turn(pipe);
                pos = dir.move(pos);
            }
            return path;
        }
    }

    @Override
    public String part1(List<String> input) {
        var path = Data.fromInput(input).findPath();
        return String.valueOf(path.size() / 2);
    }

    @Override
    public String part2(List<String> input) {
        var data = Data.fromInput(input);
        var path = data.findPath();

        // Inside count is number of empty positions at odd crossings per line.
        int inside = 0;
        for (int y = 0; y < input.size(); y++) {
            int pipeCount = 0;
            for (int x = 0; x < input.get(y).length(); x++) {
                Pos pos = new Pos(x, y);
                Character pipe = path.get(pos);
                if (pipe == null) {
                    if (pipeCount % 2 == 1) {
                        inside++;
                    }
                } else if (pipe == '|' || pipe == 'L' || pipe == 'J') {
                    pipeCount++;
                }
            }
        }
        return String.valueOf(inside);
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               -L|F7
                                               7S-7|
                                               L|7||
                                               -L-J|
                                               L|-JF""");
        var day = new Day10();
        System.out.println(day.part1(input));

        var input2 = Support.splitInput("""
                                                .F----7F7F7F7F-7....
                                                .|F--7||||||||FJ....
                                                .||.FJ||||||||L7....
                                                FJL7L7LJLJ||LJ.L-7..
                                                L--J.L7...LJS7F-7L7.
                                                ....F-J..F7FJ|L7L7L7
                                                ....L7.F7||L7|.L7L7|
                                                .....|FJLJ|FJ|F7|.LJ
                                                ....FJL-7.||.||||...
                                                ....L---J.LJ.LJLJ...""");
        System.out.println(day.part2(input2));
    }
}
