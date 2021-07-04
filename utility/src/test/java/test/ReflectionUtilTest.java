package test;

import org.junit.Test;
import reflection.ReflectionUtil;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionUtilTest {
    @Test
    public void reflectionUtilTest() throws Exception {
        System.out.println("ReflectionUtil.exec(\"test.ReflectionUtilTest.sum(1,2)\") = " + ReflectionUtil.exec("test.ReflectionUtilTest.sum((int)1,(int)2)"));
    }

    public static int sum(int a, int b) {
        return a + b;
    }
}
