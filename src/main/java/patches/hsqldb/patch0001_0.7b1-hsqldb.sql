
create table datasample (sampleTime TIMESTAMP not null, referenceId INTEGER not null, aspect VARCHAR(255) not null, value DOUBLE, primary key (sampleTime, referenceId, aspect));
create table person_role (role_id INTEGER not null, person_id INTEGER not null, project_id INTEGER not null, primary key (role_id, person_id, project_id));
create table attribute (targetId INTEGER not null, name VARCHAR(255) not null, value VARCHAR(255), primary key (targetId, name));
create table time_entry (id INTEGER not null, last_update TIMESTAMP, start_time TIMESTAMP, end_time TIMESTAMP, duration DOUBLE, person1_id INTEGER, person2_id INTEGER, task_id INTEGER, report_date TIMESTAMP, primary key (id));
create table person (id INTEGER not null, last_update TIMESTAMP, name VARCHAR(255), email VARCHAR(255), phone VARCHAR(255), initials VARCHAR(255), userId VARCHAR(255) not null, password VARCHAR(255), is_hidden BIT, primary key (id));
create table xdir (id INTEGER not null, name VARCHAR(255), parent_id INTEGER, primary key (id));
create table story (id INTEGER not null, last_update TIMESTAMP, name VARCHAR(255), description VARCHAR(4000), iteration_id INTEGER, tracker_id INTEGER, estimated_hours DOUBLE, original_estimated_hours DOUBLE, postponed_hours DOUBLE, iteration_start_estimated_hours DOUBLE, priority INTEGER, disposition CHAR(1), status CHAR(1), customer_id INTEGER, primary key (id));
create table iteration (id INTEGER not null, last_update TIMESTAMP, project_id INTEGER, name VARCHAR(255), status SMALLINT, description VARCHAR(4000), start_date DATE, end_date DATE, days_worked DOUBLE not null, primary key (id));
create table note (id INTEGER not null, attachedTo_id INTEGER, author_id INTEGER, subject VARCHAR(255), body VARCHAR(4000), submission_time TIMESTAMP, last_update TIMESTAMP, attachment_id INTEGER, primary key (id));
create table project (id INTEGER not null, last_update TIMESTAMP, name VARCHAR(255), is_hidden BIT, description VARCHAR(4000), primary key (id));
create table integration (id INTEGER not null, project_id INTEGER, last_update TIMESTAMP, person_id INTEGER, when_started TIMESTAMP, when_requested TIMESTAMP, when_complete TIMESTAMP, state CHAR(1), comments VARCHAR(255), primary key (id));
create table permission (id INTEGER not null, principal INTEGER, name VARCHAR(255), resource_type VARCHAR(255), resource_id INTEGER, primary key (id));
create table role (id INTEGER not null, role VARCHAR(255), lft INTEGER, rgt INTEGER, primary key (id),  unique (role));
create table history (id INTEGER not null, when_happened TIMESTAMP, container_id INTEGER, target_id INTEGER, object_type VARCHAR(255), action VARCHAR(255), description VARCHAR(255), person_id INTEGER, notified BIT, primary key (id));
create table notification_receivers (project_id INTEGER not null, person_id INTEGER not null, primary key (project_id, person_id));
create table task (id INTEGER not null, last_update TIMESTAMP, name VARCHAR(255), type VARCHAR(255), description VARCHAR(4000), disposition CHAR(1), acceptor_id INTEGER, created_date DATE, estimated_hours DOUBLE, original_estimate DOUBLE, is_complete BIT, story_id INTEGER not null, primary key (id));
create table xfile (id INTEGER not null, name VARCHAR(255), content_type VARCHAR(255), data LONGVARBINARY, file_size BIGINT, dir_id INTEGER, primary key (id));

alter table time_entry add constraint FK42EDB080A45F9215 foreign key (task_id) references task;
alter table xdir add constraint FK3811157B66B0D0 foreign key (parent_id) references xdir;
alter table story add constraint FK68AF8F596607D1C foreign key (customer_id) references person;
alter table story add constraint FK68AF8F5151239BD foreign key (iteration_id) references iteration;
alter table iteration add constraint FK8904EEDD8A94A401 foreign key (project_id) references project;
alter table notification_receivers add constraint FK90D2EE1032DA9A45 foreign key (person_id) references person;
alter table notification_receivers add constraint FK90D2EE108A94A401 foreign key (project_id) references project;
alter table task add constraint FK3635856662D8A5 foreign key (story_id) references story;
alter table xfile add constraint FK6CAF9F4B0A1860D foreign key (dir_id) references xdir;
alter table person add constraint userId unique (userId);

create table identifier ( nextId INTEGER );
