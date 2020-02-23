package com.java.tech.streams;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * created by Jason on 2020/2/23
 */
public class Streams5 {
    public static void main(String[] args) {
        Map<Integer, String> maps = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            maps.putIfAbsent(i, "hello" + i);
        }

        //遍历map并打印出来
        maps.forEach((key, value) -> System.out.println(key + "=" + value));

        //如果key存在
        maps.computeIfPresent(4, (key, value) -> value + key);
        String result = maps.get(4);
        System.out.println(result);

        //如果key不存在
        maps.computeIfAbsent(101, value -> "test");
        String absentResult = maps.get(101);
        System.out.println(absentResult);

        maps.remove(5);
        String removeResult = maps.get(5);

        //如果值为空可以直接设置默认值 Optional可以有效的避免了NullPointException问题
        Optional<String> optional = Optional.ofNullable(removeResult);
        System.out.println(optional.isPresent() ? optional.get() : optional.orElse("Jason"));

        //设置默认值
        String notFoundResult = maps.getOrDefault(5, "can found the key");
        System.out.println(notFoundResult);

        //merge
        maps.merge(10, "nihao", (oldValue, newValue) -> oldValue.concat(newValue));
        String mergeResult = maps.get(10);
        System.out.println(mergeResult);
    }
}
