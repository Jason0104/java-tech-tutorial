package com.java.tech.lambda;

/**
 * created by Jason on 2020/2/22
 */
public class Lambda5 {
    static int staticNum;
    int outerNum;

    void lambdaAccess() {
        int num = 10;
        Lambda2<Integer, String> converter1 = req -> String.valueOf(req
                + num);
        String result = converter1.converter(20);
        System.out.println(result);


        Lambda2<Integer, String> converter2 = req -> {
            outerNum = 10;
            return String.valueOf(req);
        };

        String result2 = converter2.converter(8);
        System.out.println(result2);
    }

    public static void main(String[] args) {
        Lambda5 lambda5 = new Lambda5();
        lambda5.lambdaAccess();
    }
}
