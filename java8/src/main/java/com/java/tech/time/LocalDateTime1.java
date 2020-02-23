package com.java.tech.time;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoField;

/**
 * created by Jason on 2020/2/23
 */
public class LocalDateTime1 {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.FEBRUARY, 23, 15, 50);
        //获取当前日期是星期几
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        System.out.println(dayOfWeek);

        //获取当前输入日期是几月
        Month month = localDateTime.getMonth();
        System.out.println(month);

        //获取分钟
        long minuteDay = localDateTime.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println(minuteDay);

    }
}
