package com.java.tech.optionals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * created by Jason on 2020/2/22
 * Optional用于解决NullPointException的问题
 */
public class Optional1 {

    public static void main(String[] args) {
        List<String> dataList = Arrays.asList("Apple","Iphone");
        Optional<List<String>> optional = Optional.of(dataList);

        //如果存在就打印出来
        optional.ifPresent(System.out::println);

        System.out.println(optional.isPresent());//判断是否存在
        System.out.println(optional.get());//获取值
        System.out.println(optional.orElse(Arrays.asList("HuaWei")));

        optional.ifPresent(s->
                System.out.println(s.get(1)));
    }
}
