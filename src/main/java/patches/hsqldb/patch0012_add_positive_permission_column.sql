alter table permission add column positive bit default 1;

update permission set positive = 0, name = SUBSTRING(name, 2) where name like '-%';