-- 规则
create table ews_rule
(
    rule_id          bigint(20) primary key,
    display_name     varchar(1000),
    rule_desc        varchar(1000),
    rule_level       int(2)        not null,
    rule_type        varchar(100)  not null,
    is_enabled       tinyint(1),
    is_not_supported tinyint(1),
    is_in_error      tinyint(1),
    conditions       varchar(3000) not null,
    actions          varchar(3000) not null,
    item_action_type char(9)       not null,
    item_actions     varchar(2000) not null,
    active           tinyint(1),
    delete_flag      tinyint(1) default 0 check (delete_flag in (0, 1))
);

-- 文件夹
create table ews_mail_folders
(
    ews_folder_id bigint(20) primary key,
    folder_code   varchar(1500) not null,
    folder_name   varchar(2000) not null,
    active        tinyint(1),
    delete_flag   tinyint(1) default 0 check (delete_flag in (0, 1))
);

-- 规则与文件夹 关联表
create table ews_rule_folder_relation
(
    relation_id   bigint(20) primary key,
    rule_id       bigint(20)    not null,
    ews_folder_id bigint(20)    not null,
    folder_id     varchar(3000) not null,
    mail_id       bigint(20)    not null,
    delete_flag   tinyint(1) default 0 check (delete_flag in (0, 1))
);

-- email配置
create table ews_mail_config
(
    mail_id     bigint(20) primary key,
    email       varchar(1500) not null,
    password    varchar(2000) not null,
    topic_id    bigint(20),
    active      tinyint(1),
    delete_flag tinyint(1) default 0 check (delete_flag in (0, 1))
);

-- 邮件收件主题
create table ews_mail_topic
(
    topic_id     bigint(20) primary key,
    topic_name   varchar(2000) not null,
    topic_desc   varchar(3000),
    topic_config varchar(3000),
    active       tinyint(1),
    delete_flag  tinyint(1) default 0 check (delete_flag in (0, 1))
);

-- 主题 规则 关联
create table ews_topic_rule_relation
(
    relation_id bigint(20) primary key,
    topic_id    bigint(20) not null,
    rule_id     bigint(20) not null,
    rule_level  int(2)     not null,
    priority    int(3)     not null,
    delete_flag tinyint(1) default 0 check (delete_flag in (0, 1))
);

-- 监听 (先用表来做,可以改为缓存处理)
create table ews_subscription
(
    ews_subscription_id  bigint(20) primary key,
    subscription_id      varchar(2000) not null,
    subscription_key     varchar(2000) not null,
    subscription_minutes int(4)        not null,
    subscription_date    timestamp  default current_timestamp,
    delete_flag          tinyint(1) default 0 check (delete_flag in (0, 1))
)