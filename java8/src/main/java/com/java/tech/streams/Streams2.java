package com.java.tech.streams;


import java.util.ArrayList;
import java.util.List;

/**
 * created by Jason on 2020/2/23
 */
public class Streams2 {

    public static void main(String[] args) {
        List<String> streamList = new ArrayList<>();

        streamList.add("aa");
        streamList.add("cc");
        streamList.add("dd");
        streamList.add("ff");
        streamList.add("ae");

        //查找是否有以c开头的元素 只要有一个元素以c开头则返回true
        boolean isMatched = streamList.stream().anyMatch(s -> s.startsWith("c"));
        System.out.println(isMatched);

        //查找是否有以c开头的元素 必须所有的元素以c开头则返回true
        boolean allMatched = streamList.stream().allMatch(s -> s.startsWith("a"));
        System.out.println(allMatched);

        //只要不符合条件的返回则true
        boolean nonMatched = streamList.stream().noneMatch(s->s.startsWith("z"));
        System.out.println(nonMatched);
    }
}
