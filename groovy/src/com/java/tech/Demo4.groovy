package com.java.tech

/**
 created by Jason on 2020/4/19
 */
//闭包
def result = { println "Hello" }
result.call()


def message = { name -> println "Hello,${name}" }
message.call("Jason")

def msg = { memo -> println "Hello, ${memo}" }
msg.call("World")

3.times {
    println it
}

def userMap = ["001": "zhangsan", "002": "lisi"]
userMap.each {
    println(it)
}


