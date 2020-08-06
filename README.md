# dbg-project
这是个个人练习用项目，用于学习新的技术和者实现一些自己的想法。

### 说明
* 项目使用 spring boot 2.1.6.RELEASE 开发
* 模块下的 build.sh 文件用于打包当前模块、构建 docker 镜像并上传到私有镜像仓库。
* 模块下的 docker 目录包含 Dockerfile 和 run.sh。run.sh 是容器启动后运行的脚本
* 需要手动创建数据库，执行 dump.sql 导入数据
* [在线演示](https://dbg-dev.icu:9700/) 账号：reader 密码：12345678 。不能重复登录，重复登录先登录的会被踢下线

### 依赖
* mysql >= 5.7
* redis >= 5.0.5

### dbg-common
目前未包含任何实现

### dbg-domain
存放数据实体类，对应数据库的表，目前只包含 dbg-service-admin 模块的实体类

### dbg-interface-rpc
远程过程调用接口定义，可以通过 rpc 调用的接口将在这里定义

### dbg-service-admin
这是本项目的核心模块，包含登录、用户管理、系统权限管理、文件管理（未实现）、B站直播弹幕获取

### dbg-service-blog
简单的博客后台，权限通过http请求注册到系统（注册接口由 dbg-service-admin 提供）

### dbg-service-content
测试 spring cloud alibaba 的远程调用(服务提供者)

### dbg-service-gateway
gateway 测试

### dbg-service-upload
测试 spring cloud alibaba 的远程调用(服务消费者)