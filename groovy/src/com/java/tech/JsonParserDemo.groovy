package com.java.tech

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 created by Jason on 2020/5/5
 */

//Groovy解析Json通过JsonSlurper来解析
def jsonParser = new JsonSlurper()
def obj = jsonParser.parseText('{"mytest":[1,4,8,10,20]}')

println(obj.mytest)

def studentInfo = jsonParser.parseText('{"id":"0108094090","name":"Tom"}')
println("Student ID=" + studentInfo.id + ", Name=" + studentInfo.name)

//通过JsonOutput将Groovy对象序列化为JSON字符串
def output = JsonOutput.toJson('{"mytest":[1,4,8,10,20]}')
println(output)