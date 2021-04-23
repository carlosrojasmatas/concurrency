package ch4;


import java.util.ArrayList;
import java.util.List;

class Processor {
    private List<Integer> list = new ArrayList<>();
    private int value = 0;
    private static final int LOW = 0;
    private static final int UPP = 10;
    private static final Object lock = new Object();

    public void produce() throws InterruptedException {
        synchronized (lock){
            while (true) {
                if (list.size() == UPP) {
                    System.out.println("Wating for consumer...");
                    lock.wait();
                } else {
                    System.out.println("Adding value " + value);
                    list.add(value++);
                    lock.notify();
                }
                Thread.sleep(500);
            }
        }

    }

    public void consume() throws InterruptedException {
        synchronized (lock){
            while (true) {
                if (list.size() == LOW) {
                    System.out.println("Wating for producer...");
                    lock.wait();
                } else {
                    System.out.println("Consuming value " + list.remove(list.size() - 1));
                    lock.notify();
                }
                Thread.sleep(500);
            }
        }
    }


}

public class WaitAndNotifyConProc {

    public static void main(String[] args) {
        Processor processor = new Processor();
        Thread t1 = new Thread(() -> {
            try {
                processor.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                processor.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }
}
