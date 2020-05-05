package com.java.tech

/**
 created by Jason on 2020/4/19
 */

def color = [red: "0011", blue: "0022"]
println(color.blue)
println(color.toString())
println(color.toMapString())

def str = "groovy"
println(str.center(8, 'h'))
println(str.padLeft(8, '1'))


def str1 = "Hello Groovy"
def str2 = "Hello"
println(str1 - str2)

println(str[0])
println(str[0..4])

println(str.capitalize())