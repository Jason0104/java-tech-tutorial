package com.java.tech.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * created by Jason on 2020/2/22
 */
public class Lambda6 {
    public static void main(String[] args) {
        String str = "Predicate";
        //Predicate是一个断言接口
        //获得string
        Predicate<String> predicate = s -> s.length() > 0;
        System.out.println(predicate.test(str));//true
        System.out.println(predicate.negate().test("apple"));//false

        Predicate<String> isEmpty = String::isEmpty;
        System.out.println(isEmpty.test(str));

        //Function可以接收参数并且能够生成结果
        Function<String, Integer> from = Integer::valueOf;
        Function<String, String> end = from.andThen(String::valueOf);
        String result = end.apply("456");
        System.out.println(result);

        //Supplier能够根据具体类型生成结果,与Functions不一样,Suppliers不能接收参数
        Supplier<Person> supplier = Person::new;
        Person person = supplier.get();
        person.setFirstName("Jason");
        person.setLastName("Chen");
        System.out.println(person);

        //Consumer 能够接收参数并执行
        Consumer<Person> greeting = (p) -> System.out.println("Hello " + p.getFirstName());
        greeting.accept(new Person("Jason", "Chen"));

        //Comparators
        Person p1 = new Person("Alice", "Chen");
        Person p2 = new Person("Bob", "Wang");
        List<Person> personList = new ArrayList<>();
        personList.add(p1);
        personList.add(p2);

        personList.sort((a, b) -> b.getFirstName().compareTo(a.getFirstName()));
        System.out.println("排序后的结果为:" + personList);
    }
}
