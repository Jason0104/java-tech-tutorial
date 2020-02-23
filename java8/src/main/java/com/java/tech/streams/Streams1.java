package com.java.tech.streams;


import java.util.ArrayList;
import java.util.List;

/**
 * created by Jason on 2020/2/23
 */
public class Streams1 {
    public static void main(String[] args) {
        List<String> streamList = new ArrayList<>();

        streamList.add("aa");
        streamList.add("cc");
        streamList.add("dd");
        streamList.add("ff");
        streamList.add("ae");

        //filter 打印所有以a开头的字符串
        streamList.stream().filter((s -> s.startsWith("a"))).forEach(System.out::println);

        System.out.println("-------asc sort--------");
        //排序 sort
        streamList.stream().sorted().forEach(System.out::println);

        System.out.println("-------desc sort--------");
        //倒排
        streamList.stream().sorted((a,b)->b.compareTo(a)).forEach(System.out::println);

        System.out.println("--------map--------------");
        //map用于对象转换
        streamList.stream().map(String::toUpperCase).sorted((a,b)->b.compareTo(a)).forEach(System.out::println);
    }
}
