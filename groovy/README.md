# Groovy从入门到精通

## 主要内容介绍
* [什么是Groovy](#what's groovy)
* [Groovy环境安装](#groovy_instal)
* [常用的操作](#groovy_operation)
* [闭包](#groovy_bibao)

## 什么是Groovy
> Groovy是用于Java虚拟机的一种敏捷的动态语言,它是一种成熟的面向对象的编程语言,既可以用于面向对象编程,又可以用作纯粹的脚本语言。
Groovy是JVM的一个替代语言,使用方式基本与Java类似。

## Groovy环境安装
1.groovy官网下载最新版本https://groovy.apache.org/download.html

2.解压groovy包到指定目录
```shell script
tar -zxvf groovy-2.5.4.tag.gz -C /software/groovy-2.5.4
```

3.添加环境变量, 编辑vim ~/.bash_profile文件
```shell script
export GROOVY_HOME=/software/groovy-2.5.4
export PATH=$PATH:$GROOVY_HOME/bin
```

执行source ~/.bash_profile完成生效

4.验证groovy是否安装成功
```shell script
JasonMacBook-Pro:~ Jason$ groovy -v
Groovy Version: 2.5.4 JVM: 1.8.0_231 Vendor: Oracle Corporation OS: Mac OS X
```
当看到上面信息,说明groovy已安装成功

## 常用的操作  
Groovy的使用与Java很类似,下面我们将以案例的形势来介绍Groovy常见的操作

### JSON解析
```shell script
//Groovy解析Json通过JsonSlurper来解析
def jsonParser = new JsonSlurper()
def obj = jsonParser.parseText('{"mytest":[1,4,8,10,20]}')

println(obj.mytest)

def studentInfo = jsonParser.parseText('{"id":"0108094090","name":"Tom"}')
println("Student ID=" + studentInfo.id + ", Name=" + studentInfo.name)

//通过JsonOutput将Groovy对象序列化为JSON字符串
def output = JsonOutput.toJson('{"mytest":[1,4,8,10,20]}')
println(output)
```

## 闭包
> 闭包是一个短的匿名代码块,它通常跨越几行代码, 一个方法甚至可以将代码块作为参数,它们是匿名的

下面来看一个简单闭包的例子:
```shell script
def clos = {println("Hello World")}
clos.call()
```

闭包也可以包包含形式参数,以使它们更有用
```shell script
def closParam = {param->println("Hello ${param}")}
closParam.call("Jason")
```
