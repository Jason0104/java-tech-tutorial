package com.java.tech.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * created by Jason on 2020/2/23
 */
public class LocalTime1 {

    public static void main(String[] args) {
        //Clock
        Clock clock = Clock.systemDefaultZone();
        long millis = clock.millis();
        System.out.println(millis);

        Instant instant = clock.instant();
        Date date = Date.from(instant);
        System.out.println(date);

        //Timezones
        System.out.println("获得所有的时区" + ZoneId.getAvailableZoneIds());

        ZoneId zone1 = ZoneId.of("Asia/Shanghai");
        ZoneId zone2 = ZoneId.of("Japan");
        System.out.println(zone1.getRules());
        System.out.println(zone2.getRules());

        //LocaleTime
        LocalTime localTime1 = LocalTime.now(zone1);
        LocalTime localTime2 = LocalTime.now(zone2);
        System.out.println(localTime1.isBefore(localTime2));

        //计算两个时区相差时间
        long hours = ChronoUnit.HOURS.between(localTime1, localTime2);
        System.out.println("两个时区相差时间(小时):" + hours);

        long minutes = ChronoUnit.MINUTES.between(localTime1, localTime2);
        System.out.println("两个时区相差时间(分钟):" + minutes);
    }
}
