INSERT INTO PERSON_ROLE VALUES(5,1,0);
INSERT INTO PERSON VALUES(1,'2005-12-07','sysadmin','no@reply.com',NULL,'SYS','sysadmin','1tGWp1Bdm02Sw4bD7/o0N2ao405Tf8kjxGBW/A==',0); // password 'admin'
INSERT INTO PERMISSION VALUES(6,5,'%','%',0);
INSERT INTO PERMISSION VALUES(7,3,'-create.project','system.project',0);
INSERT INTO PERMISSION VALUES(8,3,'-create.person','system.person',0);
INSERT INTO PERMISSION VALUES(9,4,'-create.project','system.project',0);
INSERT INTO PERMISSION VALUES(10,4,'admin%','%',0);
INSERT INTO PERMISSION VALUES(11,3,'create%','%',0);
INSERT INTO PERMISSION VALUES(12,3,'edit%','%',0);
INSERT INTO PERMISSION VALUES(13,3,'integrate%','%',0);
INSERT INTO PERMISSION VALUES(14,3,'delete%','%',0);
INSERT INTO PERMISSION VALUES(15,2,'read%','%',0);
INSERT INTO ROLE VALUES(2,'viewer',1,8);
INSERT INTO ROLE VALUES(3,'editor',2,7);
INSERT INTO ROLE VALUES(4,'admin',3,6);
INSERT INTO ROLE VALUES(5,'sysadmin',4,5);
INSERT INTO IDENTIFIER VALUES(16);
