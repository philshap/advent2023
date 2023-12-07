package advent2023;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

public class Day7 implements Day {

    @Override
    public int number() {
        return 7;
    }

    static final List<Character> FACES = List.of('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');

    static final char JOKER = 'J';
    static int getRanking(char card, boolean useJokers) {
        if (useJokers && card == JOKER) {
            return 1;
        }
        return FACES.indexOf(card);
    }

    static final List<List<Integer>> TYPES =
            List.of(List.of(5), // 5 of a kind
                    List.of(1, 4), // four of a kind
                    List.of(2, 3), //  full house
                    List.of(1, 1, 3), // three of a kind
                    List.of(1, 2, 2), // two pair
                    List.of(1, 1, 1, 2), // one pair
                    List.of(1, 1, 1, 1, 1)); // high card

    record Hand(char[] cards, int bid, boolean useJokers, int type) implements Comparable<Hand> {
        static List<Integer> partitions(char[] cards, boolean useJokers) {
            Map<Character, Integer> partitions =
                    IntStream.range(0, cards.length)
                             .mapToObj(i -> cards[i])
                             .collect(groupingBy(identity(), reducing(0, e -> 1, Integer::sum)));
            Integer numJokers = partitions.get(JOKER);
            if (useJokers && numJokers != null && partitions.size() != 1) {
                // If the partition contains a joker, remove it and add its value to the largest remaining value.
                // Only do this if there are cards other than jokers.
                partitions.remove(JOKER);
                partitions.entrySet().stream()
                          .max(Comparator.<Map.Entry<Character, Integer>>comparingInt(Map.Entry::getValue)
                                         .thenComparingInt(o -> getRanking(o.getKey(), true)))
                          .ifPresent(entry -> partitions.put(entry.getKey(), entry.getValue() + numJokers));
            }
            // This 'partition' will match a value in TYPES above.
            return partitions.values().stream().sorted().toList();
        }

        static int type(char[] cards, boolean useJokers) {
            var partitions = partitions(cards, useJokers);
            return IntStream.range(0, TYPES.size()).filter(i -> TYPES.get(i).equals(partitions)).findFirst().orElseThrow();
        }

        Hand(char[] cards, int bid, boolean useJokers) {
            this(cards, bid, useJokers, type(cards, useJokers));
        }

        @Override
        public int compareTo(Hand other) {
            int rank = Integer.compare(type, other.type);
            for (int i = 0; rank == 0 && i < cards.length; i++) {
                rank = Integer.compare(getRanking(cards[i], useJokers), getRanking(other.cards[i], useJokers));
            }
            return rank;
        }

        static final Pattern HAND = Pattern.compile("(\\w+) (\\d+)");

        static Hand fromLine(String line, boolean useJokers) {
            return HAND.matcher(line).results()
                       .map(hand -> new Hand(hand.group(1).toCharArray(),
                                             Integer.parseInt(hand.group(2)),
                                             useJokers))
                       .findFirst().orElseThrow();
        }
    }

    private static int getWinnings(List<String> input, boolean useJokers) {
        var hands = input.stream().map(line -> Hand.fromLine(line, useJokers)).sorted(Comparator.reverseOrder()).toList();
        return IntStream.range(0, hands.size()).map(i -> (i + 1) * hands.get(i).bid).sum();
    }

    @Override
    public String part1(List<String> input) {
        int winnings = getWinnings(input, false);
        return String.valueOf(winnings);
    }

    @Override
    public String part2(List<String> input) {
        int winnings = getWinnings(input, true);
        return String.valueOf(winnings);
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               32T3K 765
                                               T55J5 684
                                               KK677 28
                                               KTJJT 220
                                               QQQJA 483""");
        Day7 day = new Day7();
        System.out.println(day.part1(input).equals("6440"));
        System.out.println(day.part2(input).equals("5905"));
    }
}
