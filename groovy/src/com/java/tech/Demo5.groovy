package com.java.tech

/**
 created by Jason on 2020/5/5
 */

def clos = { println("Hello World") }
clos.call()

def closParam = { param -> println("Hello ${param}") }
closParam.call("Jason")
