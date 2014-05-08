/* Adding notification_receivers */
DROP TABLE IF EXISTS notification_receivers;
create table notification_receivers (
   project_id INTEGER not null,
   person_id INTEGER not null,
   primary key (project_id, person_id)
);

alter table notification_receivers add index (person_id), add constraint FK90D2EE1032DA9A45 foreign key (person_id) references person (id);
alter table notification_receivers add index (project_id), add constraint FK90D2EE108A94A401 foreign key (project_id) references project (id);

/* Add negative permision for creating a new project by admins */
SELECT @permissionExists:=count(*) FROM permission WHERE name = '-create.project';
SELECT @id:=next FROM identifier;
UPDATE identifier SET next = @id + 1;
INSERT INTO permission SELECT @id, role.id, '-create.project', 'system.project',0 from role WHERE @permissionExists = 0 AND role = 'admin';

/* Add negative permision for adding users to editor, only allow sysadmin and admins to add user */
SELECT @permissionCount:=count(*) FROM permission WHERE name = '-create.person';
SELECT @id:=next FROM identifier;
UPDATE identifier SET next = @id + 1;
INSERT INTO permission SELECT @id, role.id, '-create.person', 'system.person', 0 from role WHERE @permissionCount = 0 AND role = 'editor';

/* Remove permision for creating a new project by editor*/
SELECT @permissionExists:=count(*) FROM permission p, role r WHERE p.name = '-create.project' and r.role='editor' and p.principal = r.id;
SELECT @id:=next FROM identifier;
UPDATE identifier SET next = @id + 1;
INSERT INTO permission SELECT @id, role.id, '-create.project', 'system.project',0 from role WHERE @permissionExists = 0 AND role = 'editor';

/* Purge unattached notes */
CREATE temporary table tempnote AS
select n.id, n.attachment_id from note n
left join project p on n.attachedTo_id = p.id
left join iteration i on n.attachedTo_id = i.id
left join story s on n.attachedTo_id = s.id
left join task t on n.attachedTo_id = t.id
where p.id is null and i.id is null and s.id is null and t.id is null;
delete xfile from xfile,tempnote  WHERE xfile.id = tempnote.attachment_id;
delete note from note,tempnote  WHERE note.id = tempnote.id;
drop table tempnote;

