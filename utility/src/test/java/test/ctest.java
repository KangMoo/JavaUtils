package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kangmoo Heo
 */
public class ctest {
    @Test
    public void test() {
        AtomicInteger temp1 = new AtomicInteger(0);
        temp1.incrementAndGet();
        temp1.incrementAndGet();
        temp1.incrementAndGet();
        temp1.incrementAndGet();
        temp1.incrementAndGet();
        System.out.println(temp1.getAndSet(0));
        System.out.println(temp1.incrementAndGet());
    }

}
