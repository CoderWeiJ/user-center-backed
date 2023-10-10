

# 数据库
```mysql
# 创建数据库
CREATE DATABASE if not exists weijian;

# 建表
DROP TABLE if exists user;

create table user
(
    id           bigint auto_increment comment '主键',
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '登录账号',
    avatarUrl    varchar(1024)                      null comment '头像url',
    gender       tinyint                            null comment '性别：0-女，1-难',
    userPassword varchar(256)                       null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(128)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态：0-正常，1-异常',
    createdTime  datetime default CURRENT_TIMESTAMP not null comment '数据插入时间',
    updatedTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '数据更新时间',
    deleted      tinyint  default 0                 not null comment '逻辑删除：0-否，1-删除',
    constraint user_pk
        primary key (id)
)
    comment '用户表';
```