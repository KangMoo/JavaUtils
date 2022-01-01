package com.github.kangmoo.utils.practice;

import java.util.PriorityQueue;

/**
 * @author kangmoo Heo
 */
public class PriorityQueueTest {
    public static void main(String[] args) {
        PriorityQueue<Person> pq = new PriorityQueue<>(10, (o1, o2) -> o1.age - o2.age);

        pq.add(new Person("A", 11));
        pq.add(new Person("B", 20));
        pq.add(new Person("C", 30));
        pq.add(new Person("D", 25));
        pq.add(new Person("E", 50));
        pq.add(new Person("F", 33));

        pq.forEach(System.out::println);

        System.out.println("================");

        while (true) {
            Person p = pq.poll();
            if (p == null) return;
            System.out.println(p);
        }
    }

    public static class Person {
        String name;
        int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
