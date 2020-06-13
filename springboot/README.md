# SpringBoot实战

## 主要内容介绍
* [什么是springboot](#springboot)
* [springboot入门](#hello-springboot)
* [springboot自动配置](#autoConfiguration)
* [如何自定义一个starter](#starter)
* [springboot服务器部署](#deployment)

## 项目模块介绍
- springboot-demo:重点介绍springboot的基本使用方法,常见的功能使用
- springboot-starter:如何自定义一个starter

## 什么是springboot
> Spring Boot是由Pivota团队提供的全新框架,其设计目的是用来简化Spring应用初始搭建以及开发过程,
该框架使用了特定的方式来进行配置,从而使开发人员不再需要定义样板化的配置.

SpringBoot一共提供四大核心功能:
- 自动配置
- 起步依赖
- 命令行界面
- Actuator

## springboot入门
> 下面我们来简单快速的搭建springboot入门项目

1.引入maven相关依赖
```shell script
  <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.6.RELEASE</version>
        <relativePath/>
  </parent>

  <dependencies>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter</artifactId>
         </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-test</artifactId>
             <scope>test</scope>
             <exclusions>
                 <exclusion>
                     <groupId>org.junit.vintage</groupId>
                     <artifactId>junit-vintage-engine</artifactId>
                 </exclusion>
             </exclusions>
         </dependency>
     </dependencies>
 
     <build>
         <plugins>
             <plugin>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-maven-plugin</artifactId>
             </plugin>
         </plugins>
     </build>
```

2.编写启动类
```shell script
@SpringBootApplication
public class CoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }
}
```
3.也可以通过https://start.spring.io/, 输入相关信息自动生成springboot项目

## springboot自动配置
在springboot启动中使用了@SpringBootApplication注解
```shell script
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
  
}
```

这个注解其实包含三个比较重要的注解
- @SpringBootConfiguration: 实际里面是@Configuration,表明该类使用spring基于Java配置
- @EnableAutoConfiguration: 自动配置的核心
- @ComponentScan:启动组件扫描

接下来我们来重点看一下@EnableAutoConfiguration:
```shell script
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
  
}
```
其实这个类中@Import(AutoConfigurationImportSelector.class)中AutoConfigurationSelector真正实现了自动装配:
在AutoConfigurationImporterSelector类中会有getCandidateConfigurations会去扫描META-INF/spring.factories
```shell script
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
				getBeanClassLoader());
		Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
				+ "are using a custom packaging, make sure that file is correct.");
		return configurations;
	}
```
## 如何自定义一个starter
下面我们来重点介绍一下如何自定义一个starter
1.先定义autoconfigure, 比如定义acme-spring-boot-autoconfigure模块
引入相关依赖
```shell script
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

2.分别定义HelloProperties,HelloService,HelloConfiguration
```shell script
@ConfigurationProperties(prefix = "acme")
public class HelloProperties {

    private String name;
    private int age;
    private String address;
}
```

```shell script
public class HelloService {

    private String name;
    private int age;
    private String address;

    public HelloService(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String sayHello(String name) {
        return "Hello " + name;
    }

    public String getUseInfo() {
        return name + "=" + age + "=" + address;
    }
}
```

```shell script
@Configuration
@EnableConfigurationProperties(HelloProperties.class)
public class HelloConfiguration {

    private final HelloProperties properties;

    public HelloConfiguration(HelloProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public HelloService getHelloService(){
        return new HelloService(properties.getName(),properties.getAge(),properties.getAddress());
    }
}
```
3.定义acme-spring-boot-starter,在该项目中加入acme-spring-boot-autoconfigure依赖
```shell script
  <dependency>
            <groupId>com.java.tech</groupId>
            <artifactId>acme-spring-boot-autoconfigue</artifactId>
            <version>1.0-SNAPSHOT</version>
  </dependency>
```

4.分别对acme-spring-boot-autoconfigue,acme-spring-boot-starter进行打包或者执行
```shell script
mvn clean package
```

5.在springboot-demo中加入acme-spring-boot-starter的依赖
```shell script
 <dependency>
            <groupId>com.java.tech</groupId>
            <artifactId>acme-spring-boot-starter</artifactId>
            <version>1.0-SNAPSHOT</version>
  </dependency>
```

6.在spring-demo中在application.properties中添加如下内容:
```shell script
acme.name=Jason
acme.age=20
acme.address=wuhan
```

7.新建UserController
```shell script
@RestController
public class UserController {

    @Autowired
    private HelloService helloService;

    @RequestMapping(value = "/getUser" , method = RequestMethod.GET)
    public String getUserInfo(){
       return helloService.getUseInfo();
    }
}
```

8.启动spring-boot-demo项目可以通过http://localhost:8080/getUser可以完成测试
