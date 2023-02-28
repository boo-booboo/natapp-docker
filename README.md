# 简单介绍

这是一个简单的基于docker的使用[NATAPP](https://natapp.cn)服务的TCP内网穿透客户端,原理是利用了容器连接natapp服务,并将TCP请求转发到宿主机指定的端口.

当服务重启或端口更新时,会自动爬取最新的端口发送给指定的邮箱.

依赖: docker, docker-compose, JDK 17, Maven

同时需要提供SMTP邮箱账户和NATAPP免费TCP隧道的TOKEN.

NATAPP免费TCP隧道的本机地址为**127.0.0.1**, 端口配置为**22**.

# 使用方式

## 1. 编译jar

```shell
sudo docker-compose -f docker-compose-build.yml pull
sudo docker-compose -f docker-compose-build.yml up
```

## 2. 配置环境参数

将dockerfile/env.example拷贝为dockerfile/.env,将里面的配置改为自己的配置

```properties
# 发邮件服务的SMTP配置,建议申请一个免费的网易邮箱,开通SMTP服务
MAIL_HOST=smtp.163.com
MAIL_PORT=465
MAIL_FROM=natapp_notify@163.com
MAIL_USER=natapp_notify
MAIL_AUTH= # SMTP authentication key from mail server
# 接受邮件的邮箱地址,请填自己的
MAIL_TO=someone@example.com
# natapp的日志文件地址,不需要改
LOG_FILE=/root/log
# natapp的隧道token,需要自己去natapp.cn注册申请一个免费的TCP隧道
NATAPP_AUTH_TOKEN= # free natapp token from NATAPP.CN
# 主机监听端口
HOST_PORT=22
```

## 3. 执行docker-compose

执行docker-compose

```shell
# cd dockerfile
sudo docker-compose build
sudo docker-compose up -d
```
