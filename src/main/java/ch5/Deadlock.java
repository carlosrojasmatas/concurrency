package ch5;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {


    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {

        Deadlock deadlock = new Deadlock();
        Thread t1 = new Thread(deadlock::worker1);
        Thread t2 = new Thread(deadlock::worker2);
        t1.start();
        t2.start();

    }

    public void worker1(){
        try {
            lock1.lock();
            System.out.println("W1 acquired lock 1...");
            Thread.sleep(500);
            lock2.lock();
            System.out.println("W1 acquired lock 2...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
            lock2.unlock();
        }
    }

    public void worker2(){
        try {
            lock2.lock();
            System.out.println("W2 acquired lock 2...");
            Thread.sleep(500);
            lock1.lock();
            System.out.println("W1 acquired lock 1...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
            lock2.unlock();
        }
    }
}
