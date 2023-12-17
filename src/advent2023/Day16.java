package advent2023;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 implements Day {
    @Override
    public int number() {
        return 16;
    }

    record Pos(int x, int y) {
        boolean inRange(int width, int height) {
            return 0 <= x && x < width && 0 <= y && y < height;
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

        Pos move(Pos pos) {
            return new Pos(pos.x + dx, pos.y + dy);
        }

        static final Map<Dir, Map<Character, List<Dir>>> MIRRORS =
                Map.of(Dir.W, Map.of('/', List.of(Dir.S), '\\', List.of(Dir.N), '|', List.of(Dir.N, Dir.S)),
                       Dir.E, Map.of('/', List.of(Dir.N), '\\', List.of(Dir.S), '|', List.of(Dir.N, Dir.S)),
                       Dir.N, Map.of('-', List.of(Dir.E, Dir.W), '/', List.of(Dir.E), '\\', List.of(Dir.W)),
                       Dir.S, Map.of('-', List.of(Dir.E, Dir.W), '/', List.of(Dir.W), '\\', List.of(Dir.E)));

        List<Dir> hit(char mirror) {
            return MIRRORS.get(this).getOrDefault(mirror, List.of(this));
        }
    }

    record Beam(Pos pos, Dir dir) {
        Stream<Beam> hit(char mirror) {
            return dir.hit(mirror).stream().map(d -> new Beam(d.move(pos), d));
        }
    }

    record Grid(List<String> layout, int height, int width) {
        static Grid fromInput(List<String> input) {
            return new Grid(input, input.size(), input.getFirst().length());
        }

        char charAt(Pos pos) {
            return layout.get(pos.y).charAt(pos.x);
        }

        int countEnergized(Beam start) {
            Set<Beam> beams = new HashSet<>();
            Queue<Beam> queue = new ArrayDeque<>();
            queue.add(start);
            while (!queue.isEmpty()) {
                Beam beam = queue.remove();
                if (beams.add(beam)) {
                    beam.hit(charAt(beam.pos))
                            .filter(b -> b.pos.inRange(width, height))
                            .forEach(queue::add);
                }
            }
            return (int) beams.stream().map(Beam::pos).distinct().count();
        }

        int findMaxEnergized() {
            return Stream.of(IntStream.range(0, height).mapToObj(y -> new Beam(new Pos(0, y), Dir.E)),
                             IntStream.range(0, height).mapToObj(y -> new Beam(new Pos(width - 1, y), Dir.W)),
                             IntStream.range(0, width).mapToObj(x -> new Beam(new Pos(x, height - 1), Dir.N)),
                             IntStream.range(0, height).mapToObj(x -> new Beam(new Pos(x, 0), Dir.S)))
                         .reduce(Stream::concat)
                         .orElseThrow()
                         .parallel() // parallel provides a ~2x speedup
                         .mapToInt(this::countEnergized)
                         .max().orElseThrow();
        }
    }

    @Override
    public String part1(List<String> input) {
        var count = Grid.fromInput(input).countEnergized(new Beam(new Pos(0, 0), Dir.E));
        return String.valueOf(count);
    }

    @Override
    public String part2(List<String> input) {
        var count = Grid.fromInput(input).findMaxEnergized();
        return String.valueOf(count);
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               .|...\\....
                                               |.-.\\.....
                                               .....|-...
                                               ........|.
                                               ..........
                                               .........\\
                                               ..../.\\\\..
                                               .-.-/..|..
                                               .|....-|.\\
                                               ..//.|....""");
        var day = new Day16();
        System.out.println(day.part1(input).equals("46"));
        System.out.println(day.part2(input).equals("51"));
    }
}
