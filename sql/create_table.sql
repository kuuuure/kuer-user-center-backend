create database if not exists universe ;
use universe;

-- auto-generated definition
create table user
(
    username     varchar(256)                       null comment '用户昵称',
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    email        varchar(512)                       null comment '邮箱',
    userPassword varchar(256)                       null comment '密码',
    phone        varchar(128)                       null comment '电话',
    userStatus   tinyint  default 0                 not null comment ' 状态 0-正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     int      default 0                 not null,
    userRole     int      default 0                 not null comment ' 用户权限 普通用户 0   管理员 1  VIP 2'
)
    comment '用户';


