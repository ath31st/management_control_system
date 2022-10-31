create table if not exists users
(
    id                      bigserial
        primary key,
    account_non_expired     boolean      not null,
    account_non_locked      boolean      not null,
    credentials_non_expired boolean      not null,
    email                   varchar(100),
    enabled                 boolean      not null,
    firstname               varchar(25),
    lastname                varchar(25),
    password                varchar(255) not null,
    register_date           timestamp,
    username                varchar(100) not null
);

alter table users
    owner to postgres;