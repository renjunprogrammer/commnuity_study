create table user
(
    id         int auto_increment
        primary key,
    name       varchar(50)  null,
    account_id varchar(100) null,
    token      varchar(50)  null,
    gmt_create mediumtext   null,
    gmt_update mediumtext   null
);

