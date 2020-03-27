# MySQL实战到优化

## 主要内容介绍
* [MYSQL架构](#Mysql_arch)
* [索引结构](#Index)
* [SQL性能优化](#query_optimize)
* [数据库定位问题三板斧](#skills)
* [服务器优化设置](#optimized)
* [案例](#demo)
* [如何设计分布式ID不重复](#design)

## MYSQL架构
MYSQL服务逻辑架构分为三层:
- 最上层服务并不是Mysql所独有的,大多数基于网络客户端/服务器的工具
- 第二层结构包括mysql核心服务功能:查询解析(解析器),分析,优化(优化器),缓存以及所有内置函数 比如存储过程,触发器,视图等,通过API接口查询调用存储引擎.
- 存储引擎:主要负责mysql中数据的存储和提取

如何查看mysql存储引擎:
```shell script
show engines;
```

通过这个命令可以知道Mysql提供类两种事务型的存储引擎:
 - InnoDB:支持事务,XA
 - TokeDB(5.7版本不支持)
不支持事务的存储引擎
 - Myisam
 - Memory

### InnoDB与Myisam区别
- Myisam不支持事务的，不支持自动恢复数据 表级锁
- InnoDB支持事务和四种隔离级别,行级锁

#### 存储文件比较:
- InnoDB: .frm表定义文件 .ibd数据文件
- Myisam: .frm表定义文件 .myd数据文件 .myi索引文件

## 索引结构
> 说到索引其实并不陌生,那么到底什么是索引? 我们用一句话来描述:索引是能够帮助高效获取数据的数据结构,也可能是一个文件,
索引好比一本书,为了找到书中特定的话题,查看目录,获得页码,这个目录其实就是索引。

Mysql索引使用的是B+Tree实现的,叶子节点存储数据,高度是固定的,可控的,实际叶子节点存储的是一个逻辑地址

#### 这里解释一下为什么mysql选择B+ Tree没有选择其他的数据结构作为索引?
- hash:范围查询不合适
- 红黑树:它是平衡二叉树的一种,通过一系列的规则来保证树的平衡,大规模数据存储的时候,红黑树会出现树的深度过大而造成磁盘IO读写过于频繁,导致效率低下
- 二叉树:IO寻址复杂度高,如果二叉树的数据是有序的,就会形成二叉树的极端情况,形成链表,那么查询事件的复杂度就越高,并且需要更多的磁盘IO
- B-tree:是一个多路搜索树,也就是说每个节点可以有多个子节点,这样为了降低树的高度,减少磁盘IO次数
- B+ tree:子节点只存储键值不存储数据,数据全部存在叶子节点,并且叶子节点通过指针进行连接,因为子节点不存储数据,那么磁盘可以一次性读取更多的子节点
减少IO次数

基于时间局部性原理/空间局部性原理

说到索引下面我们来介绍一下建索引的基本原则:

## 建索引的基本原则
- 最左前缀匹配原则,这是一个非常重要的原则,mysql会一直向右匹配直到遇到范围查询(>,<,between,like)就停止匹配
比如 a=1 and b=2 and c>3 and d=4 如果建立索引(a,b,c,d),d是用不到索引的,如果建立(a,b,d,c)则都可以用到
- =和in可以乱序，比如a=1 and b= 2 and c=3 建立(a,b,c)索引可以任意顺序,mysql查询优化器会帮你优化索引可以识别的形式
- 尽量选择区分度高的列作为索引  比如一个人的性别只有男,女 区分度不高，所以我们不能用性别字段来建立索引
- 索引列不能参与计算,保持列干净 原因很简单 因为B+ Tree存储的都是数据表的字段值,但进行检索时,需要把所有的元素进行比较 性能开销比较大
- 尽量扩展索引考虑使用联合索引,不要新建索引。比如表中有a索引,现在要加(a,b)索引,那么只需要修改原来的索引
- 存在非等号和等号混合条件判断时,在建索引时,要把等号条件前置
- 结合业务综合评估建立联合索引

## 索引失效的场景
- 使用is not null或者is null会导致无法使用索引(这里可以通过为该字段设置默认值 来使索引生效)
- 使用like,between 会导致全表扫描,索引会失效
- 使用or(要想使用or索引也能生效,需要将or条件的每个列都加上索引)
- 使用范围查询(<,>,!=) 的时候无法使用索引,会导致索引失效

### Mysql文件系统组成部分(table.idb)
- 柱面
- 磁道
- 扇区

#### 常见建立索引相关的命令:

新建索引
```shell script
alter table cu_user add index IX_cu_user_mobile(mobile,memberId,userId);
```

第二种方式
```shell script
create index IX_cu_user_mobile on cu_user(mobile,memberId,userId)
```

##### 参数说明:
- IX_cu_user_mobile索引名称 命名规则IX_表名_索引名称
- cu_user 具体的表名称

删除索引
```shell script
alter table cu_user drop index IX_cu_user_mobile;
```

删除字段
```shell script
alter table cu_user drop column memberId;
```

增加字段
```shell script
alter table t_student add column address varchar(50);
```

新建主键索引
```shell script
alter table cu_user add unique(id,mobile);
```

为某个字段来设置默认值
```shell script
alter table cu_user alter column address set default "beijing";
```

### 建立索引字段的顺序
where -> join on -> group by -> having-> order by -> select 

**说明**
- 建立索引字段是有顺序的
- 要最优先建立where后的字段 其次是join on 然后是group by 
having,再然后是order by,最后是select

## SQL性能优化
我们在进行sql优化的时候有以下优化技巧:
- like语句中,匹配第一个字符是%不会走索引,只有%不在第一个位置索引才会起作用

这种情况是索引会失效
```shell script
select * from cu_user where  mobile like "%186%";
```
要把like条件放后面,这种情况就能走到索引
```shell script
select * from cu_user where   mobile = "18" and address like "%sh%";
```

- 如果查询条件中使用or关键字,那么or前后的条件都应该加上索引,索引才会生效
- 优化子查询 子查询虽然灵活,但执行效率不高,原因是子查询需要为内层的查询建立临时表,然后外层查询再从临时表中查询结果,
查询完毕以后,再撤销这张临时表,可以通过join来代替子查询,因为连接查询不需要建立临时表
- like语句应该改成union
下面我们来看一个例子
```shell script
select * from cu_member
where customerId = 10 and (mobile like '%13371862663%' or memberCode like '%13371862663%' or memberId like '%13371862663%');
```
虽然我们会为这个表建立索引,但是当数据量比较大的情况下查询还是会很慢,所以对于有like语句应该改成union
```shell script
select * from cu_member
where customerId = 10 and mobile="13371862663"
union
select * from cu_member
where customerId = 10 and memberCode="13371862663"
union 
select * from cu_member
where customerId = 10 and memberId="13371862663"
```

### 下面我们来介绍一下Mysql常用的一些命令
我们想知道数据库相关的一些命令比如最大连接数,可以通过show variables like "参数名称"来获得
列出所有的mysql的参数属性
```shell script
show variables;
```

获取数据库最大连接数
```shell script
show variables like "%max_connections%";
```
获得数据库的编码格式
```shell script
show variables like "%character%";
```

获得查询缓存的大小
```shell script
show variables like "%query_cache_size%";
```

获得查询限制大小
```shell script
show variables like "%query_cache_limit%";
```
获得innodb的buffer大小
```shell script
show variables like "%innodb_buffer_pool_size%";
```
获得所有相关线程的信息
```shell script
show variables like "%thread%"
```
获得启动以来历史的连接数
```shell script
show status like "%connection%"
```

### 聚集索引和非聚集索引的比较
- 聚集索引是以主键为索引来组织数据,不是一种单独的索引类型,而是一种存储数据的方式
对于聚集索引实际的数据行和相关的键值是保存在一起的 比如innoDB
- 对于非聚集索引实际的数据行和相关的键值不保存在一起 比如myisam

### 数据库执行计划
我们在数据库sql优化的时候离不开执行计划,通过执行计划可以很好的帮助分析sql性能以及是否匹配到索引

1.如何看执行计划 通过执行explain sql来看执行计划
```shell script
explain select * from cu_user where mobile="18601705642"
```

下面为执行完后的结果
```shell script
id	select_type	table	partitions	type	possible_keys	key	key_len	ref	rows	filtered	Extra
1	SIMPLE	cu_user	NULL	ref	IX_cu_user_mobile	IX_cu_user_mobile	83	const	1	100.00	NULL
```
下面我们来重点介绍以下执行计划该如何看:
- id这个表示序号 没有什么意义
- select_type:查询类型
- table:执行的表名称
- partitions
- type 这个非常重要 表示的是sql执行的效率,是衡量sql性能的一个非常重要的指标
sql性能优化的目标是 至少type要range级别,要求是ref,最好是const
关于type级别由低到高的顺序是:all<index<range<ref<const<system

#### 下面我们来type级别做一下解释
- ALL全表扫描(性能最差)
- index全索引扫描 通常比ALL快,因为索引文件比数据文件小
- range索引范围扫描,只检索范围的行, 使用一个索引来选择行
- ref指的是使用了普通的索引,查询条件既不是unique也不是primary key, join语句中被驱动表索引引用查询
- const单表中最多只有一个匹配行,在优化阶段即可读取到数据,const是最优的
- system表仅有一行 这是const类型的特列 平时不会出现

- possible_keys:可能会走到的索引
- key:具体走到索引的key
- key_len:key的长度
- rows:会影响到的行数
- Extra:该列包含列mysql解决查询的详细信息

##### 下面我们来解释以下Extra常用的信息:
- using index:使用覆盖索引
- using where:在查询使用索引的情况下,需要回表去查询所需的数据
- using index condition:查找使用了索引,需要回表查询数据
- using index & using where:查找使用了索引,但是需要的数据都在索引列中能找到所以不需要回表查询数据
- using filesort:mysql需要额外的一次传递,找出如何按排序顺序检索行
- using temporary:为了解决查询,mysql需要创建一个临时表来容纳查询结果

效率比较:using index & using where > using index condition

## 数据库定位问题三板斧
接下来我们会介绍一下数据库出现问题的解决办法

##### 1.如果发现数据库运行很慢时
通过show full processlist查询返回结果集中command列不等于Sleep的记录，找到对应的sql
```shell script
show full processlist;
```

```shell script
select * from information_schema.processlist where info is not null;
```

找出正在执行的sql
```shell script
select * from information_schema.INNODB_TRX;
```

通过上面的命令找到了sql,看哪些是人为查询,哪些是程序发送到数据库上执行的,人为查询的sql根据实际情况看是否需要kill
结束进程的命令为:
```shell script
kill 进程ID
```

##### 2.发现某一个表查询不出数据时,如何查询是否有死锁的问题
1.先通过命令查询是否有未结束的事务占用该表
```shell script
select * from information_schema.INNODB_TRX;
```
2.如果有通过以下命令kill
```shell script
kill trx_mysql_thread_id
```

### 下面我们来介绍一下慢查询优化的步骤
```shell script
0.先运行看看是否真的很慢，注意设置SQL_NO_CACHE
1.where条件单表查，锁定最小返回记录表。把查询语句的where都应用到表中返回的记录数最小的表开始查起，单表每个字段分别查询，看哪个字段的区分度最高
2.explain查看执行计划，是否与1预期一致（从锁定记录较少的表开始查询）
3.order by limit 形式的sql语句让排序的表优先查
4.了解业务方使用场景
5.加索引时参照建索引的几大原则
6.观察结果，不符合预期继续从0分析
```

## 服务器优化设置
我们讲到服务器优化下面重点介绍一下具体的参数和含义
- max_connections:最大连接数,默认1600,mysql最大的连接数是16384
- max_user_connections:每个数据库用户的最大连接 默认为0
- max_allowed_packet:每个连接独立的大小 默认32M
- innodb_buffer_pool_size:全局缓存 默认128M 一般设置为内存的一半 比如内存8GB,则设置为4GB
- innodb_sort_buffer_size:排序缓存大小 默认100M
- query_cache_size:查询缓存 默认32M 一般设置为500MB
- query_cache_limit:查询缓存限制 一般为500MB
- wait_timeout:mysql客户端的数据库连接闲置最大时间,单位是秒
- thread_concurrency:一般设置为CPU核数的2倍
- thread_cache_size:保存在缓存中线程的数量

## 案例
我们先定义两张表员工表和部门表
```shell script
CREATE TABLE `emp` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `empno` varchar(20) DEFAULT NULL COMMENT '员工号',
  `ename` varchar(20) DEFAULT NULL COMMENT '员工姓名',
  `job` varchar(20) DEFAULT NULL COMMENT '工作',
  `mgr` int(11) DEFAULT NULL COMMENT '上级编号',
  `hiredate` date DEFAULT NULL COMMENT '入职日期',
  `salary` decimal(10,2) DEFAULT NULL COMMENT '工资',
  `comm` decimal(10,2) DEFAULT NULL COMMENT '奖金',
  `deptno` int(11) DEFAULT NULL COMMENT '部门编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;
```

```shell script
CREATE TABLE `dept` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dept` int(11) DEFAULT NULL COMMENT '部门编号',
  `dname` varchar(20) DEFAULT NULL COMMENT '部门名称',
  `location` varchar(20) DEFAULT NULL COMMENT '地点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

1.列出至少有一个员工的所有部门
```shell script
SELECT t1.`dname` from `dept` as t1
WHERE t1.dept in(SELECT deptno FROM `emp` GROUP BY deptno HAVING count(deptno)>1);
```
2.列出薪资比“SMITH”多的所有员工
```shell script
SELECT * from emp 
WHERE `salary` >
(SELECT salary FROM emp WHERE ename="SMITH");
```

3.列出所有员工的姓名及其直接上级的姓名
```shell script
SELECT t1.ename,t2.ename AS manageName 
FROM emp AS t1
INNER JOIN emp AS t2
ON t1.`mgr` = t2.`empno`;
```

4.列出受雇日期早于其直接上级的所有员工
```shell script
SELECT t1.`ename` FROM emp AS t1
LEFT JOIN emp AS t2
ON t1.mgr = t2.`empno`
WHERE t1.`hiredate`< t2.`hiredate`;
```

5.列出部门名称和这些部门的员工信息，同时列出那些没有员工的部门 
```shell script
SELECT t1.dname,t2.ename,t2.empno,t2.job,t2.`hiredate`,t2.`salary` FROM dept AS t1
LEFT JOIN emp AS t2
ON t1.dept = t2.`deptno`;
```

6.列出所有“CLERK”(办事员)的姓名及其部门名称
```shell script
SELECT t1.ename,t2.dname 
FROM emp AS t1
LEFT JOIN dept AS t2
ON t1.`deptno` = t2.dept
WHERE t1.job="CLERK";
```

7.列出最低薪金大于1500的各种工作
```shell script
SELECT DISTINCT job
FROM emp
GROUP  BY job
HAVING MIN(`salary`)> 1500;
```

8.列出在部门 “SALES”(销售部)工作的员工的姓名，假定不知道销售部的部门编号
```shell script
SELECT ename FROM emp WHERE `deptno` in(
SELECT dept FROM dept WHERE dname="SALES");
```
```shell script
SELECT t1.ename FROM emp  AS t1
LEFT JOIN dept AS t2
ON t1.deptno = t2.dept
WHERE t2.dname="SALES";
```

9.列出薪金高于公司平均薪金的所有员工
```shell script
SELECT ename FROM emp
WHERE salary>(
SELECT avg(`salary`) FROM emp);
```

10.列出与 “SCOTT”从事相同工作的所有员工
```shell script
SELECT ename FROM `emp` 
WHERE job = (
SELECT job FROM emp  WHERE ename="SCOTT")
```

## 如何设计分布式ID不重复
> 在分布式环境下,如何保证产生的ID不重复,Twitter提出来一个算法叫SnowFlake 中文命名雪花算法,其目的是生成一个64bit的整数
能够在整个分布式系统内不会产生ID碰撞(由数据中心和机器ID做区分),并且效率高,每秒能够产生26万ID

### 雪花算法
雪花算法(snowflake)是Twitter开源的分布式ID生成算法，结果是一个long型的ID
核心思想是：使用41bit作为毫秒数，10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID），12bit作为毫秒内的流水号，最后还有一个符号位，永远是0.

#### 下面是雪花算法的实现
```shell script
public class SnowFlake {

    private final long poch = 1565020800000L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBit = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBit = 5L;

    /**
     * 支持的最大机器id，结果是31
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBit);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBit);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceId = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceId;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceId + workerIdBit;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceId + workerIdBit + datacenterIdBit;

    /**
     * 生成序列的掩码，这里为4095
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceId);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    public SnowFlake(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = nextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - poch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    protected long nextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
```