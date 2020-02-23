# Java8学习教程

## 主要内容介绍

* [接口的默认方法](#default-methods-for-interfaces)
* [Lambda表达式](#lambda-expressions)
* [函数式接口](#functional-interfaces)
* [方法与构造函数引用](#method-and-constructor-references)
* [Lambda作用域](#lambda-scopes)
  * [访问局部变量](#accessing-local-variables)
  * [访问对象字段与静态变量](#accessing-fields-and-static-variables)
  * [访问接口的默认方法](#accessing-default-interface-methods)
* [构建函数式接口](#built-in-functional-interfaces)
  * [Predicates接口](#predicates)
  * [Functions接口](#functions)
  * [Suppliers接口](#suppliers)
  * [Consumers接口](#consumers)
  * [Comparators接口](#comparators)
* [Optionals接口](#optionals)
* [Streams接口](#streams)
  * [Filter过滤](#filter)
  * [Sorted排序](#sorted)
  * [Map映射](#map)
  * [Match匹配](#match)
  * [Count统计](#count)
  * [Reduce规约](#reduce)
* [Parallel Streams 并行Stream](#parallel-streams)
  * [Sequential Sort 串行排序](#sequential-sort)
  * [Parallel Sort 并行排序](#parallel-sort)
* [Maps](#maps)
* [Date API](#date-api)
  * [Clock 时钟](#clock)
  * [Timezones 时区](#timezones)
  * [LocalTime 本地时间](#localtime)
  * [LocalDate 本地日期](#localdate)
  * [LocalDateTime 本地日期时间](#localdatetime)

## 接口的默认方法

Java 8允许接口中定义默认实现方法,需要在实现方法前添加"default"关键字

定义格式如下:

```java
public interface MethodIForInterface {
    String sayHello(String name);

    //java8 支持在接口中定义默认方法
    default String handleMessage(String message) {
        return message;
    }
}
```

实现类实现方式如下:
```java
public class DefaultMethodForInterface {
    public static void main(String[] args) {
        MethodIForInterface methodIForInterface = new MethodIForInterface() {
            @Override
            public String sayHello(String name) {
                return "Jason " + name;
            }
        };

        String result = methodIForInterface.sayHello("Chen");
        System.out.println(result);
    }
}
```
## Lambda表达式
Lambda表达式是Java8最重要的特性之一,提高了开发的效率

Java8之前需要排序:
```java
List<String> names = Arrays.asList("apple", "pear", "orange", "banana");

        //Java8之前想对name进行倒排
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("java8之前排序方法:" + names);
```

使用Java8 lambda之后的排序
```java
public class Lambda1 {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("apple", "pear", "orange", "banana");
        //java8 lambda写法更加简洁
        names.sort((a,b)->b.compareTo(a));
        System.out.println("java8之后排序方法:" + names);
    }
}
```

## 函数式接口(Functional Interfaces)
函数式接口是指只包含一个抽象方法的接口，每一个类型的lambda表达式都会被匹配到这个抽象方法
如何定义函数式接口，需要在接口中添加 @FunctionalInterface 注解

函数式接口定义如下:
```java
@FunctionalInterface
public interface Lambda2<T, R> {
    R converter(T req);

}
```

```java
public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        Lambda2<String,Integer> convert =(req ->  Integer.valueOf(req));
        Integer integer = convert.converter("123");
        System.out.println(integer);
    }
}
```

## 方法与构造函数引用
Java8 通过"::"关键字可以对方法与构造方法进行引用
```java
public class StringUtils {
    public static String startWith(String str){
        return String.valueOf(str.charAt(0));
    }
}
```

```java
public class Lambda3 {
    public static void main(String[] args) {
        Lambda2<String,String> methodReference = StringUtils::startWith;
        String result = methodReference.converter("Java");
        System.out.println(result);
    }
}
```

下面我们来看一个更详细的例子

```java
public class Person {

    private String firstName;
    private String lastName;

    public Person() {

    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
```
定义一个PersonFactory:
```java
public interface PersonFactory<P extends Person> {
    P create(String firstName,String lastName);
}
```

```java
public class Lambda4 {
    public static void main(String[] args) {
        //构造PersonFactory
        PersonFactory<Person> personFactory = Person::new;

        Person person = personFactory.create("Tom", "Peter");
        System.out.println(person);

    }
}

```
## Lambda作用域

### 访问本地变量
```java
int num = 10;
Lambda2<Integer, String> converter1 = req -> String.valueOf(req
                + num);
String result = converter1.converter(20);
System.out.println(result);
```

```java
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
```

## 构建函数式接口

### Predicates接口
Predicate是断言接口,接口只有一个参数,返回boolean类型

```java
  String str = "Predicate";
  //Predicate是一个断言接口
  //获得string
  Predicate<String> predicate = s -> s.length() > 0;
  System.out.println(predicate.test(str));//true
  System.out.println(predicate.negate().test("apple"));//false

  Predicate<String> isEmpty = String::isEmpty;
  System.out.println(isEmpty.test(str));
```

### Functions接口
Functions接口有一个参数并且返回一个结果

```java
        //Function可以接收参数并且能够生成结果
        Function<String, Integer> from = Integer::valueOf;
        Function<String, String> end = from.andThen(String::valueOf);
        String result = end.apply("456");
        System.out.println(result);
```

### Suppliers接口
Supplier返回任意一个泛型的值,与Functions不一样,Suppliers不能接收参数
```java
        //Supplier能够根据具体类型生成结果
        Supplier<Person> supplier = Person::new;
        Person person = supplier.get();
        person.setFirstName("Jason");
        person.setLastName("Chen");
        System.out.println(person);

```

### Consumers接口
Consumer 接口表示执行在单个参数上的操作
```java
   //Consumer 能够接收参数并执行
   Consumer<Person> greeting = (p) -> System.out.println("Hello " + p.getFirstName());
   greeting.accept(new Person("Jason", "Chen"));
```

### Comparators接口
```java
        //Comparators
        Person p1 = new Person("Alice", "Chen");
        Person p2 = new Person("Bob", "Wang");
        List<Person> personList = new ArrayList<>();
        personList.add(p1);
        personList.add(p2);

        personList.sort((a, b) -> b.getFirstName().compareTo(a.getFirstName()));
        System.out.println("排序后的结果为:" + personList);
```

## Optionals接口
Optional接口不是一个函数式接口,它更像一个工具类,能够帮助避免NotPointException

下面介绍Optional的使用方式
```java
public class Optional1 {

    public static void main(String[] args) {
        List<String> dataList = Arrays.asList("Apple","Iphone");
        Optional<List<String>> optional = Optional.of(dataList);

        //如果存在就打印出来
        optional.ifPresent(System.out::println);

        System.out.println(optional.isPresent());//判断是否存在
        System.out.println(optional.get());//获取值
        System.out.println(optional.orElse(Arrays.asList("HuaWei")));

        optional.ifPresent(s->
                System.out.println(s.get(1)));
    }
}
```

## Streams接口

> java.util.Stream 能应用在一组元素上一次执行的操作序列,Stream操作分为中间操作或者最终操作两种，
最终操作返回一特定类型的计算结果，而中间操作返回Stream本身，这样你就可以将多个操作串起来。
Stream的创建需要指定一个数据源，比如 java.util.Collection的子类，List或者Set，Map不支持
  
下面我们来看Stream的使用:
```java
 List<String> streamList = new ArrayList<>();
 streamList.add("aa");
 streamList.add("cc");
 streamList.add("dd");
 streamList.add("ff");
 streamList.add("ae");
```

### Filter接口
```java
//filter 打印所有以a开头的字符串
streamList.stream().filter((s -> s.startsWith("a"))).forEach(System.out::println);
```

### Sorted接口
```java
//排序 sort
streamList.stream().sorted().forEach(System.out::println);
```

### Map
> 中间操作map会将元素根据指定的Function接口来依次将元素转成另外的对象，通过map来进行对象转换成其他类型，map返回的Stream类型是根据你map传递进去的函数的返回值决定的

```java
//map用于对象转换
streamList.stream().map(String::toUpperCase).sorted((a,b)->b.compareTo(a)).forEach(System.out::println);
```

### Match匹配
```java
public class Streams2 {

    public static void main(String[] args) {
        List<String> streamList = new ArrayList<>();

        streamList.add("aa");
        streamList.add("cc");
        streamList.add("dd");
        streamList.add("ff");
        streamList.add("ae");

        //查找是否有以c开头的元素 只要有一个元素以c开头则返回true
        boolean isMatched = streamList.stream().anyMatch(s -> s.startsWith("c"));
        System.out.println(isMatched);

        //查找是否有以c开头的元素 必须所有的元素以c开头则返回true
        boolean allMatched = streamList.stream().allMatch(s -> s.startsWith("a"));
        System.out.println(allMatched);

        //只要不符合条件的返回则true
        boolean nonMatched = streamList.stream().noneMatch(s->s.startsWith("z"));
        System.out.println(nonMatched);
    }
}
```

#### Count统计
```java

public class Streams3 {
    public static void main(String[] args) {
        List<String> streamList = new ArrayList<>();

        streamList.add("aa");
        streamList.add("cc");
        streamList.add("da");
        streamList.add("ff");
        streamList.add("aa");

        //统计以a结尾的字符串个数
        long count = streamList.stream().filter(s -> s.endsWith("a")).count();
        System.out.println("统计以a结尾的字符串个数=" + count);
    }
}
```
### Reduce规约
> 这是一个最终操作，允许通过指定的函数来讲stream中的多个元素规约为一个元素，规约后的结果是通过Optional接口表示的：

```java
//reduce
Optional<String> reduced = streamList.stream().sorted().reduce((a, b) -> a + "#" + b);
//如果存在打印出来
reduced.ifPresent(System.out::println);
```

## 并行Stream (Parallel Streams)
```java
    int max = 1000000;
    List<String> randomList = new ArrayList<>(max);
    for (int i = 0; i < max; i++) {
        randomList.add(UUID.randomUUID().toString());
    }
```

### 串行Stream
```java
       long start = System.currentTimeMillis();
        //串行计算
        long count = randomList.stream().count();
        System.out.println(count);

        long end = System.currentTimeMillis();
        System.out.println("const time:" + (end - start) + "ms");
```

### 并行Stream
```java
        long startTime = System.currentTimeMillis();
        //并行计算
        long countNum = maxList.parallelStream().count();
        System.out.println(countNum);

        long endTime = System.currentTimeMillis();
        System.out.println("parallel const time:" + (endTime - startTime) + "ms");
```

## Maps
> Map类型不支持stream，不过Map提供了一些新的有用的方法来处理一些日常任务。

下面我们来看一下Map的使用方法:
```java
public class Streams5 {
    public static void main(String[] args) {
        Map<Integer, String> maps = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            maps.putIfAbsent(i, "hello" + i);
        }

        //遍历map并打印出来
        maps.forEach((key, value) -> System.out.println(key + "=" + value));

        //如果key存在
        maps.computeIfPresent(4, (key, value) -> value + key);
        String result = maps.get(4);
        System.out.println(result);

        //如果key不存在
        maps.computeIfAbsent(101, value -> "test");
        String absentResult = maps.get(101);
        System.out.println(absentResult);

        maps.remove(5);
        String removeResult = maps.get(5);

        //如果值为空可以直接设置默认值 Optional可以有效的避免了NullPointException问题
        Optional<String> optional = Optional.ofNullable(removeResult);
        System.out.println(optional.isPresent() ? optional.get() : optional.orElse("Jason"));
    }
}
```

设置默认值:
```java
 //设置默认值
 String defaultValue = maps.getOrDefault(5, "can found the key");
```

Merge:
```java
    maps.merge(10, "nihao", (oldValue, newValue) -> oldValue.concat(newValue));
    String mergeResult = maps.get(10);
    System.out.println(mergeResult);
```

## Date API
> Java8 提供了很多日期函数,下面我们来看一下如何使用

### Clock时钟
```java
   //Clock
   Clock clock = Clock.systemDefaultZone();
   long millis = clock.millis();
   System.out.println(millis);

   Instant instant = clock.instant();
   Date date = Date.from(instant);
   System.out.println(date);
```

### Timezones时区
```java
   //Timezones
   System.out.println("获得所有的时区" + ZoneId.getAvailableZoneIds());

   ZoneId zone1 = ZoneId.of("Asia/Shanghai");
   ZoneId zone2 = ZoneId.of("Japan");
   System.out.println(zone1.getRules());
   System.out.println(zone2.getRules());
```

### LocalTime 本地时间
```java
//LocaleTime
LocalTime localTime1 = LocalTime.now(zone1);
LocalTime localTime2 = LocalTime.now(zone2);
System.out.println(localTime1.isBefore(localTime2));

//计算两个时区相差时间
long hours = ChronoUnit.HOURS.between(localTime1, localTime2);
System.out.println("两个时区相差时间(小时):" + hours);

long minutes = ChronoUnit.MINUTES.between(localTime1, localTime2);
System.out.println("两个时区相差时间(分钟):" + minutes);
```

### LocalDate
```java
public class LocalDate1 {
    public static void main(String[] args) {
        //获取当天的日期
        LocalDate localDate = LocalDate.now();
        System.out.println("获取当天日期:" + localDate);

        //获取明天的日期
        LocalDate tomorrow = localDate.plusDays(1);
        //下个星期
        LocalDate nextWeek = localDate.plusWeeks(1);
        //下个月
        LocalDate nextMonth = localDate.plusMonths(1);
        System.out.println("获取明天日期:" + tomorrow);
        System.out.println("获取下个星期日期:" + nextWeek);
        System.out.println("获取下个月的日期:" + nextMonth);
    }
}
```

### LocalDateTime
> LocalDateTime 同时表示了时间和日期，LocalDateTime提供了一些能访问具体字段的方法

```java
public class LocalDateTime1 {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.of(2020, Month.FEBRUARY, 23, 15, 50);
        //获取当前日期是星期几
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        System.out.println(dayOfWeek);

        //获取当前输入日期是几月
        Month month = localDateTime.getMonth();
        System.out.println(month);

        //获取分钟
        long minuteDay = localDateTime.getLong(ChronoField.MINUTE_OF_DAY);
        System.out.println(minuteDay);

    }
}
```
  







