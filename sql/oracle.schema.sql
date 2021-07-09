/**
  全部表需要更改pk长度
 */

-- 规则
create table ews_rule
(
    rule_id          varchar2(1000) primary key,
    display_name     varchar2(1000),
    rule_desc        varchar2(1000),
    rule_level       number(2)      not null,
    rule_type        varchar2(100)  not null,
    is_enabled       number(1),
    is_not_supported number(1),
    is_in_error      number(1),
    conditions       varchar2(3000) not null,
    actions          varchar2(3000) not null,
    item_action_type char(9)        not null,
    item_actions     varchar2(2000) not null,
    delete_flag      number(1) default 0 check (delete_flag in (0, 1))
);

-- 文件夹
create table ews_mail_folders
(
    ews_folder_id varchar2(1000) primary key,
    folder_code   varchar2(1500) not null unique,
    folder_name   varchar2(2000) unique,
    delete_flag   number(1) default 0 check (delete_flag in (0, 1))
);

-- 规则与文件夹 关联表
create table ews_rule_folder_relation
(
    relation_id   varchar2(1000) primary key,
    rule_id       varchar2(1000) not null,
    ews_folder_id varchar2(1000) not null,
    folder_id     varchar2(3000) not null,
    mail_id       varchar2(1000) not null,
    delete_flag   number(1) default 0 check (delete_flag in (0, 1))
);

-- email配置
create table ews_mail_config
(
    mail_id     varchar2(1000) primary key,
    email       varchar2(1500) not null unique,
    password    varchar2(2000) not null,
    topic_id    varchar2(1000),
    delete_flag number(1) default 0 check (delete_flag in (0, 1))
);

-- 邮件收件主题
create table ews_mail_topic
(
    topic_id     varchar2(1000) primary key,
    topic_name   varchar2(2000) not null,
    topic_desc   varchar2(3000),
    topic_config varchar2(3000),
    delete_flag  number(1) default 0 check (delete_flag in (0, 1))
);

-- 主题 规则 关联
create table ews_topic_rule_relation
(
    relation_id varchar2(1000) primary key,
    topic_id    varchar2(1000) not null,
    rule_id     varchar2(1000) not null,
    rule_level  varchar2(1000) not null,
    priority    number(3)      not null,
    delete_flag number(1) default 0 check (delete_flag in (0, 1))
);

-- 监听 (先用表来做,可以改为缓存处理)
create table ews_subscription
(
    ews_subscription_id  varchar2(1000) primary key,
    subscription_id      varchar2(2000) not null,
    subscription_key     varchar2(2000) not null,
    subscription_minutes number(4)      not null,
    subscription_date    timestamp default current_timestamp,
    delete_flag          number(1) default 0 check (delete_flag in (0, 1))
)