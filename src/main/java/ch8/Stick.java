package ch8;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Stick {

    private int id;
    private Lock lock;

    public Stick(int id) {
        this.id = id;
        this.lock = new ReentrantLock();
    }

    public boolean pick(Philosopher philosopher, Side side) throws InterruptedException {
        if(lock.tryLock(500, TimeUnit.MILLISECONDS)){
            System.out.println("Philosopher " + philosopher.getId() + " has acquired " + side.toString() + " " + this);
            return true;
        }

        return false;
    }

    public void release(Philosopher philosopher, Side side){
        lock.unlock();
        System.out.println("Philosopher " + philosopher + " has released " + side.toString() + " " + this);
    }


    @Override
    public String toString() {
        return "Stick{" +
                "id=" + id +
                '}';
    }
}
