import org.slf4j.Logger;
import com.github.kangmoo.utils.reflection.ReflectionUtil;


import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 * @author kangmoo Heo
 */
public class ReflectionTest {
    private static final Logger logger = getLogger(ReflectionTest.class);
    private static final ReflectionTest INSTANCE = new ReflectionTest();
    private static final int num = 3;

    private ReflectionTest() {
    }

    public ReflectionTest(String name) {
        System.out.println("My Name is " + name);
    }

    public static ReflectionTest getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(ReflectionUtil.exec("new Test(\"KangMoo\")"));
        System.out.println(ReflectionUtil.exec("Test.getInstance().sum(Test.getInstance().sum((float)3.14, (float)3.14), (float)3.14)"));
        System.out.println(ReflectionUtil.exec("Test.getInstance().sum(Test.getInstance().sum((int)3.14, (int)3.14), (int)3.14)"));
        System.out.println(ReflectionUtil.exec("Test.getInstance().sum(Test.getInstance().sum(1, 2), 3)"));
        System.out.println(ReflectionUtil.exec("Test.getInstance().sum(\"AAA\", \"BBB\")"));
        System.out.println(ReflectionUtil.exec("Test.getInstance().print(new java.lang.String(\"AAA\"))"));
        System.out.println(ReflectionUtil.exec("new java.lang.String(\"AABBCCDD\").length();"));
        System.out.println(ReflectionUtil.exec("Test.num"));
        logger.debug("ReflectionUtil.exec(\"Test.getInstance().sum(Test.getInstance().sum((int)3.14, (int)3.14), (int)3.14)\")={}", ReflectionUtil.exec("Test.getInstance().sum(Test.getInstance().sum((int)3.14, (int)3.14), (int)3.14)"));
    }

    public int sum(int a, int b) {
        return a + b;
    }

    public float sum(float a, float b) {
        return a + b;
    }

    public String sum(String a, String b) {
        return a + b;
    }

    public String print(String a) {
        System.out.println("PRINT " + a);
        return a;
    }

    public int[] at(int... vals) {
        return vals;
    }
}
