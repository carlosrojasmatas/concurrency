package ch8;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Book {

    private String isbn;
    private Lock lock;
    private Random random;

    public Book(String isbn) {
        this.isbn = isbn;
        this.lock = new ReentrantLock();
        this.random = new Random();
    }

    public void read(Student student) throws InterruptedException {
        this.lock.lock();
        try {
            System.out.println("Student " + student.toString() + " is reading " + this);
            Thread.sleep(random.nextInt(5000));
            System.out.println("Student " + student.toString() + " finished reading " + this);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                '}';
    }
}


class Student implements Runnable {

    private int id;
    private Book[] toRead;
    private Book[] read;

    public Student(int id, Book[] toRead) {
        this.id = id;
        this.toRead = toRead;
        this.read = new Book[toRead.length];
    }

    @Override
    public void run() {
        for (int i = 0; i < toRead.length; i++) {
            Book curr = toRead[i];
            try {
                curr.read(this);
                read[i] = curr;
                toRead[i] = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

        System.out.println("Student " + this +  " has finished reading all books");
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                '}';
    }
}

public class Library {
    private static final int NR_OF_BOOKS = 10;
    private static final int NR_OF_STUDENTS = 5;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = null;
        Book[] books = new Book[NR_OF_BOOKS];

        try {
            es = Executors.newFixedThreadPool(NR_OF_STUDENTS);
            for (int i = 0; i < books.length; i++) {
                books[i] = new Book("isbn-" + (i + 1));
            }

            for (int i = 0; i < NR_OF_STUDENTS; i++) {
                es.submit(new Student((i + 1), books));
            }
            Thread.sleep(20000);
            System.out.println("Size of books after exec " + books.length);
            es.shutdown();
        } finally {
            if (es.awaitTermination(50, TimeUnit.MILLISECONDS)) {
                System.out.println("Waiting for students to finish the books...");
                Thread.sleep(500);
            }
        }


    }

}
