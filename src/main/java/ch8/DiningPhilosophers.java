package ch8;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DiningPhilosophers {

    private static int NR_PHILOSOPHERS = 5;
    private static int NR_STICK = 5;


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = null;
        Philosopher[] philosophers = new Philosopher[NR_PHILOSOPHERS];
        try {

            Stick[] sticks = new Stick[NR_STICK];
            for (int i = 0; i < NR_STICK; i++) {
                sticks[i] = new Stick(i);
            }

            executorService = Executors.newFixedThreadPool(NR_PHILOSOPHERS);

            for (int i = 0; i < NR_PHILOSOPHERS; i++) {
                philosophers[i] = new Philosopher(i, sticks[i], sticks[(i + 1) % NR_PHILOSOPHERS]);
                executorService.submit(philosophers[i]);
            }

            Thread.sleep(10000);
            executorService.shutdown();

            for (Philosopher phil : philosophers) {
                phil.setFull(true);
            }

        } finally {
            while (executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                Thread.sleep(1000);
                System.out.println("Waiting for termination...");
            }

            for (Philosopher phil : philosophers) {
                System.out.println("Philosopher " + phil + " has eaten " + phil.getEatingCount() + " times");
            }
        }


    }
}
