package advent2023;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 implements Day {
    @Override
    public int number() {
        return 4;
    }

    static final Pattern NUMBER = Pattern.compile("(\\d+)");

    Set<String> numbers(String input) {
        return NUMBER.matcher(input).results().map(MatchResult::group).collect(Collectors.toSet());
    }

    static final Pattern CARD = Pattern.compile(".*:([\\d ]+)\\|([\\d ]+)");

    int numWinners(String line) {
        return CARD.matcher(line).results().mapToInt(card -> {
            Set<String> winners = numbers(card.group(1));
            winners.retainAll(numbers(card.group(2)));
            return winners.size();
        }).findFirst().orElseThrow();
    }

    int score(String line) {
        int numWinners = numWinners(line);
        return numWinners == 0 ? 0 : 1 << (numWinners - 1);
    }

    @Override
    public String part1(List<String> input) {
        int total = input.stream().mapToInt(this::score).sum();
        return String.valueOf(total);
    }

    final Map<String, Integer> expandCounts = new HashMap<>();

    int expandCount(String card, List<String> cards) {
        Integer cachedCount = expandCounts.get(card);
        if (cachedCount != null) {
            return cachedCount;
        }
        int cardIndex = cards.indexOf(card) + 1;
        int count = 1 + cards.subList(cardIndex, cardIndex + numWinners(card)).stream()
                             .mapToInt(subCard -> expandCount(subCard, cards)).sum();
        expandCounts.put(card, count);
        return count;
    }

    @Override
    public String part2(List<String> input) {
        int total = input.stream().mapToInt(line -> expandCount(line, input)).sum();
        return String.valueOf(total);
    }

    public static void main(String[] args) {
        var input = List.of("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                            "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                            "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                            "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                            "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                            "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11");
        System.out.println(new Day4().part2(input));
    }
}
