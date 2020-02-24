# 设计模式从入门到精通
> 下面我们将重点介绍常用的设计模式 并进行对比 给出实际应用场景和代码实现


## 主要内容介绍
* [单例模式](#Singleton)
  * 懒汉模式(延迟加载)
  * 饿汉模式
  * 静态内部类
  * 注册登记式
* [原型模式](#prototype)
* [委派模式](#delegate)
* [策略模式](#strategy)
* [模版方法模式](#template)
* [适配器模式](#adapter)
* [代理模式](#proxy)
   * 静态代理
   * 动态代理
* [工厂模式](#factory)
   * 简单工厂
   * 抽象工厂
   * 工厂方法
* [装饰器模式](#decorator)
* [观察者模式](#observer)

## 单例模式
> 单例模式是指从系统启动到系统终止只会生成一个实例,保证独一无二
使用场景:配置文件,IOC容器

懒汉模式实现如下:
```java
public class Lazy {
    private static Lazy instance = null;

    private Lazy() {

    }

    public static Lazy getInstance() {
        if (instance == null) {
            instance = new Lazy();
        }
        return instance;
    }
}
```
饿汉模式实现如下:
```java
public class Hungry {

    private static final Hungry instance = new Hungry();

    private Hungry(){

    }

    public static Hungry getInstance(){
        return instance;
    }
}
```

静态内部类实现
```java
public class StaticInner {

    private StaticInner() {
    }

    //定义内部类
    private static class LazyHolder {
        public final static StaticInner instance = new StaticInner();
    }

    public static StaticInner getInstance(){
        return LazyHolder.instance;
    }
}
```

注册登记式实现:
```java

public class BeanFactory {

    private BeanFactory() {

    }

    //考虑多线程的问题
    private static Map<String, Object> ioc = new ConcurrentHashMap<String, Object>();

    public static Object getBean(String className) {
        if (!ioc.containsKey(className)) {
            Object obj = null;
            try {
                //创建一个实例
                obj = Class.forName(className).newInstance();
                ioc.putIfAbsent(className, obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        } else {
            return ioc.get(className);
        }
    }
   //    测试代码
   //    public static void main(String[] args) {
   //        App app = (App) BeanFactory.getBean("com.java.tech.singleton.App");
   //        App app2 = (App) BeanFactory.getBean("com.java.tech.singleton.App");
   //        System.out.println(app == app2);
   //    }
}
```

## 原型模式
> 原型模式是指从一个对象再创建另外一个可定制的对象,而且不需要知道任何创建的细节
使用场景:克隆技术

```java
public abstract class Shape implements Cloneable {

    private String id;
    protected String type;

    abstract void draw();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object clone(){
        Object obj = null;
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
```

```java
public class Rectangle extends Shape {

    public Rectangle() {
        type = "Rectangle";
    }

    @Override
    void draw() {
        System.out.println("Rectangle Draw");
    }
}
```

```java
public class ShapeFactory {

    private static Map<String,Shape> shapeMap = new ConcurrentHashMap<String,Shape>();

    public static Shape getShape(String shapeId){
        return shapeMap.get(shapeId);
    }

    public static Map<String,Shape> getCache() {
        Circle circle = new Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Square square = new Square();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Rectangle rectangle = new Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(), rectangle);

        return shapeMap;
    }

    //测试代码
    public static void main(String[] args) {
        shapeMap = getCache();
        Shape shape = getShape("2");
        System.out.println(shape.getType());
    }

}
```

## 委派模式
> 委派模式:委派的核心是分发 调度和派遣 只关心结果 不关心过程
大多委派模式都是以"delegate"或者"dispatcher"结尾

```java
public interface Executor<T,R> {

    R query(T request);
}
```

```java
public abstract class BaseExecutor implements Executor<String,String>{

}
```
定义具体干活的
```java
public class SimpleExecutor extends BaseExecutor {
    @Override
    public String query(String request) {
        return "Simple Executor is executing:" + request;
    }
}
```

```java
public class BatchExecutor extends BaseExecutor {
    @Override
    public String query(String request) {
        return "Batch Executor is executing:" +request;
    }
}
```

真正的委派类
```java
public class ExecutorDelegate implements Executor<String,String> {

    private final Executor<String,String> executorDelegate;

    public ExecutorDelegate(Executor executorDelegate) {
        this.executorDelegate = executorDelegate;
    }

    @Override
    public String query(String request) {
        return executorDelegate.query(request);
    }
}
```

测试
```java
public class Client {

    public static void main(String[] args) {
        Executor executor = new BatchExecutor();
        ExecutorDelegate delegate = new ExecutorDelegate(executor);
        String result = delegate.query("writing code");
        System.out.println(result);
    }
```
## 策略模式
> 策略模式是让用户选择固有的算法 主动权是在用户手上,比如用户在支付时,有多种方式提供给用户进行支付,如支付宝,微信
使用场景:完美的解决了switch/if else

定义策略类
```java
/**
 * created by Jason on 2020/2/24
 *
 * 发送消息可以有多种方式比如短信/email
 *
 * 策略类 定义多种固定算法实现
 */
public interface CommonSendService<T extends BaseRequest, R extends BaseResponse> {

    R sendMessage(T request);
}

```
定义多种具体的实现算法
```java
public class EmailSendServiceImpl implements CommonSendService<EmailRequest, EmailResponse>{
    @Override
    public EmailResponse sendMessage(EmailRequest request) {
        EmailResponse response = new EmailResponse();
        response.setReturnCode("M00");
        response.setMsg(request.getRequestBody());
        return response;
    }
}
```
```java
public class SmsSendServiceImpl implements CommonSendService<SmsRequest, SmsResponse> {
    @Override
    public SmsResponse sendMessage(SmsRequest request) {
        SmsResponse response = new SmsResponse();
        response.setReturnCode("S00");
        response.setMsg(request.getRequestBody());
        return response;
    }
}
```

```java
public enum ChannelType {
    SMS(new SmsSendServiceImpl()),
    EMAIL(new EmailSendServiceImpl());

    private CommonSendService commonSendService;

    ChannelType(CommonSendService commonSendService) {
        this.commonSendService = commonSendService;
    }

    public CommonSendService getCommonSendService(){
        return this.commonSendService;
    }
}
```
```java
public interface MessageSendService{

    BaseResponse sendMessage(ChannelType channelType);
}

```

```java
public class MessageSendServiceImpl implements MessageSendService {
    private BaseRequest baseRequest;

    public MessageSendServiceImpl(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
    }

    @Override
    public BaseResponse sendMessage(ChannelType channelType) {
        return channelType.getCommonSendService().sendMessage(baseRequest);
    }
}
```

测试代码:
```java
public class Client {
    //测试代码
    public static void main(String[] args) {
        BaseRequest smsRequest = new SmsRequest();
        smsRequest.setRequestBody(UUID.randomUUID().toString());
        MessageSendService messageSendService = new MessageSendServiceImpl(smsRequest);

        //用户选择用短信发送
        BaseResponse response = messageSendService.sendMessage(ChannelType.SMS);
        System.out.println(response);
    }
}
```

## 模版方法模式
> 模版模式侧重的不是选择,对于用户你没得选择,你必须按照固有的骨架算法去实现
使用场景:常见的模版模式的实现有JdbcTemplate

模版模式的例子非常之多,今天我们来写一个比较有实战性的例子
背景: 我们正常的业务其实都离不开增删改查,那对于业务层可能会出现相同的代码 比如参数检查 业务处理
失败处理 以及日志处理等类似的操作，比如对于用户查询 用户注册都会出现类似的行为.

## 适配器模式
> 适配器模式是把一个接口变成客户端期望的接口 类似转接头的概念
背景:比如早期开发的系统登录使用用户名和密码才能进行登录，随着业务系统的迭代 我们同时支持微信 微博
扫码登录的功能,那么之前暴露的登录已经公布出去了,如果想要进行切换需要考虑适配器模式

下面我们来实现适配器模式
最开始的系统使用用户名和密码登录设计如下:
```java
public interface LoginService {

    LoginResponse login(LoginRequest loginRequest);
}
```
登录实现类:

```java
public class LoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();
        response.setMessage("使用用户名和密码登录系统,用户名=" + loginRequest.getUsername());
        response.setCode("200");
        return response;
    }
}
```
测试类:
```java
public class Client {

    public static void main(String[] args) {
        LoginService loginService = new LoginServiceImpl();

        LoginRequest request = new LoginRequest();
        request.setUsername("Peter");
        LoginResponse response = loginService.login(request);
        System.out.println(response);
    }
}
```
但是随着的后续开发 需要支持微信 微博等登录方式,但又不能改变原来接口 对于用户应该是无感的

单独定义微信的登录接口:
```java
public interface WechatLogin {

    WechatResponse loginWechat(WechatLoginRequest wechatLoginRequest);
}
```
单独的微信业务实现:
```java
public class WeiboLoginServiceImpl implements WeiboLogin {
    @Override
    public WeiboResponse loginWeibo(WeiboRequest weiboRequest) {
        WeiboResponse response = new WeiboResponse();
        response.setCode("weibo");
        response.setMessage("使用微博登录系统,用户名="+ weiboRequest.getUsername());
        return response;
    }
}
```

定义微信登录的适配器:
```java
public class WechatLoginAdapter extends WechatLoginServiceImpl implements LoginService {
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        WechatLoginRequest request = new WechatLoginRequest();
        request.setUsername(loginRequest.getUsername());
        return loginWechat(request);
    }
}
```
适配器有一个非常好的优势就是如果有业务需要修改 比如我需要修改微信登录的业务 只需要修改WechatLoginServiceImpl
不需要去动适配类WechatLoginAdapter的代码,很好的解决了代码的解耦问题

测试代码
```java
public class Client {

    public static void main(String[] args) {
        LoginService loginService = new LoginServiceImpl();

        LoginRequest request = new LoginRequest();
        request.setUsername("Peter");
        LoginResponse response = loginService.login(request);
        System.out.println(response);

        //WechatLoginAdapter相当于转接头
        LoginService wechatLogin = new WechatLoginAdapter();
        LoginResponse wechatResponse = wechatLogin.login(request);
        System.out.println(wechatResponse);
    }
}
```

## 代理模式
> 代理模式分为静态代理和动态代理
使用场景:AOP,拦截器,中介

### 静态代理
定义一个接口
```java
public interface House {
    void render();
}
```

定义接口实现类
```java
public class Houser implements House {
    @Override
    public void render() {
        System.out.println("您好,我是房东,房子目前委托给中介");
    }
}
```

定义代理类
```java
public class HouseAgent implements House {

    private Houser houser;

    public HouseAgent(Houser houser) {
        this.houser = houser;
    }

    @Override
    public void render() {
        houser.render();
    }
}
```
测试类
```java
public class Client {
    public static void main(String[] args) {
        //租客来租房 直接找到来中介
        Houser fangdong = new Houser();

        House houseRender = new HouseAgent(fangdong);
        houseRender.render();
    }
}
```

### 动态代理
要实现动态代理首先需要实现InvocationHandler
```java
public class DynamicProxy implements InvocationHandler {

    //被代理的对象
    private Object target;

    public Object getInstance(Object obj){
        this.target = obj;
        Class<?> clazz = obj.getClass();
        //字节码重组
        return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target,args);
    }
}
```

测试类:
```java
public class Client {
    public static void main(String[] args) {
          //构建房东
          House house = (House) new DynamicProxy().getInstance(new Houser());
          house.render();

          //构建租客
          House render =(House) new DynamicProxy().getInstance(new Render());
          render.render();
    }
}
```

## 工厂模式
### 简单工厂
> 简单工厂又称静态工厂,由工厂类根据传入的参数,动态创建产品类

定义一个静态工厂类,根据传入的参数来创建对象
```java
public class SimpleCarFactory {

    public static Object getCar(String carName){
        if("BMW".equals(carName)){
            return new BMW();
        }else if("Bens".equals(carName)){
            return new Bens();
        }else {
            return new Audi();
        }
    }
}
```
### 抽象工厂
> 抽象工厂的作用就是让用户按照自己的需要 去工厂类获取

下面定义抽象工厂:
定义抽象工厂类:
```java
public interface CarFactory {

    BMWCar getBMW();

    BensCar getBens();
}
```

工厂的默认实现:
```java
public class DefaultCarFactory implements CarFactory {
    @Override
    public BMWCar getBMW() {
        return new BMWCarImpl();
    }

    @Override
    public BensCar getBens() {
        return new BensCarImpl();
    }
}
```

测试类:
```java
public class Client {

    public static void main(String[] args) {
        CarFactory factory = new DefaultCarFactory();
        BensCar bensCar =  factory.getBens();
        bensCar.createBens();
    }
}
```

### 工厂方法
> 工厂方法实际就是工厂的工厂

```java
public interface ICar {

    void running();
}
```

```java
public interface CarFactory {
    ICar createCar();
}
```

```java
public class BMWFactory implements CarFactory {
    @Override
    public ICar createCar() {
        return new BMWCar();
    }
}
```
测试类
```java
public class Client {

    public static void main(String[] args) {
        //创建宝马车
        CarFactory carFactory = new BMWFactory();

        //根据工厂类获取宝马车对象
        ICar bmwCar = carFactory.createCar();
        bmwCar.running();

    }
}
```

## 装饰器模式
> 装饰器模式是为了实现类在不修改原始类的基础上进行动态的覆盖或者增加方法,该实现保持跟原有类的层级关系
装饰器模式一般以wrapper或者decorator结尾

下面我们来实现一下:

定义一个接口查询用户信息
```java
public interface UserService {
    UserQueryResponse queryUser(UserQueryRequest request);
}
```
实现接口类:
```java
public class UserServiceImpl implements UserService {
    @Override
    public UserQueryResponse queryUser(UserQueryRequest request) {
        UserQueryResponse response = new UserQueryResponse();
        response.setReturnCode("200");
        response.setUserType("User");
        response.setMsg(request.getRequestId());
        return response;
    }
}
```
> 以上是一个标准的业务实现,但是如果有一天需求变了需要在查询用户之前增加用户额度查询正常情况肯定只是加一个方法
这样的就会增加系统的复杂性,对业务的耦合度比较高

接下来我们使用装饰器模式来实现 提供系统的解耦能力

1.定义装饰器类
```java
public abstract class UserServiceWrapper implements UserService {

    protected UserService userService;
}
```

2.实现装饰器类
```java
public class CheckUserLimiter extends UserServiceWrapper {
    //构造
    public CheckUserLimiter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserQueryResponse queryUser(UserQueryRequest request) {
        //查询用户之前进行查询用户额度
        checkUserLimiter(request);
        return userService.queryUser(request);
    }

    private void checkUserLimiter(UserQueryRequest request) {
        System.out.println("检查用户额度信息,用户名=" + request.getUserId());
    }
}

```
3.测试类
```java
public class Bootstrap {

    public static void main(String[] args) {
        UserService userService = new CheckUserLimiter(new UserServiceImpl());
        UserQueryRequest request = new UserQueryRequest();
        request.setUserId("Peter");
        request.setRequestId(UUID.randomUUID().toString());
        UserQueryResponse userQueryResponse =  userService.queryUser(request);
        System.out.println(userQueryResponse);
    }
}
```

## 观察者模式
> 观察者模式实际应用场景很多比如订阅发布 服务监听

定义一个任务
```java
public interface Task {

    void add(TaskObserver observer);

    void remove(TaskObserver observer);

    void notifyMessage();
}
```

实现类:
```java
public class Manager implements Task {

    private List<TaskObserver> observerList = new ArrayList<>();
    private String message;

    @Override
    public void add(TaskObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void remove(TaskObserver observer) {
        int index = observerList.indexOf(observer);
        if (index > 0) {
            observerList.remove(observer);
        }
    }

    @Override
    public void notifyMessage() {
        for (int i = 0; i < observerList.size(); i++) {
            TaskObserver observer = observerList.get(i);
            observer.update(message);
        }
    }

    public void assignTask(String taskName) {
        this.message = taskName;
        System.out.println("接到任务:" + taskName);
        this.notifyMessage();
    }
}
```
定义观察者角色:
```java
public interface TaskObserver {

    void update(String message);
}
```

具体实现类
```java
public class WorkA implements TaskObserver {
    private Manager manager;
    private String name;

    public WorkA(Manager manager, String name) {
        this.manager = manager;
        this.name = name;
        manager.add(this);
    }

    @Override
    public void update(String message) {
        System.out.println(name + "已收到" + message + "任务");
    }
}
```

```java
public class WorkB implements TaskObserver {

    private Manager manager;
    private String name;

    public WorkB(Manager manager, String name) {
        this.manager = manager;
        this.name = name;
        manager.add(this);
    }

    @Override
    public void update(String message) {
        System.out.println(name + "已收到" + message + "任务");
    }
}
```
测试类:
```java
public class Client {
    public static void main(String[] args) {
        Manager manager = new Manager();
        new WorkA(manager,"张三");
        new WorkB(manager,"李四");
        manager.assignTask("盖房子");
    }
}
```