/* Change migrated schema to match hibernate generated schema */
alter table datasample change value value double default NULL;
alter table identifier change next nextId integer not null default 1;
alter table integration change id id integer not null default 0;
alter table integration change last_update last_update datetime default NULL;
alter table integration change person_id person_id int(11) default NULL;
alter table integration change project_id project_id int(11) default NULL;
alter table integration change when_complete when_complete datetime default NULL;
alter table integration change when_requested when_requested datetime default NULL;
alter table integration change when_started when_started datetime default NULL;
alter table iteration change description description text;
alter table iteration change end_date end_date date default NULL;
alter table iteration change id id integer not null default 0;
alter table iteration change last_update last_update datetime default NULL;
alter table iteration change name name varchar(255) default NULL;
alter table iteration change project_id project_id int(11) default NULL;
alter table iteration change start_date start_date date default NULL;
alter table note change attachedTo_id attachedTo_id int(11) default NULL;
alter table note change author_id author_id int(11) default NULL;
alter table note change body body text;
alter table note change id id integer not null default 0;
alter table note change last_update last_update datetime default NULL;
alter table note change subject subject varchar(255) default NULL;
alter table note change submission_time submission_time datetime default NULL;
alter table person change email email varchar(255) default NULL;
alter table person change id id integer not null default 0;
alter table person change initials initials varchar(255) default NULL;
alter table person change is_hidden is_hidden tinyint(1) default NULL;
alter table person change last_update last_update datetime default NULL;
alter table person change name name varchar(255) default NULL;
alter table person change password password varchar(255) default NULL;
alter table person change phone phone varchar(255) default NULL;
alter table person change userId userId varchar(255) default NULL;
alter table project change description description text;
alter table project change id id integer not null default 0;
alter table project change is_hidden is_hidden tinyint(1) default NULL;
alter table project change last_update last_update datetime default NULL;
alter table project change name name varchar(255) default NULL;
alter table story change description description text;
alter table story change estimated_hours estimated_hours double default NULL;
alter table story change id id integer not null default 0;
alter table story change iteration_id iteration_id int(11) default NULL;
alter table story change last_update last_update datetime default NULL;
alter table story change name name varchar(255) default NULL;
alter table story change priority priority int(11) default NULL;
alter table story change tracker_id tracker_id int(11) default NULL;
alter table task change acceptor_id acceptor_id int(11) default NULL;
alter table task change created_date created_date date default NULL;
alter table task change description description text;
alter table task change disposition disposition varchar(255) default NULL;
alter table task change estimated_hours estimated_hours double default NULL;
alter table task change id id integer not null default 0;
alter table task change is_complete is_complete tinyint(1) default NULL;
alter table task change last_update last_update datetime default NULL;
alter table task change name name varchar(255) default NULL;
alter table task change original_estimate original_estimate double default NULL;
alter table task change story_id story_id integer not null default 0;
alter table task change type type varchar(255) default NULL;
alter table time_entry change duration duration double default NULL;
alter table time_entry change end_time end_time datetime default NULL;
alter table time_entry change id id integer not null default 0;
alter table time_entry change last_update last_update datetime default NULL;
alter table time_entry change person1_id person1_id int(11) default NULL;
alter table time_entry change person2_id person2_id int(11) default NULL;
alter table time_entry change report_date report_date datetime default NULL;
alter table time_entry change start_time start_time datetime default NULL;
alter table time_entry change task_id task_id int(11) default NULL;
-- alter table task add index (story_id), add constraint FK0035856662D8A5 foreign key (story_id) references story (id);
alter table story add index (customer_id), add constraint FK0015856662D8A5 foreign key (customer_id) references person (id);
alter table story add index (iteration_id), add constraint FK0025856662D8A5 foreign key (iteration_id) references iteration (id);
alter table time_entry add index (task_id), add constraint FK0045856662D8A5 foreign key (task_id) references task (id);

/* New fields */
alter table story add column status char(1) NOT NULL default 'd';
alter table story add column original_estimated_hours double;
alter table story add column disposition char(1) NOT NULL default 'p';
alter table story add column postponed_hours double default 0;
alter table story add column iteration_start_estimated_hours double default 0;
alter table iteration add column status smallint(6) default NULL;
alter table iteration add column days_worked double default NULL;

/* Obsolete fields */
alter table task drop pair_id;

/* Change task disposition to one character enum */
alter table task add column disposition_new char(1) NOT NULL default 'p';
update task set disposition_new = 'c' where disposition = 'disposition.carriedOver';
update task set disposition_new = 'a' where disposition = 'disposition.added';
update task set disposition_new = 'd' where disposition = 'Discovered';
update task set disposition_new = 'o' where disposition = 'Overhead';
alter table task drop column disposition;
alter table task change disposition_new disposition char(1) NOT NULL default 'p';

/* REMOVED DUE TO PROBLEMS DURING TESTING */
-- alter table integration add column person_id integer not null default 0;
-- alter table iteration change end_date end_date date default NULL;
-- alter table iteration change start_date start_date date default NULL;
-- alter table note add constraint PK0105856662D8A5 primary key (id);
-- alter table note drop constraint false;
-- alter table person add primary key(id);


/* Iteration table, default status to 0, which means inactive */
-- todo jm: change the status inactive to 0 and active to 1.
-- update table iteration set status = 2 where status = 1;
-- update table iteration set status = 1 where status = 0;
-- update table iteration set status = 0 where status = 2;

/* Delete obsolete app.version now replaced by patches*/
delete from attribute where name="app.version";