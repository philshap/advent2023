package advent2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Day19 implements Day {
    @Override
    public int number() {
        return 19;
    }

    record Part(Map<String, Integer> categories) {
        static Part fromLine(String line) {
            return new Part(Arrays.stream(line.substring(1, line.length() - 1).split(","))
                                  .collect(Collectors.toMap(value -> value.substring(0, 1),
                                                            value -> Integer.parseInt(value.substring(2)))));
        }
    }

    record Workflow(String name, List<String> rules) {
        static Workflow fromLine(String line) {
            var parts = line.split("[{}]");
            return new Workflow(parts[0], Arrays.stream(parts[1].split(",")).toList());
        }

        public String run(Part part) {
            for (int i = 0; i != rules.size() - 1; i++) {
                var split = rules.get(i).split(":");
                var name = split[0].substring(0, 1);
                BiFunction<Integer, Integer, Boolean> compare =
                        (split[0].charAt(1) == '>') ? (x, y) -> x > y : (x, y) -> x < y;
                var value = Integer.parseInt(split[0].substring(2));
                if (compare.apply(part.categories.get(name), value)) {
                    return split[1];
                }
            }
            return rules.getLast();
        }
    }

    record Data(Map<String, Workflow> workflows, List<Part> parts) {
        static Data fromLines(List<String> lines) {
            var data = new Data(new HashMap<>(), new ArrayList<>());
            int i = 0;
            for (; !lines.get(i).isBlank(); i++) {
                Workflow workflow = Workflow.fromLine(lines.get(i));
                data.workflows.put(workflow.name, workflow);
            }
            for (i++; i < lines.size(); i++) {
                data.parts.add(Part.fromLine(lines.get(i)));
            }
            return data;
        }

        boolean accepted(Part part) {
            String name = "in";
            for (Workflow workflow = workflows.get(name); workflow != null; workflow = workflows.get(name)) {
                name = workflow.run(part);
            }
            return name.equals("A");
        }

        int ratingsTotal() {
            return parts.stream().filter(this::accepted).flatMap(part -> part.categories().values().stream()).mapToInt(i -> i).sum();
        }
    }

    @Override
    public String part1(List<String> input) {
        Data data = Data.fromLines(input);
        return String.valueOf(data.ratingsTotal());
    }

    @Override
    public String part2(List<String> input) {
        return null;
    }

    public static void main(String[] args) {
        var input = Support.splitInput("""
                                               px{a<2006:qkq,m>2090:A,rfg}
                                               pv{a>1716:R,A}
                                               lnx{m>1548:A,A}
                                               rfg{s<537:gd,x>2440:R,A}
                                               qs{s>3448:A,lnx}
                                               qkq{x<1416:A,crn}
                                               crn{x>2662:A,R}
                                               in{s<1351:px,qqz}
                                               qqz{s>2770:qs,m<1801:hdj,R}
                                               gd{a>3333:R,R}
                                               hdj{m>838:A,pv}
                                                                                             
                                               {x=787,m=2655,a=1222,s=2876}
                                               {x=1679,m=44,a=2067,s=496}
                                               {x=2036,m=264,a=79,s=2244}
                                               {x=2461,m=1339,a=466,s=291}
                                               {x=2127,m=1623,a=2188,s=1013}""");
        var day = new Day19();
        System.out.print(day.part1(input).equals("19114"));
    }
}
