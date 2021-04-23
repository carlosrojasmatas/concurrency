package ch4;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Worker {
    private List<Integer> list = new ArrayList<>();
    private int value = 0;
    private static final int LOW = 0;
    private static final int UPP = 10;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void produce() throws InterruptedException {
        try {
            lock.lock();
            while (true) {
                if (list.size() == UPP) {
                    System.out.println("Wating for consumer...");
                    condition.await();
                } else {
                    System.out.println("Adding value " + value);
                    list.add(value++);
                    condition.signal();
                }
                Thread.sleep(500);
            }
        } finally {
            lock.unlock();
        }


    }

    public void consume() throws InterruptedException {
        try {
            lock.lock();
            while (true) {
                if (list.size() == LOW) {
                    System.out.println("Wating for producer...");
                    condition.await();
                } else {
                    System.out.println("Consuming value " + list.remove(list.size() - 1));
                    condition.signal();
                }
                Thread.sleep(500);
            }
        } finally {
            lock.unlock();
        }
    }


}

public class LockConProc {

    public static void main(String[] args) {
        Worker worker = new Worker();
        Thread t1 = new Thread(() -> {
            try {
                worker.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                worker.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }
}
