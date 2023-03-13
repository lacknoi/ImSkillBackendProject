--drop table debt.TEST_ACCOUNT
create table debt.TEST_ACCOUNT(
	ACCOUNT_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, MASTER_ID int
	, ACCOUNT_NO varchar(50)
	, ACCOUNT_NAME varchar(50)
	, ACCOUNT_LEVEL varchar(50)
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
);