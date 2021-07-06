package test;

import org.junit.Test;
import com.github.kangmoo.utils.utility.AutoLock;

/**
 *
 * @author kangmoo Heo
 */
public class AutoLockTest {
    @Test
    public void autolockTest(){
        LockClass lc = new LockClass();
        Runnable conflictableJob = ()->{
            while(lc.num<1000) {
                lc.blockLock(() -> System.out.println(lc.num++));
            }
        };
        Thread thd1 = new Thread(conflictableJob);
        Thread thd2 = new Thread(conflictableJob);
        thd1.start();
        thd2.start();
        try{
            Thread.sleep(500);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public class LockClass{
        private int num = 1;
        private AutoLock autoLock = new AutoLock();
        public void blockLock(Runnable job){
            // job.run(); // Concurrency problem
            autoLock.blockLock(job);
        }
    }
}
