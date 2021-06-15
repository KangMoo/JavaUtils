package practice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 *
 * @author kangmoo Heo
 */
public class IteratorTest {


    public static void main(String[] args) {
        System.out.println("SAMPLE 1");
        sample1();
        System.out.println("SAMPLE 2");
        sample2();
        System.out.println("SAMPLE 3");
        sample3();
    }

    public static void sample1() {
        ArrayList<Integer> list = new ArrayList<>();
        IntStream.range(3, 7).forEachOrdered(list::add);
        for (Iterator<Integer> itr = list.iterator(); itr.hasNext(); ) {
            System.out.println(itr.next());
            itr.remove();
        }
    }

    public static void sample2() {
        ArrayList<Integer> list = new ArrayList<>();
        IntStream.range(3, 7).forEachOrdered(list::add);
        Iterator<Integer> itr = list.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
            itr.remove();
        }
    }

    public static void sample3() {
        ArrayList<Integer> list = new ArrayList<>();
        IntStream.range(3, 7).forEachOrdered(list::add);
        Iterator<Integer> itr = list.iterator();
        itr.forEachRemaining(num -> {
            System.out.println(num);
            // list.remove(num); // Remove 사용 시 에러
        });
        list.clear();
    }
}
