# Nginx从入门到精通

## 主要内容介绍
* [什么是Nginx](#what's nginx)
* [Nginx的安装部署](#install nginx)
* [Nginx服务器架构](#nginx_arch)
* [Nginx服务器高级配置](#nginx_config)
* [Nginx代理服务](#nginx_proxy)
* [Rewrite](#nginx_rewrite)
* [项目案例配置](#nginx_demo)
   * 高并发配置
 
## 什么是Nginx
> Nginx是一个高性能的反向代理服务器

### Nginx功能特性
- HTTP服务
- 邮件代理服务
- Web缓存

#### 正向代理&反向代理的区别?
- 正向代理代理的是客户端, 比如通过浏览器->百度网站
- 反向代理代理的是服务端, 比如浏览器访问->百度网站->新浪,阿里,网易网站

##### 常见的web服务器
- Tomcat:动态的web服务器
- Apache:静态的web服务器
- IIS:是Microsoft公司的web服务器产品
- Jetty
- JBoss
- Nginx:是一款高性能的HTTP反向代理服务器,能够支持高达50000个高并发连接数响应,是2004年10月开始发布
- Weblogic
- Netty
- Websphere

## Nginx的安装(官网地址:http://nginx.org/)

### Mac安装nginx
```shell script
brew install nginx
```
### 源码安装
```shell script
1.tar -zxvf 安装包  -解压安装包
2. ./configure --prefix=/usr/local/nginx   默认安装到/usr/local/nginx
3. make & make install
```

### Linux安装
```shell script
yum install nginx
```

### 安装过程中可能会出现问题
缺少pcre的依赖,缺少openssl的依赖,通过执行下面的命令来完成所需依赖的安装:
```shell script
yum install pcre-devel
yum install openssl-devel
yum install zlib-devel
```

##### Nginx启动
```shell script
service nginx start;//启动nginx
./nginx -c /usr/local/nginx/nginx/conf;//启动nginx
```

##### Nginx平滑启动
```shell script
service nginx reload;//重新加载nginx.conf
./nginx -s reload;
```

##### Nginx停止
```shell script
service nginx stop;//停止
./nginx -s stop;
./ningx -s quit;//退出
```

## Nginx服务器架构
> Nginx服务器响应和处理web请求的过程就是基于事件驱动模型的,事件驱动模型是Nginx服务器保障完整功能和具有良好性能的重要机制之一

事件驱动模型主要分为三个组成部分:
- 事件收集器
- 事件发送器
- 事件处理器

事件驱动处理库又被称为多路IO复用,最常见包括以下三种:
- select模型
- poll模型
- epoll模型

Nginx服务器结构大致分为:
- 主进程:Nginx服务器启动运行时的主要进程,主要功能是与外界通信和对内部其他进程进行管理
- 工作进程:由主进程生成,生成数量通过nginx配置文件指定
- 后端服务
- 缓存


Nginx进程模型分为Single和Master两种进程模型
- Single模型为单进程式,具有较差的容错能力,不适合生产环境
- Master模型即一个master进程+N个worker进程的工作方式

## Nginx服务器高级配置
### nginx服务器日志相关
关于日志的配置格式:
```shell script
log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"'
                      '"$http_cookie" "$request_time" "$upstream_addr" "$upstream_response_time" "$upstream_status"' ;
```
**参数解释**
- $remote_addr:客户端地址
- $remote_user:客户端用户名称
- $time_local:访问时间与时区
- $request:请求url与http协议
- $status:Http请求状态
- $body_bytes_sent:发送客户端文件内容的大小
- $http_referer:url跳转来源 记录从哪个页面链接访问过来的
- $http_user_agent:用户终端浏览器相关信息
- $http_x_forwarded_for:客户端ip
- $http_cookie:用户获取cookie信息
- $upstream_status:upstream状态
- $upstream_addr:后台upstream的地址,真正用于提供服务的地址
- $request_time:整个请求的总时间
- $upstream_response_time:upstream响应时间
- $http_host:请求主机
- $arg:请求参数
- $request_method:请求方法


### nginx配置https证书
```shell script
server  {
   listen  443;
   server_name   test.nginx.cn;

   if    (  $host = $server_addr )  {
       return 444;
   }

   ssl  on;
   ssl_certificate   /etc/nginx/2018_nginx.cn.crt;
   ssl_certificate_key  /etc/nginx/2018_nginx.cn.key;
   ssl_session_timeout  5m;
   ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
   ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
   ssl_prefer_server_ciphers  on;

   location  /  {
       proxy_pass  http://localhost:8080;
       proxy_set_header Host $host;
       proxy_set_header X-Real-IP $remote_addr;
       proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       proxy_set_header X-Forwarded-Proto  $scheme;
       client_max_body_size 10m;
    }
 }
```

### Gzip压缩策略
常见的压缩方式有:gzip,deflate
```shell script
    gzip  on;
    gzip_http_version  1.1;
    gzip_types  application/x-javascript application/javascript    application/xml text/javascript  text/css  text/plain  text/xml ;
    gzip_buffers 4 16k;
    gzip_min_length  1024;
    gzip_comp_level 4;  //数字越小 压缩效果越好 消耗CPU越高
    gzip_disable "MSIE [1-6].";
    gzip_vary    on; 
    gzip_proxied any;
```

## Nginx代理服务
nginx反向代理是通过proxy_pass来配置,可以是ip也可以是域名
```shell script
   upstream proxy_server{
      ip_hash;
      server 127.0.0.1:9090  weight=6;
      server 192.168.102.100:9090 weight=4;
   }

   location  /  {
       proxy_pass  http://proxy_server;
       proxy_set_header Host $host;
       proxy_set_header X-Real-IP $remote_addr;
       proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       proxy_set_header X-Forwarded-Proto  $scheme;
       client_max_body_size 10m;
    }
```

## Nginx负载均衡的策略
- ip_hash:根据ip的hash值来做转发
- 轮询机制:默认的方式
- 权重:通过weight=6 数字越大 分配到服务器的几率越大
- fair:根据服务器的响应时间来分配请求
- url_hash

## Rewrite
rewrite通过ngx_http_rewrite_module模块支持url重写,支持if判断,但不支持else

#### rewrite与location的区别
- rewrite是同一域名内更改获取资源的路径
- location是对一类路径做控制访问或者反向代理

常用的rewrite配置方法:
```shell script
location / {
     if ($http_user_agent ~ Mozilla/5.0) { #如果是chrom浏览器
        rewrite ^(.*)$ http://www.baidu.com; #返回客户端302
      }
    }
```

比如有一个需求换域名后导流到新域名,实现效果访问www.a.com/123.html会自动跳转到www.b.com/123.html
```shell script
server {
    listen             80;
    server_name      www.a.com;
    rewrite  ^/(.*)$     http://www.b.com/$1 permanent;
}
```

### Rewrite使用参数说明:
- last: 停止处理后续的rewrite指令集、 然后对当前重写的uri在rewrite指令集上重新查找
- break; 停止处理后续的rewrite指令集 ,并不会重新查找
- redirect:返回302   临时重定向
- permanent:返回301  永久性重定向

## 项目案例

### 高并发场景配置
> 漏桶算法: 假设系统是一个漏桶,当请求到达时,就是往漏桶里加水,而当请求被处理掉,就是水从漏桶的底部漏出,水漏出的速度是固定的，
当加水太快,桶就会溢出,也就是拒绝请求,使得桶里的水的体积不可能超出桶的容量,主要的目的是控制数据注入到网络的速率,平滑网络上的
突发流量。

```shell script
limit_req_zone  $server_name          zone=allips:10m  rate=10r/s;
limit_conn_zone $server_name          zone=perip:10m;
```

### 参数说明
- zone=one或allips表示one或者allips的存储区,大小为10m
- rate=10r/s表示允许1秒钟不超过10个请求
- burst=5表示最大延迟请求数量不大于5,如果过多的请求被限制,延迟是不需要的,这是需要使用nodelay参数,服务器会返回503
- limit_conn one 100:表示限制每个客户端IP的最大并发连接数100
- limit_conn perserver 1000:表示该服务提供的总连接数不得超过1000，超过请求的会被拒绝

下面我们来对一个完整的nginx配置做一下解释
```shell script
user root; //用户的配置
worker_processes 4; //用来设置nginx服务的进程数,一般根据CPU的核数来设置的 比如CPU是4核 则设置为4
error_log /usr/nginx/data/error.log; //设置错误日志的路径 默认是在/var/log/nginx/error.log
pid /var/run/nginx.pid;//nginx进程号

//配置事件驱动模型
events {
    use epoll; //用于指定nginx服务器使用的事件驱动模型
    worker_connections  65536; //设置nginx服务器的每个工作进程允许同时连接客户端的最大数量
}

http {
    //默认类型
    default_type        application/octet-stream;

    //日志配置格式
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"'
                      '"$http_cookie"';
    //高并发限流的配置
    limit_req_zone  $server_name    zone=allips:100m rate=3000r/s;
    limit_conn_zone $server_name    zone=perip:100m;
 
    //设置nginx访问日志
    access_log  /usr/nginx/data/access.log  main;

    sendfile            on;
    tcp_nopush          on;
    tcp_nodelay         on;
    keepalive_timeout   65;//设置nginx服务器与客户端保持连接的超时时间
    client_header_timeout 15;
    client_header_buffer_size 4k; //设置nginx服务器允许客户端请求头部的缓冲区大小,默认是1KB
    client_body_timeout 15;
    send_timeout 25;//设置nginx服务器响应客户端的超时时间
    types_hash_max_size 2048;

    //fastcgi相关参数配置
    fastcgi_intercept_errors on;
    fastcgi_connect_timeout 300;
    fastcgi_send_timeout 300;
    fastcgi_read_timeout 300;
    fastcgi_buffer_size 256k;
    fastcgi_buffers 8 256k;
    fastcgi_busy_buffers_size 256k;
    fastcgi_temp_file_write_size 256k;

    //gzip相关参数配置
    gzip  on; //开启gzip功能
    gzip_http_version  1.1; //设置支持http版本
    //设置被压缩的文件类型
    gzip_types  application/x-javascript application/javascript    application/xml text/javascript  text/css  text/plain  text/xml ;
    gzip_buffers 4 16k;//设置gzip压缩文件使用缓存空间的大小
    gzip_min_length  1024;//设置压缩的大小
    gzip_comp_level 4; //设置gzip压缩的程度 数字越小 压缩效率越高
    gzip_disable "MSIE [1-6]."; //针对不同种类客户端发起的请求,可以选择性的关闭或开启gzip功能
    gzip_vary    on;//设置在使用gzip功能时是否发送带有"Vary:Accept-Encoding" 的响应头部
    gzip_proxied any;//该指令在使用nginx服务器的反向代理功能时有效

    client_body_buffer_size 512k;
    client_max_body_size    20m;
    proxy_connect_timeout 600;
    proxy_read_timeout 600;
    proxy_send_timeout 600;
    proxy_buffering on;
    proxy_max_temp_file_size 0;
    proxy_buffer_size 64k;
    proxy_buffers 6 256k;
    proxy_busy_buffers_size 640K;
    proxy_temp_file_write_size 512k;

    include             /etc/nginx/mime.types;

    #负载均衡
    upstream payment{
      # ip_hash;
       server 127.0.0.1:8090 weight=6;
       server 192.168.1.2:9091 weight=4;
    }

    upstream acquire{
       ip_hash;
       server 127.0.0.1:8087 weight=4;
       server 192.168.1.3:8083 weight=6;
       keepalive 1024;
    }

   server {
    listen       80;
    server_name  www.pay.cn;
    root         /usr/share/nginx/html;

    location ~* ^.+.gzjs$ {
       add_header Content-Encoding gzip;
       gzip on;
    }
    //匹配具体的文件
    location /test.txt{
       root  /usr/share/nginx/html;
       index  index.html  index.htm;
   }  
    
    location ~*  \.(gif|jpg|jpeg|png|bmp|swf|svg|ico|mp3)$
                  {
                        proxy_pass http://payment;
                        proxy_ignore_headers Cache-Control Expires ;
                        proxy_buffering on;
                        proxy_hide_header Cache-Control;
                        add_header Cache-Control max-age=18000;
                        proxy_set_header        X-Real-IP $remote_addr;
                        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
                        proxy_set_header        Host $http_host;
                        proxy_cache_methods GET HEAD POST;
                        proxy_cache_min_uses 1;
                        expires 1d;
                   }
     //根据正则表达式进行匹配
     location ~*  \.(js|css|html)?$ {
                        proxy_pass http://acquire;
                        proxy_buffering on;
                        proxy_ignore_headers Cache-Control Expires;
                        proxy_hide_header Cache-Control;
                        add_header Cache-Control max-age=18000;
                        proxy_set_header        X-Real-IP $remote_addr;
                        proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
                        proxy_set_header        Host $http_host;
                        proxy_set_header        Accept-Encoding 'gzip';
                        proxy_cache_methods GET HEAD POST;
                        proxy_cache_min_uses 1;
                        expires 1d;
                    }

   //配置具体的location进行反向代理
   location /payMng{
          proxy_redirect off;
          proxy_set_header Host $host;
          proxy_set_header X-Real-IP $remote_addr;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_pass http://payment;
    }
    //rewrite的使用实现永久重定向
    location /{
      rewrite ^(.*)$  https://www.pay.cn$1 permanent;
    }

   //配置错误页面
   error_page 501 502 503 504  /50x.html{
        location = /50x.html {
            root   html;
        }
   }   

  //配置https
  server  {
   listen  443; //端口
   server_name   www.pay.cn;
   if ($host = $server_addr){
       return 444;
   }

   ssl  on;//开启ssl
   ssl_certificate   /etc/nginx/2020_test.cn.crt;//配置证书
   ssl_certificate_key  /etc/nginx/2020_test.cn.key;//配置证书key
   ssl_session_timeout  5m;
   ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
   ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
   ssl_prefer_server_ciphers  on;

   location  /  {
       proxy_pass  http://localhost:8080;
	   proxy_set_header Host $host;
       proxy_set_header X-Real-IP $remote_addr;
       proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
	   proxy_set_header X-Forwarded-Proto  $scheme;
       client_max_body_size 10m;
    }
  }
}
```