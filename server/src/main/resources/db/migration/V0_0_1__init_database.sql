create schema if not exists balances;

set search_path to balances;

create table if not exists balance (
    id bigserial primary key,
    amount bigint
);

insert into balance(amount) values
    (0),
    (5000),
    (10000),
    (50000),
    (100000),
    (500000),
    (1000000);
