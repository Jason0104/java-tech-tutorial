# ElasticSearch从入门到精通

## 主要内容介绍
* [什么是ElasticSearch](#what's elasticSearch)
* [单机环境搭建](#elasticSearch_alone)
* [分布式环境搭建](#elasticSearch_distributed)
* [可视化插件安装](#elasticSearch_plugins)
* [基本原理](#elasticSearch_how_to_work)
* [高级特性](#elasticSearch_feature)
* [分布式存储](#elasticSearch_distributed)
* [分片技术](#elasticSearch_shard)


## 什么是ElasticSearch
> ElasticSearch是一个基于Lucene的搜索服务器,提供了分布式搜索和分析引擎可以用于全文搜索,基于Restful Web接口,是用Java开发的。

## ElasticSearch单机环境搭建
1.去ElasticSearch官网下载:https://www.elastic.co/downloads/past-releases,
解压到/workspace/software目录下,进入elasticSearch中bin目录下,启动elasticSearch：
```shell script
./elasticsearch
```
2.启动完成elasticSearch以后可以在控制台查看启动日志
```shell script
[2020-03-30T22:27:49,077][INFO ][o.e.h.AbstractHttpServerTransport] [Jason-Pro.local] publish_address {127.0.0.1:9200}, bound_addresses {[::1]:9200}, {127.0.0.1:9200}
[2020-03-30T22:27:49,079][INFO ][o.e.n.Node               ] [Jason-Pro.local] started
[2020-03-30T22:27:49,319][INFO ][o.e.l.LicenseService     ] [Jason-Pro.local] license [1a9b48cd-c75a-4c7d-9943-bf0b698fbee0] mode [basic] - valid
[2020-03-30T22:27:49,320][INFO ][o.e.x.s.s.SecurityStatusChangeListener] [Jason-Pro.local] Active license is now [BASIC]; Security is disabled
[2020-03-30T22:27:49,329][INFO ][o.e.g.GatewayService     ] [Jason-Pro.local] recovered [0] indices into cluster_state
```

3.浏览器端输入http://localhost:9200 查看elasticSearch是否启动成功
```json
{
  "name" : "Jason-Pro.local",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "y4kLjfwFSGqUcAQZV1GS5Q",
  "version" : {
    "number" : "7.6.1",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "aa751e09be0a5072e8570670309b1f12348f023b",
    "build_date" : "2020-02-29T00:15:25.529771Z",
    "build_snapshot" : false,
    "lucene_version" : "8.4.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```
说明elasticSearch启动成功

## ElasticSearch分布式环境搭建
1.将刚下载的elasticSearch文件复制两份并重新命名为
- elasticsearch-7.6.1-master
- elasticsearch-7.6.1-salve1
- elasticsearch-7.6.1-salve2

2.修改注节点的配置,打开elasticsearch-7.6.1-master/conf/elasticsearch.yml文件,在文件底部添加以下内容:
```shell script
cluster.name: es-cluster   # 集群名称三个节点保持一致
node.name: master          # 主节点id,保证唯一 
node.master: true          # 标注是否为主节点
network.host: 127.0.0.1    # 对外公开的IP地址
```
**这里需要注意一定要有空格**

3.接下来配置slave-1从节点,打开elasticsearch-7.6.1-salve1/conf/elasticsearch.yml,添加以下内容:
```shell script
cluster.name: es-cluster    # 集群名称三个节点保持一致
node.name: slave-1          # 从节点id,保证唯一
network.host: 127.0.0.1     # 对外公开的IP地址
http.port: 9100              # 默认端口是9200 因为在同一台机器,需要指定端口
discovery.zen.ping.unicast.hosts: ["127.0.0.1"] # 集群的IP组,配置主节点IP即可
```

4.配置slave-2节点,在elasticsearch-7.6.1-salve2/conf/elasticsearch.yml添加下面内容:
```shell script
cluster.name: es-cluster    # 集群名称三个节点保持一致
node.name: slave-2          # 从节点id,保证唯一
network.host: 127.0.0.1     # 对外公开的IP地址
http.port: 9300              # 默认端口是9200 因为在同一台机器,需要指定端口
discovery.zen.ping.unicast.hosts: ["127.0.0.1"] # 集群的IP组,配置主节点IP即可
```

5.分别启动三个节点,分别通过浏览器访问http://localhost:9100,ttp://localhost:9200,ttp://localhost:9300都可以访问成功
此时说明elasticSearch分布式环境已经安装成功!

## ElasticSearch可视化插件安装
1.下载NodeJS https://nodejs.org/en/download/
2.安装完nodejs以后,运行下面命令用于查看是否安装成功
```shell script
node -v
v8.11.3
```
3.下载elasticSearch-head安装包,在github官网搜索elasticSearch

## ElasticSearch基本原理
### Lucene的工作原理
- Document:文本数据,数据的存储单元
- Index:索引,提升查询效率
- Analyze:分词器,打标签提高精准度

### ElasticSearch基本概念
- 索引:含有相同属性的文档集合
- 类型:索引可以定义一个或多个类型,文档必须属于一个类型
- 文档:数据的存储单元
- 分片:每个索引都有多个分片,每个分片是一个Lucene索引
- 备份:拷贝一份分片就完成了分片的备份

#### 关系数据库与ElasticSearch操作对比
关系数据库    ElasticSearch
建库(DB)      建库(index)
建表(Table)   建表(IndexType)
键约束(Constraint)  主键(ID)

正排索引与倒排索引

## 高级特性


## 分布式存储

## 分片技术