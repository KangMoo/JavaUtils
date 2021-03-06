package test;

import org.junit.Test;
import com.github.kangmoo.utils.scheduler.IntervalTaskManager;
import com.github.kangmoo.utils.scheduler.IntervalTaskUnit;

/**
 *
 * @author kangmoo Heo
 */
public class IntervalTaskTest {
    @Test
    public void Test() {
        IntervalTaskManager.getInstance().addJob("Test", new DisplayTime(1000));
        IntervalTaskManager.getInstance().start();
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntervalTaskManager.getInstance().stop();
    }


    public static class DisplayTime extends IntervalTaskUnit {
        protected DisplayTime(int interval) {
            super(interval);
        }

        @Override
        public void run() {
            System.out.println("Now = " + System.currentTimeMillis());
        }
    }
}
