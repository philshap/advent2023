package advent2023;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18 implements Day {
    @Override
    public int number() {
        return 18;
    }

    record Pos(int x, int y) { }

    enum Dir {
        U(0, -1),
        D(0, 1),
        R(1, 0),
        L(-1, 0);
        final int dx;
        final int dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Pos move(Pos pos) {
            return new Pos(pos.x + dx, pos.y + dy);
        }
    }

    record Dig(Dir dir, int length) {
        static Dig fromLine(String line) {
            var split = line.split(" ");
            return new Dig(Dir.valueOf(split[0]), Integer.parseInt(split[1]));
        }
    }

    record DigPlan(Set<Pos> trench, Set<Pos> edges) {
        static DigPlan fromInput(List<String> input) {
            List<Dig> digs = input.stream().map(Dig::fromLine).toList();
            Pos pos = new Pos(0, 0);
            var digPlan = new DigPlan(new HashSet<>(), new HashSet<>());
            for (int i = 0; i < digs.size(); i++) {
                Dig dig = digs.get(i);
                Dig prev = (i == 0) ? digs.getLast() : digs.get(i - 1);
                for (int j = 0; j < dig.length; j++) {
                    if (dig.dir == Dir.U || dig.dir == Dir.D) {
                        if (j != 0 || !((dig.dir == Dir.D && prev.dir == Dir.R) ||
                                (dig.dir == Dir.U && prev.dir == Dir.L))) {
                            digPlan.edges.add(pos);
                        }
                    } else if (j == 0 && ((dig.dir == Dir.R && prev.dir == Dir.D) ||
                            (dig.dir == Dir.L && prev.dir == Dir.U))) {
                        digPlan.edges.add(pos);
                    }
                    digPlan.trench.add(pos);
                    pos = dig.dir.move(pos);
                }
            }
            return digPlan;
        }

        int totalSpace() {
            var xVals = trench.stream().mapToInt(Pos::x).sorted().toArray();
            var yVals = trench.stream().mapToInt(Pos::y).sorted().toArray();
            int inside = 0;
            for (int y = yVals[0]; y < yVals[yVals.length - 1] + 1; y++) {
                int edgeCount = 0;
                for (int x = xVals[0]; x < xVals[xVals.length - 1] + 1; x++) {
                    Pos pos = new Pos(x, y);
                    if (edges.contains(pos)) {
                        edgeCount++;
                    } else if (!trench.contains(pos) && edgeCount % 2 == 1) {
                        inside++;
                    }
                }
            }
            return inside + trench.size();
        }
    }

    @Override
    public String part1(List<String> input) {
        int total = DigPlan.fromInput(input).totalSpace();
        return String.valueOf(total);
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               R 6 (#70c710)
                                               D 5 (#0dc571)
                                               L 2 (#5713f0)
                                               D 2 (#d2c081)
                                               R 2 (#59c680)
                                               D 2 (#411b91)
                                               L 5 (#8ceee2)
                                               U 2 (#caa173)
                                               L 1 (#1b58a2)
                                               U 2 (#caa171)
                                               R 2 (#7807d2)
                                               U 3 (#a77fa3)
                                               L 2 (#015232)
                                               U 2 (#7a21e3)""");
        var day = new Day18();
        System.out.println(day.part1(input));
    }
}
