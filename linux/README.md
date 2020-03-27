# Linux从入门到精通

## 主要内容介绍
* [Linux常用命令介绍](#Linux_Command)
  * 操作文件相关
  * 查询Linux系统相关
  * 查询进程相关
  * 安装软件相关
  * 网络及防火墙设置相关
  * 操作磁盘相关
  * grep&sed&awk的用法
* [磁盘分区](#Linux_File)
* [创建Swap分区](#Swap)
* [Linux文件权限](#Linux_privilage)

## Linux常用命令介绍
#### 操作文件相关

1.查找当前目录下文件中包含某个字符串 比如"welcome"字符串,*表示当前目录下的所有文件也可以是文件名
```shell script
grep -rn "welcome" * --col;
grep -rn "hello" /home/admin/
```
2.在某个具体的目录下查找包含某个关键字的文件
```shell script
grep -rn /usr/local -e "2003"
```
3.查找目录/home/admin下的所有Snow开头的文件
```shell script
find /home/admin -name "Snow*"
```
4.查找所有大小超过100M的文件
```shell script
find /home/admin -size +100M
```
5.在某个目录下面查找名字为"tomcat"的文件
```shell script
find /home/admin -type f -name "tomcat"
```
6.解压tar.gz的文件到/usr/local目录下
```shell script
tar -zxvf redis.tar.gz -C /usr/local
```
7.解压zip文件到/usr/local目录下
```shell script
unzip grade-3.3-bin.zip -d /usr/local
```
8.把某个文件夹打包成zip,比如将test目录打包成test.zip
```shell script
zip -r test.zip test
```
9.创建软链接/var/test指向/var/www/test
```shell script
ln -s /var/www/test /var/test; 
```
10.删除软链接,注意后面不要加/
```shell script
rm -rf /var/test
```
11.统计某个文件具体的行数
```shell script
wc -l filename
```
12.统计某个文件的字数
```shell script
wc -c filename
```
13.统计某个文件的单词数
```shell script
wc -w filename
```
14.统计当前目录的文件个数
```shell script
ls -l | wc -l
```
15.远程拷贝文件到另外一台服务器的/usr/local/temp目录下
```shell script
scp file.txt root@hostname:/usr/local/temp/
```
16.拷贝远程服务器的某个目录到本地的/usr/local/temp目录下
```shell script
scp root@hostname:/wwww/*  /usr/local/temp
```
很多时候可能有ssh端口限制,需要加上端口号进行传输
```shell script
scp -P 2244 client.xml root@hostname:/usr/local/temp
```
17.有的时候文件传输考虑到性能问题,我们采用异步传输
将本地目录/home/app与远程/usr/local/temp目录同步,启动压缩
```shell script
rsync -avz /home/app root@hostname:/usr/local/temp
```

#### 查询Linux系统相关
1.查看linux内核版本
```shell script
cat /proc/version
uname -r
```
2.查看内核/操作系统的信息
```shell script
uname -a
```
3.查看系统版本号
```shell script
lsb_release -a
```
4.查看系统CPU执行情况
```shell script
top -c
```
5.查看CPU统计信息
```shell script
mpstat 1
```
6.查看虚拟内存统计信息
```shell script
vmstat 2;//每隔2s进行统计
```
7.查看IO统计信息
```shell script
iostat 2;//每隔2s进行统计
```
8.监测硬件和启动消息
```shell script
dmesg
```
9.查看CPU的信息
```shell script
cat /proc/cpuinfo
```
10.查看内存信息
```shell script
cat /proc/meminfo
```
11.查看内存的已使用和可用 -m单位为M
```shell script
free -m
```
12.新建用户
```shell script
adduser cdn
```
设置密码
```shell script
passwd cdn
```
13.删除用户
```shell script
deluser -remove-home cdn
```
14.创建组
```shell script
groupadd test
```
删除分组
```shell script
delgroup test
```

15.Linux内存清理命令
```shell script
free -m ;//查看内存使用情况
echo 1 > /proc/sys/vm/drop_caches;//开始清理
```

#### 查询进程相关
1.查看所有的进程
```shell script
ps -aux
```
2.查看所有的Java进程
```shell script
ps -ef|grep java
```
3.查看启动redis的服务
```shell script
netstat -lp|grep redis
```
4.查看端口8080是否被监听
```shell script
netstat -nltp|grep 8080
```
5.列出所有活跃的进程打开的文件
```shell script
lsof;
lsof -u admin;//列出所有admin用户打开的文件
```
6.列出最近使用pip命令的1000条记录
```shell script
history 1000|grep pip
```

#### 安装软件相关
1.解压rpm文件
```shell script
rpm --install couchbase-server-enterprise-3.0.3-centos6.x86_64.rpm
```
2.查看所有安装的软件包
```shell script
rpm -qa
```
3.查看系统所有的内核
```shell script
rpm -qa|grep kernel
```
4.删除内核kernel-headers-3.10.0-327.el7.x86_64
```shell script
yum remove kernel-headers-3.10.0-327.el7.x86_64
```
5.从源码进行安装,进入文件的根目录下执行下面的命令
```shell script
./configure
make
make install
```

#### 网络及防火墙设置相关
##### 防火墙相关的命令
1.列出系统所有服务启动的情况
```shell script
chkconfig --list
```
2.查看防火墙的状态
```shell script
service iptables status
```
3.打开防火墙 
```shell script
service iptables start
```
4.关闭防火墙
```shell script
service iptables stop
```
5.为防火墙添加端口和IP,需要在/etc/sysconfig/iptables目录编辑
添加允许1006端口访问
```shell script
iptables -A INPUT -p tcp --dport 1006 -j ACCEPT
```
允许192.168.60.87能够访问
```shell script
iptables -A INPUT -s 192.168.60.87 -p tcp -j ACCEPT
```
##### 网络相关的命令
1.查询域名所对应的IP地址能够知道整个解析的过程
```shell script
nslookup www.baidu.com
```
获取指定域名的DNS信息
```shell script
dig www.baidu.com
```
2.列出所有网络端口和IP地址
```shell script
ifconfig -a
```
列出指定以太网端口对应的IP地址和详细信息
```shell script
ifconfig eth0
```
3.监控网络带宽,需要安装iftop
```shell script
iftop
```
4.列出系统的活跃连接
```shell script
netstat -tupl
```

#### 操作磁盘相关
1.查看磁盘的使用情况
```shell script
df -h
```
2.查看目录的大小
```shell script
du -h
```
3.查看某个目录下所有文件的及文件大小
```shell script
du -sh *
du -sh *|sort -nr;//排序
```
4.查看当前的所有分区
```shell script
fdisk -l
```

## grep&sed&awk的用法
Linux中最重要的三个命令在业界被称为"三剑客",它们是grep,sed,awk
- grep擅长查找功能
- sed擅长取行和替换,本身是一个管道命令
- awk擅长取列

sed的常用命令:
a∶新增 a的后面可以接字符串
c∶取代 c的后面可以接字符串，这些字串可以取代n1,n2之间的行
d∶删除 d的后面不接字符串
i∶插入 i的后面可以接字符串，而这些字串会在新的一行出现(目前的上一行)

1.删除hello.txt文件中的第一行
```shell script
sed "1d" hello.txt
```
2.删除hello.txt文件中的最后一行
```shell script
sed "$d" hello.txt
```
## 磁盘分区
1.查看磁盘使用情况
```shell script
[root@VM_176_224_centos ~]# fdisk  -l    #查看磁盘使用情况
Disk /dev/vda: 21.5 GB, 21474836480 bytes, 41943040 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x0005fc9a
   Device Boot      Start         End      Blocks   Id  System
/dev/vda1   *        2048    41943039    20970496   83  Linux

Disk /dev/vdb: 85.9 GB, 85899345920 bytes, 167772160 sectors  看到有一个/dev/vdb磁盘
Units = sectors of 1 * 512 = 512 bytes                        是没有使用的
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
```
2.对数据盘/dev/vdb进行分区
```shell script
[root@VM_176_224_centos ~]# fdisk   /dev/vdb     
Command (m for help): n                   n表示创建一个分区
Partition type:
   p   primary (0 primary, 0 extended, 4 free)

   e   extended

Select (default p): p                     p表示 创建的是一个主分区
Partition number (1-4, default 1): 1         1表示这是第一个分区
First sector (2048-167772159, default 2048):           起始大小，默认 Using default value 2048
Last sector, +sectors or +size{K,M,G} (2048-167772159, default 167772159): +20G  
Partition 1 of type Linux and of size 20 GiB is set                 创建20G的一个分区
Command (m for help): n   
Partition type:
   p   primary (1 primary, 0 extended, 3 free)
   e   extended
Select (default p): p
Partition number (2-4, default 2): 2       表示这是第二个分区
First sector (41945088-167772159, default 41945088):
Using default value 41945088
Last sector, +sectors or +size{K,M,G} (41945088-167772159, default 167772159): 这里默认回车Using default value 167772159                            表示所有的空间全分出去
Partition 2 of type Linux and of size 60 GiB is set
Command (m for help): w           把所做的修改保存
[root@VM_176_224_centos ~]# fdisk  -l       
Disk /dev/vdb: 85.9 GB, 85899345920 bytes, 167772160 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk label type: dos
Disk identifier: 0x67b29857
   Device Boot      Start         End      Blocks   Id  System
/dev/vdb1            2048    41945087    20971520   83  Linux
/dev/vdb2        41945088   167772159    62913536   83  Linux
```

3.格式化磁盘
```shell script
[root@VM_176_224_centos ~]# mkfs.ext4   /dev/vdb1      对分区/dev/vdb1格式化成ext4
[root@VM_176_224_centos ~]# mkfs.ext4   /dev/vdb2
```
4.挂载磁盘到指定的目录
```shell script
[root@VM_176_224_centos ~]# mount  /dev/vdb1   /data   把分区/dev/vdb1挂在到/data下
[root@VM_176_224_centos ~]# mkdir  /software
[root@VM_176_224_centos ~]# mount  /dev/vdb2   /software
[root@VM_176_224_centos ~]# unmount /software; //卸载/root/software挂载目录
```
5.查看是否挂载成功
```shell script
[root@VM_176_224_centos ~]# df  -h   查看是否挂在成功
Filesystem      Size  Used Avail Use% Mounted on
/dev/vdb1        20G   45M   19G   1% /data
/dev/vdb2        59G   53M   56G   1% /software
```

6.如何设置开机启动
```shell script
[root@VM_176_224_centos ~]# vim  /etc/fstab         设置开机自动挂载

/dev/vdb1            /data                ext4      defaults    0  0

/dev/vdb2            /software      ext4      defaults   0  0 
```
## 创建Swap分区
1.通过命令查看系统是否有swap分区
```shell script
[root@_224_centos ~]# free
             total       used       free     shared    buffers     cached
Mem:      16332028    6526084    9805944        176     302608    1812816
-/+ buffers/cache:    4410660   11921368
Swap:            0          0          0
```

2.在根目录创建1个10G的swap文件
```shell script
[root@VM_30_103_centos ~]# dd if=/dev/zero of=./swap bs=1M count=10000
记录了1024+0 的读入
记录了1024+0 的写出
524288字节(524 kB)已复制，0.00233599 秒，224 MB/秒
```
3.创建交换区
```shell script
[root@VM_30_103_centos /]# mkswap -f /swap
Setting up swapspace version 1, size = 508 KiB
no label, UUID=f6d806e4-ffd4-4c20-bd81-bf0b7194b38d
```
4.启用swap
```shell script
swapon ./swap 
```
5.通过命令查看swap是否生效
```shell script
[root@VM_30_103_centos /]# free -m
             total       used       free     shared    buffers     cached
Mem:         32108      23582       8525         30        499      16842
-/+ buffers/cache:       6240      25867
Swap:         9999          0       9999
```

6.设置开机自动启动,通过vim /etc/fstab
```shell script
dev/vda1            /                    ext3       noatime,acl,user_xattr 1 1
proc                 /proc                proc       defaults              0 0
sysfs                /sys                 sysfs      noauto                0 0
debugfs              /sys/kernel/debug    debugfs    noauto                0 0
devpts               /dev/pts             devpts     mode=0620,gid=5       0 0
/dev/vdb1            /root/data           ext4       defaults              0 0
UUID=e7caca55-82c7-4600-a89e-07a475bc45c4 swap       swap    defaults      0 0     
```
**修改完成以后输入: mount -a 即可生效**

### 关于swap常用命令的介绍
```shell script
cat /proc/sys/vm/swappiness；查看swap配置使用多大比例内存之后开始使用swap
swapon -s ;swap当前使用情况
cat /proc/swaps;swap当前使用情况
swapon ./swap;启用swap分区
swapoff /swap；删除swap分区
```

## Linux文件权限
Linux系统中的每个文件和目录都有访问许可权限,Linux使用chmod,chgrp,chown命令来修改文件权限
先来看一下例子
```shell script
-rw-r--r-- 1 root root 483997 Ju1 l5 17:3l tomcat.tgz
```
参数解释:
- 第一个字符是横线表示是一个非目录的文件,如果是d表示是一个目录
- r代表只读 w代表写,x代表可执行

#### chmod命令
chmod命令是非常重要的,用于改变文件或者目录的访问权限
比如我们想修改一个文件的访问权限
```shell script
chmod 777 /var/home/test
```
具体权限说明:
- r(Read读取):权限值是4,对文件而言具有读取文件内容的权限,对目录来说,具有浏览目录的权限
- w(Write写入):权限值2,对文件而言具有新增,修改文件内容的权限,对目录来说,具有删除 移动目录的文件权限
- x(eXecute 执行):权限值1,对文件具有执行文件的权限,对目录来说具有进入目录的权限

具体数值如何计算,我们来看一个例子
```shell script
rwx rw- r-
```
rwx=4+2+1=7
rw-=4+2+0=6
r-=4+0=4

所以rwx rw- r-对应的权限值是764

为某个具体的文件赋予权限
```shell script
chmod 750 test.txt
```

为文件赋予执行权限,a表示所有用户,x表示执行
```shell script
chmod a+x test.txt
```

#### chgrp命令
chgrp用于改变文件或目录所属的组

将/opt/local,/cdn及其目录下的所有文件的组改为cdn
```shell script
chgrp -R cdn /opt/local/ /cdn
```

#### chown命令
chown用于更改某个文件或目录的所属分组和用户

1.把test.txt的所有者改为test
```shell script
chown test test.txt
```
2.把/usr/local目录下及其所有的文件和子目录的拥有者改为admin
```shell script
chown -R admin /usr/local
```
3.把/usr/local目录下及其所有的文件和子目录的所属用户和所属分组改为admin
```shell script
chown -R admin:admin /usr/local
```