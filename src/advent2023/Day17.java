package advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class Day17 implements Day {
    @Override
    public int number() {
        return 17;
    }

    record Pos(int x, int y) {
        boolean inRange(int width, int height) {
            return 0 <= x && x < width && 0 <= y && y < height;
        }
    }

    enum Dir {
        N(0, -1, '^'),
        S(0, 1, 'v'),
        E(1, 0, '>'),
        W(-1, 0, '<');
        final int dx;
        final int dy;
        final char ch;

        Dir(int dx, int dy, char ch) {
            this.dx = dx;
            this.dy = dy;
            this.ch = ch;
        }

        Pos move(Pos pos) {
            return new Pos(pos.x + dx, pos.y + dy);
        }
    }

    record City(List<String> input) {
        int loss(Pos pos) {
            return Integer.parseInt(input.get(pos.y).substring(pos.x, pos.x + 1));
        }

        int width() {
            return input.getFirst().length();
        }

        int height() {
            return input.size();
        }
    }

    record Move(Pos pos, Dir dir) {
    }

    record Path(List<Move> moves, int loss) {
        Stream<Path> extend(City city) {
            var lastThree = moves.size() > 3 ? moves.subList(moves.size() - 3, moves.size()).stream().map(Move::dir).distinct().toList() : List.of();
            var excludeDir = lastThree.size() == 1 ? lastThree.getFirst() : null;
            var head = moves.getLast();
            return Arrays.stream(Dir.values())
                         .filter(dir -> !dir.equals(excludeDir))
                         .map(dir -> new Move(dir.move(head.pos), dir))
                         .filter(move -> move.pos.inRange(city.width(), city.height()))
                         .filter(move -> moves.stream().map(Move::pos).noneMatch(move.pos::equals))
                         .map(move -> {
                             var newMoves = new ArrayList<>(moves);
                             newMoves.add(move);
                             return new Path(newMoves, loss + city.loss(move.pos));
                         });

        }
    }

    int distance(Pos p1, Pos p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    void draw(City city, Path path) {
        for (int y = 0; y < city.height(); y++) {
            for (int x = 0; x < city.width(); x++) {
                Pos pos = new Pos(x, y);
                Move move = path.moves.stream().filter(m -> m.pos.equals(pos)).findFirst().orElse(null);
                if (move != null) {
                    System.out.print(move.dir.ch);
                } else {
                    System.out.print(city.loss(pos));
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    int findLeastLoss(List<String> input) {
        City city = new City(input);
        Pos start = new Pos(0, 0);
        Pos end = new Pos(city.width() - 1, city.height() - 1);
        var queue = new PriorityQueue<>(Comparator.comparingInt((Path path) -> distance(path.moves.getLast().pos, end) * path.loss));
        queue.add(new Path(List.of(new Move(start, Dir.N)), 0));
        Path min = new Path(null, Integer.MAX_VALUE);
        while (!queue.isEmpty()) {
            var head = queue.poll();
            if (head.moves.getLast().pos.equals(end)) {
                if (min.loss > head.loss) {
                    System.out.println(head.loss);
                    min = head;
                }
            }
            head.extend(city).forEach(queue::add);
        }
        return min.loss;
    }

    // 1461 too high

    @Override
    public String part1(List<String> input) {
//        int loss = findLeastLoss(input);
//        return String.valueOf(loss);
        return null;
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               2413432311323
                                               3215453535623
                                               3255245654254
                                               3446585845452
                                               4546657867536
                                               1438598798454
                                               4457876987766
                                               3637877979653
                                               4654967986887
                                               4564679986453
                                               1224686865563
                                               2546548887735
                                               4322674655533""");
        var day = new Day17();
        System.out.println(day.part1(input));
    }
}
