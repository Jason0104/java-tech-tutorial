# Gradle从入门到精通

## 主要内容介绍
* [什么是Gradle](#what's gradle)
* [Gradle的安装](#gradle_install)
* [Gradle配置文件介绍](#gradle_file)
* [Gradle的常用命令](#gradle_command)
* [Gradle实战](#gradle_practice)
* [Gradle案例项目](#gradle_project)

## 什么是Gradle
> Gradle is an open-source build automation tool focused on flexibility and performance. Gradle build scripts are written using a Groovy or Kotlin DSL
通俗的讲Gradle是开源的构建项目工具,跟maven一样但是比maven更加的灵活可扩展,因为gradle可以通过写groovy脚本来完成项目的构建,Maven我们都知道通过xml来配置,gradle只需要通过写groovy脚本
比如在写插件的时候gradle只需要简短的几行代码就可以完成

### Gradle功能特性
- 高度定制化: 定制化插件非常方便
- 构建快速:构建的速度比maven快很多
- 功能强大:能够很好的扩展,比较的灵活

## Gradle的安装
1.gradle的安装需要要有jdk1.8以上,首先要确保自己本地环境jdk已经安装
```shell script
java version "1.8.0_231"
Java(TM) SE Runtime Environment (build 1.8.0_231-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.231-b11, mixed mode)
```
看到以上信息说明JDK是安装成功的

2.去gradle官网下载gradle最新版本,https://gradle.org/releases/ 下载gradle安装版本
通过命令解压
```shell script
tar -zxvf gradle-6.3-bin.zip -d /usr/local/gradle
```

3.配置环境变量,vim ~/.bash_profile文件中添加以下内容
```shell script
export GRADLE_HOME=/workspace/software/gradle-6.3
export PATH=$PATH:GRADLE_HOME/bin
```
执行source ~/.bash_profile命令使其生效

4.通过下面命令查看gradle是否安装成功
```shell script
JasonMacBook-Pro:~ Jason$ gradle -v

------------------------------------------------------------
Gradle 6.1.1
------------------------------------------------------------

Build time:   2020-01-24 22:30:24 UTC
Revision:     a8c3750babb99d1894378073499d6716a1a1fa5d

Kotlin:       1.3.61
Groovy:       2.5.8
Ant:          Apache Ant(TM) version 1.10.7 compiled on September 1 2019
JVM:          1.8.0_231 (Oracle Corporation 25.231-b11)
OS:           Mac OS X 10.14.5 x86_64
```
看到以上信息说明Gradle安装成功

## Gradle配置文件介绍
##### gradle项目会有以下几个文件:
- build.gradle: 描述构建逻辑, 所有的构建逻辑都写在这里
- settings.gradle:描述项目的基本信息,标记根项目和子项目
- gradlew:对gradle可执行命令的包装,屏蔽不同版本的兼容

## Gradle的常用命令

#### gradle项目清理命令
```shell script
gradle clean
```
```shell script
 ./gradlew clean test
```

#### gradle项目构建
```shell script
gradle build
```
以debug模式编译项目
```shell script
 gradle build --debug
```

构建生成分析报告:
```shell script
gradle build --scan
```

```shell script
 ./gradlew build
```

## Gradle实战
> 下面的例子我们来建立多个module项目,来演示一些如何创建gradle项目
**具体项目的例子在gradle-tutorial目录下面**

1.我们先建立gradle-facade项目,用于提供服务接口
新建一个接口MemberFacade
```shell script
public interface MemberFacade {
    String sayHello(String name);
```

2.新建一个新的module用于实现接口gradle-facade-impl
定义实现MemberFacade接口的实现类:
```shell script
public class MemberFacadeImpl implements MemberFacade {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
```
**在该module下的build.gradle文件添加下面依赖**
```shell script
dependencies {
    //依赖本地jar
    compile(project(":gradle-facade"))
    testCompile group: 'junit', name: 'junit', version: '4.12'
}
```
3.完成测试,编写测试类
```shell script
public class MemberFacadeTest {

    @Test
    public void testQueryMember(){
        MemberFacade memberFacade = new MemberFacadeImpl();
        String result = memberFacade.sayHello("Jason");
        System.out.println(result);
    }
}
```

4.接下来我们来建立一个新的module叫web,用springboot来启动项目
首先要在web项目build.gradle添加下面内容
```shell
plugins {
    //添加springboot依赖
    id 'org.springframework.boot' version '2.2.6.RELEASE'
    id 'io.spring.dependency-management' version '1.0.9.RELEASE'
    id 'java'
}

group 'com.java.tech'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    //添加springboot依赖
    implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    testCompile group: 'junit', name: 'junit', version: '4.12'
}
```
5.编写启动类
```shell script
@SpringBootApplication
public class CoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }
}
```

## Gradle案例项目
> 接下来我们会重点介绍build.gradle文件中的具体配置,重点把常见的使用技巧标注出来  

```shell script
//构建脚本 需要添加到build.gradle文件顶端
buildscript {
    //用于定义动态属性
    ext{
        springBootVersion='2.2.6.RELEASE'
    }

    repositories {
        //使用maven的中央仓库
        mavenCentral()
    }

    //注明依赖关系
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

plugins {
    id 'java'
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
}

group 'com.java.tech'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8

//仓库定义
repositories {
    mavenCentral()
}

//依赖关系
dependencies {
    compile('org.springframework.boot:spring-boot-starter-thymeleaf')
    compile('org.springframework.boot:spring-boot-starter-web')
    //父类依赖
    implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation('org.springframework.boot:spring-boot-starter-test') {
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    //本地项目依赖
    compile(project(":gradle-service-impl"))
    testCompile group: 'junit', name: 'junit', version: '4.12'
}
```