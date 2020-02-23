package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 */
public class Lambda4 {

    public static void main(String[] args) {
        //构造PersonFactory
        PersonFactory<Person> personFactory = Person::new;

        Person person = personFactory.create("Tom", "Peter");
        System.out.println(person);

    }
}
