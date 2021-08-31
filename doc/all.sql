#test table
drop table if exists `test20210608-2`;
create table `test20210608-2`
(`id` bigint not null comment '用户id',
 `name` varchar(50) comment '用户名称',
 `password` varchar(50) comment '密码',
 primary key (`id`))
    engine = innodb default charset = utf8mb4 comment '测试';

insert into `test20210608-2` (id, name, password) values (1,'test','password');

drop table if exists `demo2`;
create table `demo2`
(`id` bigint not null comment '用户id',
 `name` varchar(50) comment '用户名称',
 primary key (`id`))
    engine = innodb default charset = utf8mb4 comment '测试';

insert into `demo2` (id, name) values (1,'test');


#ebook table
drop table if exists `ebook`;
create table `ebook`
(`id` bigint not null comment 'id',
 `name` varchar(50) comment '名称',
 `category1_id` bigint comment '分类1',
 `category2_id` bigint comment '分类2',
 `description` varchar(200) comment '描述',
 `cover` varchar(200) comment '封面',
 `doc_count` int comment '文档数',
 `view_count` int comment '阅读数',
 `vote_count` int comment '点赞数',
 primary key (`id`))
    engine = innodb default charset = utf8mb4 comment='电子书';

insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (1,'TypeScript入门教程','TypeScript和JavaScript是什么关系呢','/image/cover1.png',10,100,1000);
insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (2,'JavaScript入门教程','JavaScript和Java是什么关系呢','/image/cover2.png',20,12,13);
insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (3,'SpringBoot入门教程','SpringBoot你会用吗，包你学了还是不会','/image/cover3.jpg',111,222,333);
insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (4,'Vue入门教程','Vue带你写页面','/image/cover4.jpg',32439,2345,2312);
insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (5,'Java入门教程','如果月薪2000以下的你想提高收入','image/cover5.jpg',23924,8888888,999999999);
insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (6,'MySql入门教程','学了就是数据砖头工','image/cover6.jpg',-1,-2,-3);
insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (7,'橘猪染色教程','想拥有一只橘橘胖胖的猪吗','image/cover7.jpg',45546,8888856,55555555);
insert into `ebook` (id, name, description, cover, doc_count, view_count, vote_count) values (8,'郭毛毛养成教程','吃喝睡玩每一天','image/cover8.png',43566,0,0);


#category table
drop table if exists `category`;
create table `category`
(`id` bigint not null comment 'id',
 `parent` bigint not null default 0 comment '父id',
 `name` varchar(50) not null comment '名称',
 `sort` int comment '顺序',
  primary key (`id`)
) engine = innodb default  charset = utf8mb4 comment='分类表';

insert into `category` (id, parent, name, sort) values (100, 000, '前端开发', 100);
insert into `category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `category` (id, parent, name, sort) values (102, 100, 'Html', 102);
insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, '基础应用', 201);
insert into `category` (id, parent, name, sort) values (202, 200, '框架应用', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'MySql', 201);
insert into `category` (id, parent, name, sort) values (301, 300, '数据分析师', 301);
insert into `category` (id, parent, name, sort) values (302, 300, '数据工程师', 302);
insert into `category` (id, parent, name, sort) values (400, 000, '郭毛毛', 400);


#document table
drop table if exists `doc`;
create table `doc`
(`id` bigint not null comment 'id',
 `parent` bigint not null default 0 comment '父id',
 `ebook_id` bigint not null default 0 comment '电子书id',
 `name`varchar(50) not null comment '名称',
 `sort` int comment '顺序',
 `view_count` int not null default 0 comment '阅读数',
 `vote_count` int not null default 0 comment '点赞数',
 primary key (`id`)
) engine = innodb default  charset = utf8mb4 comment='文档表';

insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (1, 0, 7, '文档1', 1, 0, 0);
insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (2, 1, 7, '文档1.1', 2, 0, 0);
insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (3, 1, 7, '文档1.2', 3, 0, 0);
insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (4, 0, 7, '文档2', 4, 0, 0);
insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (5, 4, 7, '文档2.1', 5, 0, 0);
insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (6, 4, 7, '文档2.2', 6, 0, 0);
insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (7, 0, 7, '文档3', 7, 0, 0);
insert into `doc` (id, parent, ebook_id, name, sort, view_count, vote_count) values (8, 7, 7, '文档3.1', 8, 0, 0);

#content table
drop table if exists `content`;
create table `content`
(`id` bigint not null comment 'id',
`content` mediumtext not null comment '内容',
primary key(`id`)
)engine = innodb default charset = utf8mb4 comment = '文档内容表';


#user table
drop table if exists `user`;
create table `user`
(`id` bigint not null comment 'id',
`login_name` varchar(50) not null comment '登录名',
`nickname` varchar(50) not null comment '昵称',
`password` char(32) not null comment '密码',
primary key (`id`),
unique key `login_name_unique` (`login_name`)
)engine = innodb default charset = utf8mb4 comment = '用户登录表';

insert into `user` (id, login_name, nickname, password) values (1, 'test', '测试', 'test1111');

#data update
update
    ebook t1,
(select
    ebook_id,
    count(1) doc_count,
    sum(view_count) view_count,
    sum(vote_count) vote_count
from
     doc
group by
    ebook_id) t2
set t1.doc_count = t2.doc_count,
    t1.view_count = t2.view_count,
    t1.vote_count = t2.vote_count
where t1.id = t2.ebook_id

