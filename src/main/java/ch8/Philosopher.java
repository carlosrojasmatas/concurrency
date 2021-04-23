package ch8;

import java.util.Random;

public class Philosopher implements Runnable {

    private int id;
    private Stick leftStick;
    private Stick rightStick;
    private int eatingCount;
    private Random random;
    private boolean isFull;


    public Philosopher(int id, Stick leftStick, Stick rightStick) {
        this.id = id;
        this.leftStick = leftStick;
        this.rightStick = rightStick;
        this.random = new Random();
        this.eatingCount = 0;

    }

    @Override
    public void run() {
        while (!isFull) {
            try {
                think();
                if (leftStick.pick(this, Side.LEFT)) {
                    if (rightStick.pick(this, Side.RIGHT)) {
                        eat();
                        rightStick.release(this, Side.RIGHT);
                    }
                    leftStick.release(this, Side.LEFT);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void think() throws InterruptedException {
        System.out.println(this + " is thinking");
        Thread.sleep(random.nextInt(1000));
    }

    private void eat() throws InterruptedException {
        System.out.println(this + "is eating");
        Thread.sleep(random.nextInt(1000));
        eatingCount++;
    }

    public int getId() {
        return id;
    }

    public Stick getLeftStick() {
        return leftStick;
    }

    public Stick getRightStick() {
        return rightStick;
    }

    public int getEatingCount() {
        return eatingCount;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "id=" + id +
                '}';
    }


}
