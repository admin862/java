package org.hongxi.java.util.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Created by shenhongxi on 2018/1/11.
 */
public class StreamTest {
    public static void main(String[] args) {
        Collection<Task> tasks = Arrays.asList(
                new Task(Status.OPEN, 5),
                new Task(Status.OPEN, 13),
                new Task(Status.CLOSED, 8)
        );
        int openTaskPoints = tasks
                .stream()
                .filter(task -> task.getStatus() == Status.OPEN)
                .mapToInt(Task::getPoint)
                .sum();
        System.out.println(openTaskPoints);

        final int totalPoints = tasks
                .stream()
                .parallel()
                .map(Task::getPoint)
                .reduce(0, Integer::sum);
        System.out.println(totalPoints);

        Map<Status, List<Task>> map = tasks.stream().collect(groupingBy(Task::getStatus));
        System.out.println(map);
        Map<Status, Long> map2 = tasks.stream().collect(groupingBy(Task::getStatus, counting()));
        System.out.println(map2);
        Map<Boolean, List<Task>> map3 = tasks.stream().collect(partitioningBy(e -> e.getPoint() > 7));
        System.out.println(map3);
//        Map<String, Product> productMap = products.stream().collect(toMap(Product::getSku, identity(), (key1, key2) -> key2));

        Collection<String> result = tasks
                .stream()
                .mapToInt(Task::getPoint)
                .mapToDouble(point -> point * 100 / totalPoints)
                .mapToObj(percentage -> (int) percentage + "%")
                .collect(toList());
        System.out.println(result);
    }
}
