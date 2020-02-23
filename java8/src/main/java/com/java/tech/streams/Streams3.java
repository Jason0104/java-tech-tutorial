package com.java.tech.streams;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * created by Jason on 2020/2/23
 */
public class Streams3 {

    public static void main(String[] args) {
        List<String> streamList = new ArrayList<>();

        streamList.add("aa");
        streamList.add("cc");
        streamList.add("da");
        streamList.add("ff");
        streamList.add("aa");

        //统计以a结尾的字符串个数
        long count = streamList.stream().filter(s -> s.endsWith("a")).count();
        System.out.println("统计以a结尾的字符串个数=" + count);

        //reduce
        Optional<String> reduced = streamList.stream().sorted().reduce((a, b) -> a + "#" + b);
        //如果存在打印出来
        reduced.ifPresent(System.out::println);

    }
}
