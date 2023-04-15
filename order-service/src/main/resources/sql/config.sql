--drop table debt.TEST_CONFIG
create table debt.TEST_CONFIG(
	CONFIG_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, TYPE varchar(50)
	, KEYWORD varchar(50)
	, VALUE varchar(100)
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
);