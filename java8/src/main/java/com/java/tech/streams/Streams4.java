package com.java.tech.streams;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * created by Jason on 2020/2/23
 */
public class Streams4 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int max = 1000000;
        List<String> randomList = new ArrayList<>(max);
        for (int i = 0; i < max; i++) {
            randomList.add(UUID.randomUUID().toString());
        }
        //串行计算
        long count = randomList.stream().count();
        System.out.println(count);

        long end = System.currentTimeMillis();
        System.out.println("const time:" + (end - start) + "ms");

        System.out.println("---------parallel stream-----------");

        long startTime = System.currentTimeMillis();
        int maxNum = 1000000;
        List<String> maxList = new ArrayList<>(maxNum);
        for (int i = 0; i < maxNum; i++) {
            maxList.add(UUID.randomUUID().toString());
        }
        //并行计算
        long countNum = maxList.parallelStream().count();
        System.out.println(countNum);

        long endTime = System.currentTimeMillis();
        System.out.println("parallel const time:" + (endTime - startTime) + "ms");

    }
}
