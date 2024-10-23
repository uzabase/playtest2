create schema if not exists acme;

drop table if exists acme.i;
create table acme.i (
  i int
);

drop table if exists acme.d;
create table acme.d (
  d decimal
);

drop table if exists acme.uid;
create table acme.uid (
  uid uuid
);

drop table if exists acme.str;
create table acme.str (
  str varchar(255)
);

drop table if exists acme.bool;
create table acme.bool (
  bool boolean
);

insert into acme.i (i)
values (1), (42);

insert into acme.d (d)
values
(1.0),
--('NaN'::NUMERIC),
(3.3)
;

insert into acme.uid (uid)
values ('56af9408-9063-11ef-bee3-7f726de76f14');

insert into acme.str (str)
values ('hello'), ('');

insert into acme.bool (bool)
values (true), (false), (null);