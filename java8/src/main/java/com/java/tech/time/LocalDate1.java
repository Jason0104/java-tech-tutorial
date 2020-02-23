package com.java.tech.time;


import java.time.LocalDate;

/**
 * created by Jason on 2020/2/23
 */
public class LocalDate1 {

    public static void main(String[] args) {
        //获取当天的日期
        LocalDate localDate = LocalDate.now();
        System.out.println("获取当天日期:" + localDate);

        //获取明天的日期
        LocalDate tomorrow = localDate.plusDays(1);
        //下个星期
        LocalDate nextWeek = localDate.plusWeeks(1);
        //下个月
        LocalDate nextMonth = localDate.plusMonths(1);
        System.out.println("获取明天日期:" + tomorrow);
        System.out.println("获取下个星期日期:" + nextWeek);
        System.out.println("获取下个月的日期:" + nextMonth);

    }
}
