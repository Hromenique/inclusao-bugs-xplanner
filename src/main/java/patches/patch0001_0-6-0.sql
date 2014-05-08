create table datasample (
   sampleTime DATETIME not null,
   referenceId INTEGER not null,
   aspect VARCHAR(255) not null,
   value DOUBLE PRECISION,
   primary key (sampleTime, referenceId, aspect)
);
create table person_role (
   role_id INTEGER not null,
   person_id INTEGER not null,
   project_id INTEGER not null,
   primary key (role_id, person_id, project_id)
);
create table time_entry (
   id INTEGER not null,
   last_update DATETIME,
   start_time DATETIME,
   end_time DATETIME,
   duration DOUBLE PRECISION,
   person1_id INTEGER,
   person2_id INTEGER,
   task_id INTEGER,
   report_date DATETIME,
   primary key (id)
);
create table person (
   id INTEGER not null,
   last_update DATETIME,
   name VARCHAR(255),
   email VARCHAR(255),
   phone VARCHAR(255),
   initials VARCHAR(255),
   userId VARCHAR(255),
   password VARCHAR(255),
   is_hidden BIT,
   primary key (id)
);
create table xdir (
   id INTEGER not null,
   name VARCHAR(255),
   parent_id INTEGER,
   primary key (id)
);
create table story (
   id INTEGER not null,
   last_update DATETIME,
   name VARCHAR(255),
   description TEXT,
   iteration_id INTEGER,
   tracker_id INTEGER,
   estimated_hours DOUBLE PRECISION,
   priority INTEGER,
   customer_id INTEGER,
   primary key (id)
);
create table iteration (
   id INTEGER not null,
   last_update DATETIME,
   project_id INTEGER,
   name VARCHAR(255),
   description TEXT,
   start_date DATE,
   end_date DATE,
   primary key (id)
);
create table note (
   id INTEGER not null,
   attachedTo_id INTEGER,
   author_id INTEGER,
   subject VARCHAR(255),
   body TEXT,
   submission_time DATETIME,
   last_update DATETIME,
   attachment_id INTEGER,
   primary key (id)
);
create table project (
   id INTEGER not null,
   last_update DATETIME,
   name VARCHAR(255),
   description TEXT,
   is_hidden BIT,
   primary key (id)
);
create table integration (
   id INTEGER not null,
   project_id INTEGER,
   last_update DATETIME,
   person_id INTEGER,
   when_started DATETIME,
   when_requested DATETIME,
   when_complete DATETIME,
   state CHAR(1),
   comments VARCHAR(255),
   primary key (id)
);
create table permission (
   id INTEGER not null,
   principal INTEGER,
   name VARCHAR(255),
   resource_type VARCHAR(255),
   resource_id INTEGER,
   primary key (id)
);
create table role (
   id INTEGER not null,
   role VARCHAR(255) unique,
   lft INTEGER,
   rgt INTEGER,
   primary key (id)
);
create table history (
   id INTEGER not null,
   when_happened DATETIME,
   container_id INTEGER,
   target_id INTEGER,
   object_type VARCHAR(255),
   action VARCHAR(255),
   description VARCHAR(255),
   person_id INTEGER,
   notified BIT,
   primary key (id)
);
create table task (
   id INTEGER not null,
   last_update DATETIME,
   name VARCHAR(255),
   type VARCHAR(255),
   description TEXT,
   disposition VARCHAR(255),
   acceptor_id INTEGER,
   pair_id INTEGER,
   created_date DATE,
   estimated_hours DOUBLE PRECISION,
   original_estimate DOUBLE PRECISION,
   is_complete BIT,
   story_id INTEGER not null,
   primary key (id)
);
create table xfile (
   id INTEGER not null,
   name VARCHAR(255),
   content_type VARCHAR(255),
   data LONGBLOB,
   file_size BIGINT,
   dir_id INTEGER,
   primary key (id)
);
alter table time_entry add index (task_id), add constraint FK42EDB080A45F9215 foreign key (task_id) references task (id);
alter table xdir add index (parent_id), add constraint FK3811157B66B0D0 foreign key (parent_id) references xdir (id);
alter table story add index (customer_id), add constraint FK68AF8F596607D1C foreign key (customer_id) references person (id);
alter table story add index (iteration_id), add constraint FK68AF8F5151239BD foreign key (iteration_id) references iteration (id);
alter table iteration add index (project_id), add constraint FK8904EEDD8A94A401 foreign key (project_id) references project (id);
alter table task add index (story_id), add constraint FK3635856662D8A5 foreign key (story_id) references story (id);
alter table xfile add index (dir_id), add constraint FK6CAF9F4B0A1860D foreign key (dir_id) references xdir (id);

create table identifier (
    next INTEGER 
);
