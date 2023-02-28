FROM centos:8
MAINTAINER MHL<mh@minghua.li>

RUN rm -f /etc/yum.repos.d/*.repo
RUN curl -o /etc/yum.repos.d/Centos-8.repo https://mirrors.aliyun.com/repo/Centos-8.repo

WORKDIR /root
RUN curl -o /root/natapp https://cdn.natapp.cn/assets/downloads/clients/2_3_9/natapp_linux_386/natapp?version=20220415
RUN yum install -y socat java-17-openjdk

ENV MAIL_HOST=smtp.163.com
ENV MAIL_PORT=465
ENV MAIL_FROM=natapp_notify@163.com
ENV MAIL_USER=natapp_notify@163.com
ENV MAIL_AUTH=##
ENV MAIL_TO=mh@minghua.li
ENV LOG_FILE=/root/log
ENV NATAPP_AUTH_TOKEN=##
ENV HOST_IP=172.17.0.1
ENV HOST_PORT=22

