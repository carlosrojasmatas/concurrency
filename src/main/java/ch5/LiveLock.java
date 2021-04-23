package ch5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLock {
    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    public void worker1(){

        while(true){
            System.out.println("W1 tries to acquire the lock 1...");
            try {
                lock1.tryLock(50, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("W1 acquired lock 1...");
            System.out.println("W1 tries to acquire the lock 2...");

            if(lock2.tryLock()){
                System.out.println("W1 acquired lock 2...");
                lock2.unlock();
            }else{
                System.out.println("W1 can't acquire lock 2");
            }
            lock1.unlock();

        }
    }

    public void worker2(){

        while(true){
            System.out.println("W2 tries to acquire the lock 2...");
            try {
                lock2.tryLock(50, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("W2 acquired lock 2...");
            System.out.println("W2 tries to acquire the lock 1...");

            if(lock1.tryLock()){
                System.out.println("W2 acquired lock 1...");
                lock1.unlock();
            }else{
                System.out.println("W2 can't acquire lock 1");
            }
            lock2.unlock();
        }
    }

    public static void main(String[] args) {
        LiveLock liveLock = new LiveLock();
        Thread t1 = new Thread(liveLock::worker1);
        Thread t2 = new Thread(liveLock::worker2);

        t1.start();
        t2.start();
    }


}
