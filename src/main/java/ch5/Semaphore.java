package ch5;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

enum Worker {
    INSTANCE;

    private java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(5, true);

    public void doWork(Runnable runnable) {
        try {
            semaphore.acquire();
            runnable.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

}

public class Semaphore {

    public static void main(String[] args) {
        Worker worker = Worker.INSTANCE;

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 1; i < 100; i++) {
            es.submit(() -> worker.doWork(() -> {
                System.out.println("Thread " + Thread.currentThread().getName() + " is executing work");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        }
    }
}
