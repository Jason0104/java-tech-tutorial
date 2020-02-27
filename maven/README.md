# Maven实战


## 主要内容介绍
* [什么是Maven](#maven)
  * 什么是Maven
  * Maven安装与配置
  * Maven坐标
* [Maven依赖](#maven_dependency)
* [Maven依赖调解原则](#maven_prins)
* [Maven生命周期](#maven_lifestyle)
* [Maven常用命令](#maven_commands)
* [Maven插件介绍](#maven_middleware)
* [Maven仓库](#maven_repo)
* [聚合和继承](#maven_inherit)
* [Jenkins环境搭建](#jenkins)


## 什么是Maven
### 什么是Maven
> Maven翻译为知识的积累,也可以翻译为专家或者内行,是跨平台的项目管理工具,主要服务于Java
平台的项目构建,依赖管理和项目信息管理.

#### Maven的安装与配置(本文主要针对mac用户)
1.maven的安装需要依赖JDK,首先需要确保本地JDK是安装成功的
通过输入下面命令可验证JDK是否安装成功:
```shell script
JasonMacBook-Pro:~ Jason$ java -version
java version "1.8.0_231"
Java(TM) SE Runtime Environment (build 1.8.0_231-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.231-b11, mixed mode)
```
获得以上信息说明JDK安装成功

2.Maven官网下载Maven安装包,地址http://maven.apache.org/ 下载apache-maven-3.6.3-bin.tar.gz
```shell script
tar -zxvf apache-maven-3.6.3-bin.tar.gz -C /usr/local/; //解压到指定的目录
```
3.添加环境变量
vim ~/.bash_profile添加以下内容:
```shell script
export M2_HOME=/usr/local/maven
export PATH=$PATH:$M2_HOME/bin
```
执行source .bash_profile命令完成生效

4.验证Maven是否安装成功:
```shell script
Jason-Pro:~ Jason$ mvn -v
Apache Maven 3.6.2 (40f52333136460af0dc0d7232c0dc0bcf0d9e117; 2019-08-27T23:06:16+08:00)
Maven home: /usr/local/maven
Java version: 1.8.0_231, vendor: Oracle Corporation, runtime: /Library/Java/JavaVirtualMachines/jdk1.8.0_231.jdk/Contents/Home/jre
Default locale: zh_CN, platform encoding: UTF-8
OS name: "mac os x", version: "10.14.5", arch: "x86_64", family: "mac"
```
说明已经安装成功了

#### Maven坐标
> Maven坐标为任何一个构件明确定义唯一性,通过groupId,artifactId,version,packaging,
classifier

下面先看一组坐标的定义
```shell script
<dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <packaging>jar</packaging>
</dependency>
```
下面解释以下各坐标元素的定义:
- groupId: 定义当前Maven项目所属的实际项目,一般命名都是反向一一对应 代表一种组织 机构和公司
比如groupId定义为:com.tech.java
- artifactId:实际项目的名称
- version: 指当前项目的所处版本
- packaging: 主要定义Maven项目的打包方式
- classifier:帮助定义构件输出的一些附属构件,该构件与主构件对应的

Maven坐标groupId,artifactId,version是必须的,packaging是可选的(默认为jar),
classifier是不能直接定义的。

## Maven依赖
Maven有以下几种依赖范围:
- compile: 编译依赖范围,如果没有指定的话就会默认该依赖范围,适用于所有阶段会随着项目一起发布
- test: 测试依赖范围,在测试和运行的时候都需要该依赖,典型的例子就是junit.jar,不会随项目发布
- provided:已提供依赖范围,对于编译和测试classpath有效,但是运行时无效 典型例子是servlet-api.jar
- runtime:运行时依赖范围,对于测试和运行classpath有效,但在编译主代码时无效 典型例子是JDBC驱动
- import:导入依赖范围, 用于解决单继承的问题 代码非常简洁,只能用在dependencyManagement里面,并且type=pom的dependency
- system: 系统依赖范围,本地一些jar 例如短信jar,maven不会在repositoy中查找它

比如项目中想引入外部的短信sms.jar
```shell script
<dependency>
    <groupId>sms-jar</groupId>   
    <artifactId>sms-jar</artifactId> 
    <version>1.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/lib/*.jar</systemPath> //jar路径
</dependency>
```
## Maven依赖调解的原则
由于传递依赖我们有的时候会碰到maven jar冲突的问题下面介绍依赖调解的原则
- 最短路径原则(直接依赖 > 传递依赖)
- 声明优先原则(依赖路径的长度相等的情况下,采用声明优先的原则)
- 排除原则(传递依赖冲突时,可以在不需要的jar的传递依赖中声明排除,使用<exclusion>来排除依赖)
- 版本锁定原则(在配置文件pom.xml中先声明使用哪个版本的jar,声明后其他版本一律不依赖)
- 可选依赖,通过<optional>标签声明可选依赖,表示该jar只会对该项目产生影响,该依赖不会传递

## Maven生命周期
Maven生命周期主要为clean,default,site三大部分.
- clean生命周期的目的是清理项目
- default生命周期的目的是构建项目
- site生命周期是建立和发布项目站点

### clean生命周期
- pre-clean 执行一些清理前需要完成的工作
- clean 清理上一次构建生成的文件
- post-clean 执行一些清理后需要完成的工作

### default生命周期
包含以下阶段:
- validate
- initialize
- generate-sources
- process-sources
- generate-resources
- process-resources
- compile 编译项目的主源码
- process-classes
- generate-test-sources
- process-test-sources
- generate-test-resource
- process-test-resouces
- test-compile
- process-test-classes
- test 使用单元测试框架运行测试,测试代码不会被打包或部署
- prepare-package
- package 接受编译好的代码,打包成可发布的格式 如JAR,WAR
- pre-integration-test
- integration-test
- post-integration-test
- verify
- install 将包安装到Maven本地仓库,供本地其他Maven项目使用
- deploy  将最终的包复制到远程仓库,供其他开发任意和Maven项目使用

### site生命周期
- pre-site 执行一些在生成项目站点之前需要完成的工作
- site 生成项目站点文档
- post-site 执行一些在生成项目站点之后需要完成的工作
- site-deploy 将生成的项目站点发布到服务器上

## Maven常用命令
列出项目所有依赖
```shell script
mvn dependency:list
```
分析编译主代码和测试代码需要用到的依赖
```shell script
mvn dependency:analyze
```
打印依赖的树形结构
```shell script
mvn dependency:tree
```
打印依赖的树形结构信息最全
```shell script
mvn dependency:tree -Dverbose
```
打印信息需要包含指定的jar
```shell script
mvn dependency:tree -Dverbose -Dincludes=junit:junit
```
打印信息指定过滤的jar
```shell script
mvn dependency:tree -Dverbose -Dexcludes=juit:junit
```
项目打包跳过测试
```shell script
mvn clean package -DskipTests //跳过单元测试
mvn clean package -Dmaven.test.skip=true
```
强制让maven检查更新
```shell script
mvn clean install -U;
```
多线程并行编译
```shell script
mvn clean -Dmaven.compile.fork=true
```
多线程并行打包
```shell script
mvn clean -Dmaven.package.fork=true
```
多线程并行安装jar到本地
```shell script
mvn clean -Dmaven.install.fork=true
```
把jar发布到私服
```shell script
mvn deploy
mvn deploy -U ;//更新最新jar包
```

maven多线程构建
```shell script
mvn -T 4 clean install
```

```shell script
mvn -T 1C clean install
```
参数说明:
- -T 4是直接指定4个线程
- -T 1C表示CPU线程的倍数

以Debug模式编译项目
```shell script
mvn clean compile -X
```

## Maven插件介绍
在介绍maven插件之前先解释几个概念
- Phase: 可以理解为任务单元,lifecycle是总任务,phase就是总任务分出来的一个个子任务 执行phase实际执行的是goal,如果一个phase没有绑定，那么phase就不会被执行,一个lifecycle可以包含任意个phase,phase的执行是按顺序的，一个phase可以绑定很多个goal,至少为一个,没有goal的phase是没有意义的
- Goal:  这是执行任务的最小单元, 一个goal是独立的，它可以被绑定到多个phase中去，也可以一个phase都没有。如果一个goal没有被绑定到任何一个lifecycle，它仍然可以直接被调用，而不是被lifecycle调用
- Mojo: mojo是具体做事情的 可以简单理解为mojo为goal的实现类 它继承于AbstractMojo

Maven插件官网: 
- https://maven.apache.org/plugins/
- http://www.mojohaus.org/plugins.html

下面我们介绍如何编写插件
> 背景:写一个插件用于统计某个具体项目的文件个数和代码行数

1.引入相关依赖
```shell script
       <dependency>
            <groupId>org.apache.maven</groupId>
            <artifactId>maven-plugin-api</artifactId>
            <version>3.5.0</version>
       </dependency>
       <dependency>
            <groupId>org.apache.maven.plugin-tools</groupId>
            <artifactId>maven-plugin-annotations</artifactId>
            <version>3.5</version>
       </dependency>
```

这里需要注意在上面pom.xml设置packaging类型为maven-plugin
```shell script
    <packaging>maven-plugin</packaging>
```

2.定义插件类需要继承AbstractMojo
```shell script
@Mojo(name = "autoScanCount", requiresProject = false, defaultPhase = LifecyclePhase.PACKAGE)
public class AutoScanCode extends AbstractMojo {

    //存放扫描的文件
    private static List<String> fileList = new ArrayList<>();

    @Parameter(name = "currentBaseDir", defaultValue = "/workspace")
    private String currentBaseDir;

    @Parameter(name = "suffix", defaultValue = ".java")
    private String suffix;

    //代码行数
    private int lines = 0;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        autoScanDir(currentBaseDir);
        System.out.println("自动扫描代码插件总共扫描文件个数:" + fileList.size());
        System.out.println("自动扫描代码插件总共扫描代码行数:" + lines);
    }

    //扫描文件目录
    private void autoScanDir(String baseDir) {
        File files = new File(baseDir);

        for (File file : files.listFiles()) {
            //如果是目录 需要进行递归调用
            if (file.isDirectory()) {
                autoScanDir(file.getAbsolutePath());
            } else {
                if (file.getName().endsWith(suffix)) {
                    fileList.add(file.getName());
                    lines += countLine(file);
                }
            }
        }
    }

    //统计文件行数
    private int countLine(File file) {
        int codeLine = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                reader.readLine();
                codeLine++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeLine;
    }
}
```
3.将项目本地安装到本地仓库,将项目依赖引入到其他项目
```shell script
 <dependencies>
        <dependency>
            <groupId>com.java.tech</groupId>
            <artifactId>auto-scan-code</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

    //这里需要绑定
    <build>
        <plugins>
            <plugin>
                <groupId>com.java.tech</groupId>
                <artifactId>auto-scan-code</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                    //指定路径
                    <currentBaseDir>/workspace/gitrepo/java-tech-tutorial</currentBaseDir>
                    <suffix>.java</suffix>
                </configuration>
                <executions>
                    <execution>
                        <phase>clean</phase>
                        <goals>
                            <goal>autoScanCount</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
4.在当前项目执行mvn clean命令即可生效该插件,更多代码可参考项目demo

## Maven仓库
### 本地仓库
> 本地仓库用于存放依赖文件的目录,默认情况本地仓库位于用户目录下的.m2/repository目录下

有的时候需要修改仓库地址:
vim ~/.m2/repository

```shell script
<localRepository>/usr/local/repo</localRepository>
```
Maven提倡约定优于配置,也叫COC(Conversion Over Configuration)
Maven加载jar的顺序:~/.m2/setting.xml->maven/conf/setting.xml
- 先去.m2/setting.xml下寻找,然后在去conf/setting.xml
- 仓库顺序:本地仓库->私服->maven中央仓库

### 远程仓库(nexus)
> 远程仓库主要用于解决jar依赖的问题,还有因为网络下载慢,很多公司都会选择搭建自己内部的私服服务器,方便下载构件
首先默认会在本地仓库寻找构件,如果本地仓库没有会去私服下载到本地仓库,如果私服还没有的话,会去maven中央仓库下载.

市面上有比较多的仓库管理软件
- Sonatype Nexus
- Jarvana
- MVNrepository

我们这里重点介绍一下Nexus的使用
Nexus私服的优势
- 节省带宽
- 加速maven构建
- 部署第三方构件
- 降低中央仓库的压力

#### Nexus仓库类型:
- group(仓库组)
- hosted(宿主):管理本地仓库
- proxy(代理)
- virtual(虚拟)

#### 仓库的用途
- Codehaus Snapshots:用来管理Codehaus Snapshot仓库的快照构件
- Apache Snapshots:用来管理Apache Snapshot仓库快照构件
- Central:该仓库为Maven中央仓库,用于下载和缓存中央仓库中的发布版本构件
- Release:策略为Release的宿主类型仓库,用于部署组织内部结构的发布版本构件
- Snapshots:策略为Snapshot的宿主类型仓库,用来部署组织内部结构的快照版本构件

#### nexus安装与启动
1.下载nexus
2.解压nexus文件到指定的目录
```shell script
tar -zxvf nexus-public-release-3.21.0-05.tar.gz -C /usr/local/nexus
```
3.在/usr/local/nexus/bin/nexus文件中加入:
```shell script
export RUN_AS_USER=root
```
4.运行./nexus start启动nexus

### 关于Nexus下sonatype-work文件目录介绍:
- conf: 存放日志 私服权限等配置文件
- indexer:存放索引文件,私服页面看到的jar都是通过索引去storage搜索的
- storage:jar包存储文件
- logs: 存放日志文件

## 聚合和继承
> Maven的聚合特性能够把项目的各个模块聚合在一起构建,而Maven的继承特性则能帮助抽取各模块相同的依赖和插件等配置
在简化POM的同时,还能促进各个模块配置的一致性

聚合就是把项目的各个模块聚合在一起进行构建,比如本项目就是一个聚合
```shell script
    <modules>
        <module>java8</module>
        <module>design-pattern</module>
        <module>mybatis</module>
        <module>maven</module>
    </modules>
```

继承就是把一些通用的jar定义在父pom.xml中

#### dependencyManagement与dependency区别
- dependencyManagement只能出现在父pom中 是一种统一声明,统一版本号,子pom根据按需引用,是一种预定义
- dependency只引用jar依赖的

#### distributionManagement
- 用于指定maven分发构件的位置
- maven是区别对待release版本的构件和snapshot版本的构件
  * release版本比较稳定,版本无法覆盖
  * snapshot为开发过程的版本,实时但不稳定 版本可以被覆盖

## Jenkins环境搭建
> Jenkins是持续集成平台统称CI(Continous Intergation)

搭建步骤如下:
1.下载jenkins.war包,地址: https://jenkins.io/zh/download/

2.修改jenkins的目录,打开tomcat的bin目录,编辑catalina.sh文件
在# OS specific support.  $var _must_ be set to either true or false添加下面内容:
```shell script
JENKINS_HOME=/data/jenkins
```
3.编辑profile文件 vim /etc/profile将JENKINS_HOME加入到环境变量中
```shell script
export JENKINS_HOME=/data/jenkins
```
执行source /etc/profile命令让配置生效

4.在/usr/local/tomcat/bin/catalina.sh下面加上下面内容:
```shell script
CATALINA_OPTS="$CATALINA_OPTS -server -Djava.awt.headless=true -Xms1g -Xmx1g -Xss256k  -XX:NewSize=600m -XX:MaxNewSize=600m -XX:SurvivorRatio=8 -XX:+UseParNewGC -XX:ParallelGCThreads=4 -XX:MaxTenuringThreshold=15 -XX:+UseConcMarkSweepGC -XX:+DisableExplicitGC -XX:+UseCMSInitiatingOccupancyOnly -XX:+ScavengeBeforeFullGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:CMSFullGCsBeforeCompaction=9 -XX:CMSInitiatingOccupancyFraction=80 -XX:+CMSClassUnloadingEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:-ReduceInitialCardMarks -XX:+CMSPermGenSweepingEnabled -XX:CMSInitiatingPermOccupancyFraction=80 -XX:+ExplicitGCInvokesConcurrent -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -DJENKINS_HOME=/data/jenkins -Dhudson.model.DirectoryBrowserSupport.CSP="
CATALINA_OPTS="$CATALINA_OPTS -Duser.timezone=Asia/Shanghai -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8"
```

5.把jenkins.war放到tomcat目录下的webapps并运行tomcat
```shell script
sh /usr/local/tomcat/bin/startup.sh 
```
此时jenkins已经启动,通过浏览器输入http://localhost:8080进行访问