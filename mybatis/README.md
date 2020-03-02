# Mybatis源码深入分析

## 主要内容介绍
* [什么是Mybatis](#mybatis)
* [Mybatis简单例子](#mybatis-demo)
* [Mybatis基础模块及生命周期](#mybatis-moudles)
* [Mybatis配置的主要内容](#mybatis-config)
* [Mybatis映射器的主要内容和配置](#mybatis-mapping)
* [动态SQL](#mybatis-sql)
* [Mybatis解析及运行原理](#mybatis-running)
* [Mybatis-Spring](#mybatis-spring)
* [手写简单版本Mybatis](#mybatis-write)

## 什么是Mybatis
> Mybatis的前身是Apache的一个开源项目iBatis,2010年这个项目由apache software foundation迁移
google code正式更名为Mybatis

下面是官方对Mybatis的描述(mybatis官网:http://mybatis.org/))
> MyBatis is a first class persistence framework with support for custom SQL, stored procedures and advanced mappings. MyBatis eliminates almost all of the JDBC code and manual setting of parameters and retrieval of results. MyBatis can use simple XML or Annotations for configuration and map primitives, Map interfaces and Java POJOs (Plain Old Java Objects) to database records.

在Mybatis里面,需要手写SQL,虽然比Hibernate配置多,但是Mybatis可以配置动态SQL,大大提高了开发效率和灵活性

## Mybatis简单例子
1.引入相应依赖
```shell script
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.38</version>
        </dependency>
```
2.定义实体类
```shell script
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{

    private int id;
    private String username;
    private String address;
    private String mobile;
}
```

3.定义mapper
```shell script
public interface UserMapper extends BaseMapper<User> {

    void create(User user);
}
```
4.定义mybatis-config.xml
```shell script
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <properties resource="db.properties"/>

    <typeAliases>
        <typeAlias alias="User" type="com.java.tech.entity.User"/>
    </typeAliases>

    <environments default="dev">
        <environment id="dev">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driverName}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="sqlmap/UserMapper.xml"/>
    </mappers>
</configuration>
```
5.定义UserMapping.xml
```shell script
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.java.tech.mapper.UserMapper">

    <!-- 自定义返回结果集 -->
    <resultMap id="userMap" type="User">
        <id property="id" column="id" javaType="java.lang.Integer"></id>
        <result property="username" column="username" javaType="java.lang.String"></result>
        <result property="address" column="address" javaType="java.lang.String"></result>
        <result property="mobile" column="mobile" javaType="java.lang.String"></result>
    </resultMap>

    <insert id="create" parameterType="User" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO cu_user(username, address, mobile) VALUES(#{username},#{address},#{mobile})
    </insert>
</mapper>
```
6.测试代码
```shell script
public class UserTest extends AbstractTest {

    @Test
    public void testInsert(){
        try {
            //1.加载mybatis-config配置文件
            Reader reader = Resources.getResourceAsReader("config/mybatis-config.xml");

            //2.构建sqlSessionFactory
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            //3.构建SqlSession
            SqlSession sqlSession =  sqlSessionFactory.openSession();
            UserMapper userMapper =  sqlSession.getMapper(UserMapper.class);

            User user = new User();
            user.setUsername("Jason");
            user.setAddress("shanghai");
            user.setMobile("18601705688");

            userMapper.create(user);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
更多详细可以参考项目mybatis/mybatis-demo中代码

## Mybatis基础模块及生命周期


## Mybatis配置的主要内容
1.typeHandler类型处理器:主要作用是将javaType转换为jdbcType或者从数据库取出结果时把jdbcType
转换为javaType,mybatis中用TypeHandlerRegistry来处理类型转换

如何自定义typeHandler
1.定义TypeHandler类
```shell script
@MappedTypes({String.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MyStringTypeHandler implements TypeHandler<String> {


    private Logger log = LoggerFactory.getLogger(MyStringTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        log.info("开始使用自定义的TypeHandler");
        ps.setString(i, parameter);
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        log.info("使用自定义的TypeHandler,ResultSet列名获取字符串");
        return rs.getString(columnName);
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        log.info("使用自定义的TypeHandler,ResultSet下标获取字符串");
        return rs.getString(columnIndex);
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        log.info("使用自定义的TypeHandler,CallableStatement下标获取字符串");
        return cs.getString(columnIndex);
    }
}
```

2.在mybatis-config.xml中配置TypeHandler
```shell script
<typeHandlers>
        <typeHandler handler="com.java.tech.typeHandler.MyStringTypeHandler" javaType="string" jdbcType="VARCHAR"/>
</typeHandlers
```

3.配置具体的Mapper.xml文件
```shell script
    <resultMap id="studentMap" type="Student">
        <id property="id" column="id" javaType="java.lang.Integer"></id>
        <result property="name" column="name" javaType="java.lang.String"></result>
        <result property="address" column="address" typeHandler="com.java.tech.typeHandler.MyStringTypeHandler"></result>
        <result property="createTime" column="createTime" javaType="java.util.Date"></result>
        <result property="updateTime" column="updateTime" javaType="java.util.Date"></result>
        <result property="isDeleted" column="isDeleted" javaType="java.lang.Integer"></result>
    </resultMap>


    <select id="queryAll" parameterType="Map" resultMap="studentMap">
        <include refid="select_all"></include>
    </select>
```
4.写个单元测试
```shell script
    @Test
    public void testQueryAll(){
        List<Student> studentList = studentMapper.queryAll();
        System.out.println(studentList);
    }
```
5.通过控制台可以看到日志说明TypeHandler生效了

Mybatis内部为我们提供了3种数据源的实现方式
- UNPOOLED 非连接池 由org.apache.ibatis.datasource.unpooled.UnpooledDataSource实现
- POOLED   连接池 由org.apache.ibatis.datasource.pooled.PooledDataSource实现
- JNDI  由org.apache.ibatis.datasource.jndi.JndiDataSourceFactory实现   

## Mybatis映射器的主要内容和配置
> 映射器是Mybatis比较强大的工具

常用的映射器配置有:
- select:查询语句
- insert:插入语句
- update:更新
- delete:删除
- parameterMap:定义参数映射关系
- sql:允许定义一部分sql,在各个地方使用
- resultMap:从数据库结果集种加载对象,提供映射规则,级联的更新
- cache:给定命名空间的缓存配置
- cache-ref:其他命名空间的缓存配置的引用

特殊字符串替换和处理(#和$)的区别:
- 配置参数要用#,不要用$,因为$不安全,容易被sql注入

### resultType与resultMap区别
### 优点
- resultType多表关联字段是清楚的 性能调优直观
- resultMap不需要join语句

### 缺点
- resultType需要创建很多实体类
- resultMap会出现N+1的问题(通过引入延迟加载来解决)

## 动态SQL
Mybatis的动态SQL包含以下几种元素:
- if: 判断语句
- choose(when,otherwise):多条件的判断
- trim(where,set):辅助元素 用于处理一些SQL的拼装问题
- foreach:循环语句

foreach的使用用法:
```shell script
 <select id="queryUserList" resultType="User">
        <include refid="select_all"></include>
        WHERE username in
        <foreach collection="userlist" item="username" index="index" open="(" separator="," close=")">
            #{username}
        </foreach>
</select>
```
下面对foreach参数解释一下:
- collection配置的userlist是传进来的参数名称,它可以是一个数组List,Set等集合
- item配置的循环中当前的元素
- index配置的当前元素在集合的下标
- open和close配置的是以什么符号将这些元素包装起来
- separator是各个元素的间隔符

## Mybatis解析及运行原理
在分析mybatis源码之前我们先要搞清楚几个重要的概念
- 代理模式
- 反射技术
- JDK动态代理
- CGLIB动态代理

> 代理模式一方面可以控制如何访问真正的服务对象提供额外服务,另外一方面有机会通过重写一些类来满足特定的需要
代理对象会持有真实对象的引用

### 反射技术
> 反射最大的好处是提高了Java的灵活性和配置性,降低模块之间的耦合

下面来看一个例子,写一个方法来通过反射调用
1.定义一个简单的方法
```shell script
public class ReflectService {

    public void sayHello(String name) {
        System.out.println("Hello " + name);
    }
}
```

2.写一个测试类
```shell script
public class ReflectServiceTest {

    public static void main(String[] args) throws Exception {
       //通过反射创建ReflectService对象
       Object service =  Class.forName("com.java.tech.proxy.ReflectService").newInstance();
       //获取服务方法
       Method method =  service.getClass().getMethod("sayHello", String.class);
       //反射调用
       method.invoke(service,"Jason");
    }
}
```
### JDK动态代理
JDK动态代理的缺点就是要提供接口,而Mybatis的Mapper就是一个接口,它采用的是JDK动态代理
```shell script
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface Mapper {
  // Interface Mapper
}
```
下面我们来实现动态代理
1.定义一个接口
```shell script
public interface HelloService {

    void echo(String name);
}
```

2.实现该接口
```shell script
public class HelloServiceImpl implements HelloService {
    @Override
    public void echo(String name) {
        System.out.println("Hello " + name);
    }
}
```
3.定义代理类
```shell script
public class ServiceProxy implements InvocationHandler {
    private Object target;

    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("--------我是JDK动态代理------------");
        Object result;
        System.out.println("我准备说hello");
        result = method.invoke(target, args);
        System.out.println("我已经说过了");
        return result;
    }
}
```
4.编写测试类
```shell script
public class Bootstrap {
    public static void main(String[] args) {
        ServiceProxy proxy = new ServiceProxy();
        HelloService helloService =  (HelloService) proxy.bind(new HelloServiceImpl());
        helloService.echo("Jason");
    }
}
```
### CGLIB动态代理
我们都知道JDK动态代理有一个缺陷是需要提供接口才可以使用,为了克服这个缺陷我们使用了开源框架-CGLIB
CGLIB的实现需要实现一个接口MethodInterceptor

```shell script
public class CglibServiceProxy implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("--------我是CGLIB动态代理--------");
        Object object;
        System.out.println("我准备说hello");
        object = methodProxy.invokeSuper(o, objects);
        System.out.println("我已经说过了");
        return object;
    }
}
```

### Mybatis源码分析
1.构建SqlSessionFactory的过程
SqlSessionFactory是通过SqlSessionFactoryBuilder来构建的,主要分两步:
- 解析配置的XML文件org.apache.ibatis.builder.xml.XMLConfigBuilder并存入org.apache.ibatis.session.Configuration
- 使用Configuration来创建SqlSessionFactory

2.几乎所有的配置信息都是存储在Configuration中

### 映射器的组成部分
一般而言,映射器的组成包括以下三个部分:
- MappedStatement:保存映射器的节点(select|insert|update|delete)
- SqlSource:提供BoundSql对象的地方,它是MappedStatement的一个属性
- BoundSql:建立参数和SQL的地方,有三个主要的特性:parameterMappings,parameterObject,sql

### SqlSession的运行过程分析
> 我们先来解释一下为什么Mybatis只用Mapper接口就能运行SQL,因为映射器的XML文件的命名空间对应的是这个接口
全路径,根据全路径和方法名能够绑定起来,通过动态代理技术让接口跑起来

下面我们介绍一下源码的实现
先来看下MapperRegistery源码实现
```shell script
@SuppressWarnings("unchecked")
  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    if (mapperProxyFactory == null) {
      throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }
    try {
      return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
      throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
  }
```

MapperProxyFactory源代码实现:
```shell script
protected T newInstance(MapperProxy<T> mapperProxy) {
    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
  }

  public T newInstance(SqlSession sqlSession) {
    final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
    return newInstance(mapperProxy);
  }
```
MapperProxy实现InvocationHandler
```shell script
@Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);
      } else if (method.isDefault()) {
        return invokeDefaultMethod(proxy, method, args);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    //实际业务会走execute方法
    return mapperMethod.execute(sqlSession, args);
  }
  }
```
MapperMethod源代码实现
```shell script
public Object execute(SqlSession sqlSession, Object[] args) {
    Object result;
    //部分代码省略
    switch (command.getType()) {
      case SELECT:
        if (method.returnsVoid() && method.hasResultHandler()) {
          executeWithResultHandler(sqlSession, args);
          result = null;
        } else if (method.returnsMany()) {
          result = executeForMany(sqlSession, args);
        } else if (method.returnsMap()) {
          result = executeForMap(sqlSession, args);
        } else if (method.returnsCursor()) {
          result = executeForCursor(sqlSession, args);
        } else {
          Object param = method.convertArgsToSqlCommandParam(args);
          result = sqlSession.selectOne(command.getName(), param);
          if (method.returnsOptional()
              && (result == null || !method.getReturnType().equals(result.getClass()))) {
            result = Optional.ofNullable(result);
          }
        }
        break;
      case FLUSH:
        result = sqlSession.flushStatements();
        break;
      default:
        throw new BindingException("Unknown execution method for: " + command.getName());
    }
```
通过上面的分析其实映射器就是一个动态代理的对象,进入到MapperMethod的execute方法以后,通过简单的分析
就进入了SqlSession的增删改成操作

### SqlSession的四大对象
- Executor代表执行器,它主要用来调度StatementHandler,ParameterHandler,ResultHandler来执行SQL
- StatementHandler:使用数据库的Statement(PrepareStatement)来执行操作,是四大对象的核心
- ParameterHandler:用于SQL对参数的处理
- ResultHandler:对最后的数据集ResultSet的封装

## Mybatis-Spring(官网: http://mybatis.org/spring/zh/)
> mybatis提供了和spring无缝对接,通过mybatis-spring-x.x.x.jar来实现,它允许mybatis参与spring的事务管理之中,创建映射器和SqlSession并注入
到bean中

1.引入相关依赖
```shell script
<dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
        </dependency>
</dependencies>
```

2.配置数据源等信息,更多详细内容参考mybatis-spring-demo/resources/appcontext-dal.xml文件
```shell script
 <context:property-placeholder location="classpath*:db.properties"/>

    <!--配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driverName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

    <!--配置sqlSessionFactoryBean-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="com.java.tech.entity"/>
        <property name="mapperLocations" value="classpath*:sqlmap/*.xml"/>
    </bean>

    <!--配置自动扫描对象关系映射-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!--具体的Mapper目录-->
        <property name="basePackage" value="com.java.tech.mapper"/>
    </bean>

    <!--声明事务管理-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--支持注解驱动的事务管理-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
```

3.写单元测试完成验证
```shell script
public class RoleDaoTest extends AbstractSpringContextTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testInsert() {
        Role role = new Role();
        role.setName("test");
        roleMapper.create(role);
    }

    @Test
    public void testFindById() {
        Role role = roleMapper.findById(2);
        System.out.println(role);
    }
}
```

## 手写简单版本Mybatis
### 设计思路
> 要手写mybatis首先要对mybatis底层有一个初步的认识,下面我们来介绍一下几个核心的对象

- Configuration: 用于存储所有的环境配置信息
- SqlSession:直接去数据库交互
- Executor:真正的执行器
- StatementHandler:用于操作数据库的
- ResultHandler:对数据集ResultSet的封装

为了更加贴近真实版本的mybatis,下面对所有类的命名前缀加"DF"

1.定义Configuration用于存储环境信息,是整个框架的入口
```shell script
public class DFConfiguration {

    private String basePackage;
    private final DFMapperRegistry mapperRegistry = new DFMapperRegistry();

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public DFMapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }
}
```

2.定义MapperRegistry
```shell script
public class DFMapperRegistry {

    private static final Map<String, DFMapperStatement> methodMapping = new HashMap<>();

    public DFMapperRegistry() {
        //这里需要手动模拟sql
        methodMapping.putIfAbsent("com.java.tech.mapper.UserMapper.queryByMobile", new DFMapperStatement("select * from cu_user where mobile=%s", User.class));
    }

    /**
     * DFMapperStatement用于保存映射器节点的信息
     *
     * @param <T>
     */
    public class DFMapperStatement<T> {
        private String sql;
        private Class<T> type;

        public DFMapperStatement(String sql, Class<T> type) {
            this.sql = sql;
            this.type = type;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Class<T> getType() {
            return type;
        }

        public void setType(Class<T> type) {
            this.type = type;
        }
    }

    public DFMapperStatement get(String namespace) {
        return methodMapping.get(namespace);
    }
}
```

3.定义executor
```shell script
public interface DFExecutor {

   <T> T query(DFMapperRegistry.DFMapperStatement mapperStatement, Object parameter);
}

```

4.实现Executor类
```shell script
public class DFSimpleExecutor implements DFExecutor {

    private DFConfiguration configuration;

    public DFSimpleExecutor(DFConfiguration configuration) {
        this.configuration = configuration;
    }

    public DFConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(DFConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T query(DFMapperRegistry.DFMapperStatement mapperStatement, Object parameter) {
        //真正用于操作数据库
        //这里要使用StatementHandler
        DFStatementHandler statementHandler = new DFStatementHandler(configuration);
        return (T)statementHandler.query(mapperStatement,parameter);
    }
}
```

5.测试代码
```shell script
public class Bootstrap {

    public static void main(String[] args) {

        //构建Configuration
        DFConfiguration configuration = new DFConfiguration();
        configuration.setBasePackage("com.java.tech.mapper");

        //构建ExecutorFactory工厂
        ExecutorFactory factory = new DefaultExecutorFactory();

        //获得Executor
        DFExecutor executor = factory.getCachingExecutor(configuration, new DFSimpleExecutor(configuration));

        //构建SqlSession
        DFSqlSession sqlSession = new DefaultsSqlSession(configuration, executor);

        //开始sql
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.queryByMobile("18801790199");
        System.out.println(user);
    }
}
```
**关于手写mybatis框架代码由于代码太多没有全部贴出来,源码部分放在mybatis/mybatis-demo下的mybatis目录下**