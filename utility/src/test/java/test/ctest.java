package test;

import javafx.util.Pair;
import org.junit.Test;
import utility.TimeUtil;

import java.text.ParseException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kangmoo Heo
 */
public class ctest {
    @Test
    public void test() throws ParseException {
        String nowTime =TimeUtil.dateFormat(System.currentTimeMillis(),"yyyHHmm");
        System.out.println("nowTime = " + nowTime);
        System.out.println(TimeUtil.dateFormat(nowTime,"yyyyHHmm"));
    }
}
