package com.java.tech.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * created by Jason on 2020/2/22
 */
public class Lambda1 {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("apple", "pear", "orange", "banana");

        //Java8之前想对name进行倒排
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("java8之前排序方法:" + names);

        //java8 lambda写法更加简洁
        names.sort((a,b)->b.compareTo(a));
        System.out.println("java8之后排序方法:" + names);
    }
}
