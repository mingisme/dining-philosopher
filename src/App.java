import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App {
    public static void main(String[] args) {

        for (int r = 0; r < 100; r++) {
            Chopstick[] chopsticks = new Chopstick[5];
            for (int i = 0; i < chopsticks.length; i++) {
                chopsticks[i] = new Chopstick();
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

    public Chopstick() {
        lock = new ReentrantLock();
    }

    public boolean pickUp() {
        return lock.tryLock();
    }

    public void putDown() {
        lock.unlock();
    }
}

class Philosopher extends Thread {
    private int bites = 10;
    private Chopstick left, right;

    public Philosopher(Chopstick left, Chopstick right) {
        this.left = left;
        this.right = right;
    }

    public void eat() {
        if (pickUp()) {
            chew();
            putDown();
        }
    }

    private void putDown() {
        right.putDown();
        left.putDown();
    }

    private void chew() {
        System.out.println("Thread: " + Thread.currentThread().getId() + " eat something");
    }

    private boolean pickUp() {
        if (!left.pickUp()) {
            return false;
        }
        if (!right.pickUp()) {
            left.putDown();
            return false;
        }
        return true;
    }

    public void run() {
        for (int i = 0; i < bites; i++) {
            eat();
        }
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
