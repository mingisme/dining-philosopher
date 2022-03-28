import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App {
    public static void main(String[] args) {

        for (int r = 0; r < 100; r++) {
            Chopstick[] chopsticks = new Chopstick[5];
            for (int i = 0; i < chopsticks.length; i++) {
                chopsticks[i] = new Chopstick(i);
            }

            System.out.println(Arrays.toString(chopsticks));

            Philosopher[] philosophers = new Philosopher[5];
            for (int i = 0; i < philosophers.length; i++) {
                philosophers[i] = new Philosopher(chopsticks[i], chopsticks[(i + 1) % philosophers.length]);
            }

            System.out.println(Arrays.toString(philosophers));

            for (int i = 0; i < philosophers.length; i++) {
                philosophers[i].start();
            }
        }
    }
}


class Chopstick {
    private Lock lock;
    private int number;

    public Chopstick(int number) {
        lock = new ReentrantLock();
        this.number=number;
    }

    public void pickUp() {
        lock.lock();
    }

    public void putDown() {
        lock.unlock();
    }

    public int getNumber(){
        return number;
    }
}

class Philosopher extends Thread {
    private int bites = 10;
    private Chopstick lower, higher;

    public Philosopher(Chopstick left, Chopstick right) {
        if(left.getNumber()<right.getNumber()) {
            this.lower = left;
            this.higher = right;
        }else{
            this.lower = right;
            this.higher = left;
        }
    }

    public void eat() {
        pickUp();
        chew();
        putDown();
    }

    private void putDown() {
        higher.putDown();
        lower.putDown();
    }

    private void chew() {
        System.out.println("Thread: " + Thread.currentThread().getId() + " eat something");
    }

    private void pickUp() {
        lower.pickUp();
        higher.pickUp();
    }

    public void run() {
        for (int i = 0; i < bites; i++) {
            eat();
        }
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "lower=" + lower +
                ", higher=" + higher +
                '}';
    }
}
