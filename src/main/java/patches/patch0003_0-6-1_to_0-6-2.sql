DROP TABLE IF EXISTS attribute;

CREATE TABLE attribute (
  targetId int(11) NOT NULL,
  name varchar(255) NOT NULL,
  value varchar(255),
  PRIMARY KEY  (targetId,name)
);
