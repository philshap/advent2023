package advent2023;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class Main implements Support {

    private final boolean includeSlow;

    private static final String INPUT_URL = "https://adventofcode.com/2023/day/%d/input";

    public Main(String[] args) {
        includeSlow = (args.length == 1 && args[0].equals("includeSlow"));
    }

    @Override
    public String readString(int day) throws Exception {
        return CachingHttpReader.getData(new URI(INPUT_URL.formatted(day)).toURL()).trim();
    }

    @Override
    public List<String> readLines(int day) throws Exception {
        return Arrays.asList(readString(day).split("\n"));
    }

    @Override
    public boolean includeSlow() {
        return includeSlow;
    }

    private void runDays() throws Exception {
        Day[] days = {new Day1()};
        for (var day : days) {
            day.run(this);
        }
    }

    public static void main(String[] args) throws Exception {
        new Main(args).runDays();
    }
}
