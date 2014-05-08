-- person
-- todo jm verify that it should not be userId since the hibernate created schema only has it.
alter table person add UNIQUE userId (userId);