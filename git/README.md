# Git从原理到实战

## 主要内容介绍

* [什么是Git](#what's git)
* [Git与传统版本控制系统的比较](#compare-verson-control)
   * CVS
   * SVN
* [Git安装](#install git)
* [Git通用设置](#install git)
* [Git提交代码原理](#commit code)
* [Git分支](#git_branch)
* [Git命令介绍](#git_command)
   * 常用的操作命令
   * tag相关命令
   * 日志相关命令
* [客户端工具介绍](#git_client) 

## 什么是Git?
> git是分布式版本控制系统,支持多种协议如https,ssh,通过ssh支持的原生git协议速度快

### Git与传统版本控制系统的比较  
#### Git
- git是用C语言开发的分布式版本控制系统,安全性高,不需要联网,没有中央服务器,有强大的分支管理,Git有点像异地多活.

#### CVS/SVN
- CVS/SVN是集中式版本控制系统,pull/push必须要联网
- 安全性不高,每次都需要连接网络
- 如果服务器磁盘坏了就无法使用

## Git安装
### 客户端Git安装
点击http://git-scm.com/download/  下载与自己操作系统的版本即可,完成安装即可.

### 服务器端Git安装:
1.安装Git,执行下面命令:
```java
yum install -y git
```
2.创建git用户  
```java
groupadd git  //创建用户组
adduser git  -g git //为Linux添加用户
passwd git; 修改密码
```
3.创建证书登录  把所有公钥导入到/home/git/.ssh/authorized_keys文件里
4.初始化git仓库 先选定一个目录作为Git仓库 假定/git/repo/ 在/repo目录下面输入命令:
```java
git init --bare test-demo.git
```
5.修改仓库权限
```java
chown -R git:git test.git
```
6.禁用shell登录
第二步创建的git账户不允许登录shell，可以通过/etf/passwd文件来完成
```java
 git:x:505:507::/home/git:/bin/bash
```
修改为:
```java
git:x:505:507::/home/git:/usr/bin/git-shell
```
7.克隆远程仓库
```java
 git clone git@hostIp:/git/repo/test-demo.git
```
8.如何提交代码不需要密码
```shell script
cd /home/git/
mkdir .ssh
touch .ssh/authorized_keys
cp ~/.ssh/id_rsa.pub  ./.ssh/    //将公钥拷贝到/home/git/.ssh目录下面
cat id_rsa.pub >> authorized_keys  //将公钥拷贝到authorized_keys文件中
```
最后将本地的id_rsa.pub写入authorized_keys文件中
至此已完成服务器搭建!

## Git通用设置
当安装完Git以后,可以对全局进行设置,比如设置邮箱,用户名等信息
- 查询目前git的全局配置
```shell script
git config --list
```

- 可以设置用户名和邮箱等信息
```shell script
git config --global user.name "Jason0104" //设置用户名
git config --global user.email "Jason0104@163.com" //设置邮箱
```

- 设置别名
> git的命令很多,有的时候因为单词太长会敲错,所以可以通过设置别名来简化git命令的输入

下面为不同命令分别配置别名:
```shell script
git config --global alias.co checkout
git config --global alias.ci commit
git config --global alias.br branch
```
这个时候我们可以通过命令:
```shell script
git s;//实际效果就是git status
git co;//git checkout
git br://git branch
```
配置Git的时候,加上--global是针对当前用户起作用的,如果不加只针对当前的仓库起作用
那么我们前面配置的文件都放到哪里了?
默认是放在当前用户主目录下的一个隐藏文件 ~/.gitconfig中

vim ~/.gitconfig可以看到配置的别名已经在.gitconfig中:
```
[user]
        name = Jason0104
        email = "Jason01042@163.com"

//中间内容省略
[alias]
        s = status
        co = checkout
        ci = commit
        br = branch
```

## Git提交代码原理
我们把Git工作区域划分为三个部分:
- Working Directory(工作目录)
- Index(也叫Stage区 暂存区):当执行git add命令就会进入到暂存区
- HEAD(.git Directory):当执行git commit就会到HEAD指向到最近的一次提交

### 下面我来简单描述一下代码提交的过程:
我们本地代码是在working directory,当本地修改完代码并完成测试要提交到远程仓库
执行git add fileName 这个时候进入stage
执行git commit -m "commit" 进入到HEAD

## Git分支
> Git分支Git非常重要的一个特性,Git创建一个分支很快,除了增加一个指针外,修改HEAD的指向,工作区的文件都没有任何变化
Git分支的创建 删除都是通过指针来完成的
下面我们来看一下具体分支操作命令:

从当前分支创建一个新分支dev
```shell script
git checkout -b newFeature;
```
从当前分支切换到新分支
```shell script
git checkout newFeature;
```
还原当前分支修改的文件
```shell script
git checkout -- fileName;
```
查看本地有哪些分支
```shell script
git branch
```
删除本地分支
```shell script
git branch -d newFeature;
```
查看本地和包括远程所有的分支:
```shell script
git branch -a;
```
修改远程分支的名称
```shell script
git branch -m old_branchName new_branchName
```
   
## Git命令介绍
* 常用的操作命令
* tag命令
* 日志相关命令

### 常用的操作命令
初始化git仓库
```shell script
git init
```
从远程仓库拉取代码:
```shell script
git clone git@github/test.git
```
将本地项目与远程仓库关联
```shell script
git remote add origin git@github/test.git
```
获取所有远程分支
```shell script
git fetch
```
查看远程仓库信息
```shell script
git remote -v
```
将本地所有文件添加到Stage区
```shell script
git add .  //当前目录的所有文件
git add *  //所有文件
```
把暂存区的内容提交到当前分支,这个命令执行完以后HEAD指针就会指向本次提交的commitId,但是不会同步到远程仓库
```shell script
git commit -m "commit message"
```
提交到远程仓库
```shell script
git push -u origin master;//第一次提交加参数-u
git push origin master; //将代码提交到远程仓库
```
从远程仓库拉取代码
```shell script
git pull origin master
```
查看本地目录状态
```shell script
git status
```
### Git文件状态
当我们使用git status命令时,会看到几种类型的状态
- Untracked:未添加到版本控制系统中
- Unmodified:修改文件
- Modified：已经修改过
- unstage: 文件未提交到远程仓库
- staged: 文件提交到远程仓库

比较两个分支的代码差异
```shell script
git diff oldBranch newBranch
```

#### 关于代码回退
> 通过前面的介绍我们都知道HEAD指向的是当前最新分支代码

我们在提交代码的时候都会看到一串"e475afc93c2",这个就是commitId
commitId是用SHA进行加密的,用于区分每一次提交的唯一性

如果我们已经把代码提交了,但是又想回到上一次提交的代码的版本
```shell script
git reset --hard e475afc93c2;//指定回退到某个一个版本,e475afc93c2为commitId
git reset --hard HEAD^;//回退到上一个版本
```
把暂存区的修改撤销掉,重新放回工作区
```shell script
git reset HEAD fileName
```

很多时候我们有一个场景,当前分支是master,想从远程仓库拉取master与本地文件的修改有冲突
这个时候需要先把本地的内容修改隐藏掉

隐藏本地修改文件
```shell script
git stash
```
当同步完远程仓库以后需要显示本地文件
```shell script
git stash pop
```
如果要在master分支合并dev分支代码
```shell script
git merge dev //Fast-forward 表示快进模式
```
### tag命令
> tag有点milestone的概念,当项目开发到一个版本的时候,需要记录里程杯，比如我开发项目到V1.0,开发V2.0的
时候会打一个tag,标签功能有点类似分支

查看所有标签
```shell script
git tag
```
查看具体标签信息
```shell script
git show v1.0
```
新建一个标签
```shell script
git tag v1.0
```
添加指定标签信息
```shell script
git tag -a v1.0 -m "add"
```
推送本地标签
```shell script
git push origin v1.0
```
推送本地全部未推送的本地标签
```shell script
git push origin --tags
```
删除标签
```shell script
git tag -d v1.0
```

### 日志相关命令
> 关于日志可以很好的查看提交历史,以便确定回退到哪个版本

查看提交历史
```shell script
git log
```
查看某个作者的提交历史
```shell script
git log --author=Jason0104
```

美化日志格式
```shell script
git log --pretty=oneline
```
以图形化显示日志
```shell script
git log --graph
```
用于记录你的每一次命令
```shell script
git reflog
```
想了解更多git log的使用可以通过下面命令了解
```shell script
git log --help
```

## 客户端工具介绍
常用的Git客户端图形化工具很多:
- Source Tree(http://www.sourcetreeapp.com/)
- GUI Client(https://git-scm.com/download/)
- GitBox(AppStore)
- IDEA自带Git图形化插件