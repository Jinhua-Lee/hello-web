create table if not exists usr
(
    id int auto_increment
    primary key,
    name varchar(50) null,
    pwd varchar(50) null,
    sex varchar(50) null,
    home varchar(50) null,
    info varchar(255) null
);