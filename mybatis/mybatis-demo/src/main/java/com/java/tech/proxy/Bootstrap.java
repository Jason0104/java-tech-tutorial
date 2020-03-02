package com.java.tech.proxy;

/**
 * created by Jason on 2020/2/29
 */
public class Bootstrap {

    public static void main(String[] args) {
//        ServiceProxy proxy = new ServiceProxy();
//        HelloService helloService =  (HelloService) proxy.bind(new HelloServiceImpl());
//        helloService.echo("Jason");

        CglibServiceProxy proxy = new CglibServiceProxy();
        HelloService helloService = (HelloService) proxy.getInstance(new HelloServiceImpl());
        helloService.echo("cglib");

    }
}
