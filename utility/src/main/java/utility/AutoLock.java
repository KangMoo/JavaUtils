package utility;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author kangmoo Heo
 */
public class AutoLock {
    private final ReentrantLock lock = new ReentrantLock();

    public void blockLock(Runnable action) {
        try {
            lock();
            action.run();
        } finally {
            unlock();
        }
    }

    public void lock() {
        this.lock.lock();
    }

    public boolean tryLock(int mSec) {
        try {
            return this.lock.tryLock(mSec, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public void unlock() {
        this.lock.unlock();
    }
}
