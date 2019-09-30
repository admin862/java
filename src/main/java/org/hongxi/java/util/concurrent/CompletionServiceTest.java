package org.hongxi.java.util.concurrent;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author shenhongxi 2019/8/11
 */
public class CompletionServiceTest {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);

        // produce
        for (int i = 0; i < 10; i++) {
            final int seq = i + 1;
            completionService.submit(() -> {
                Thread.sleep(new Random().nextInt(3000));
                return seq;
            });
        }

        // consume
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(completionService.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
}
