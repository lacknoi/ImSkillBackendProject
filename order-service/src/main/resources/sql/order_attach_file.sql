--drop table debt.TEST_ORDER_ATTACH_FILE
create table debt.TEST_ORDER_ATTACH_FILE(
	ORDER_ATTACH_FILE_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, ORDER_ID int
	, FILE_NAME varchar(50)
	, ORIGINAL_FILE_NAME varchar(50)
	, FILE_TYPE varchar(50)
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
);