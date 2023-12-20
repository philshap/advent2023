package advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day15 implements Day {
    @Override
    public int number() {
        return 15;
    }

    static int hash(String s) {
        int val = 0;
        // Determine the ASCII code for the current character of the string.
        // Increase the current value by the ASCII code you just determined.
        // Set the current value to itself multiplied by 17.
        // Set the current value to the remainder of dividing itself by 256.
        for (byte b : s.getBytes()) {
            val += b;
            val *= 17;
            val = val % 256;
        }

        return val;
    }

    @Override
    public String part1(List<String> input) {
        int sum = Arrays.stream(input.getFirst().split(",")).mapToInt(Day15::hash).sum();
        return String.valueOf(sum);
    }

    record Lens(String label, int length) {}

    record Box(List<Lens> lenses) {
        int indexOf(String label) {
            for (int i = 0; i < lenses.size(); i++) {
                if (lenses.get(i).label.equals(label)) {
                    return i;
                }
            }
            return -1;
        }

        void remove(String label) {
            int index = indexOf(label);
            if (index != -1) {
                lenses.remove(index);
            }
        }

        void add(Lens lens) {
            int index = indexOf(lens.label);
            if (index != -1) {
                lenses.set(index, lens);
            } else {
                lenses.add(lens);
            }
        }

        int power() {
            return IntStream.range(0, lenses.size())
                            .map(i -> (hash(lenses.get(i).label) + 1) * (i + 1) * lenses.get(i).length)
                            .sum();
        }
    }

    @Override
    public String part2(List<String> input) {
        Map<Integer, Box> boxes = new HashMap<>();
        Function<String, Box> getBox = label -> boxes.computeIfAbsent(hash(label), key -> new Box(new ArrayList<>()));
        Arrays.stream(input.getFirst().split(","))
              .forEach(op -> {
                  if (op.endsWith("-")) {
                      String label = op.substring(0, op.length() - 1);
                      if (boxes.containsKey(hash(label))) {
                          getBox.apply(label).remove(label);
                      }
                  } else {
                      String[] split = op.split("=");
                      getBox.apply(split[0]).add(new Lens(split[0], Integer.parseInt(split[1])));
                  }
              });

        int total = boxes.values().stream().mapToInt(Box::power).sum();
        return String.valueOf(total);
    }

    public static void main(String[] args) {
        var input = List.of("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7");
        var day = new Day15();
        System.out.println(day.part1(input).equals("1320"));
        System.out.println(day.part2(input).equals("145"));
    }
}
