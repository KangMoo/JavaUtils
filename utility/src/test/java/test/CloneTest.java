package test;

import com.rits.cloning.Cloner;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author kangmoo Heo
 */
public class CloneTest {
    @Test
    public void test(){
        List<Foo> a = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> a.add(new Foo(i)));
        List<Foo> b = a;

        a.get(0).data = 100;

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("");

        Cloner cloner = new Cloner();
        b = cloner.deepClone(a);
        a.get(0).data = 200;

        System.out.println("a = " + a);
        System.out.println("b = " + b);
    }

    class Foo{
        public int data;

        public Foo(int a) {
            this.data = a;
        }

        @Override
        public String toString() {
            return "" + data;
        }
    }
}
