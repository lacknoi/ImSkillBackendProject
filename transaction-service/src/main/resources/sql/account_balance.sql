--drop table debt.TEST_ACCOUNT_BALANCE
create table debt.TEST_ACCOUNT_BALANCE(
	ACCOUNT_BALANCE_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, ACCOUNT_NO varchar(50)
	, TOTAL_BALANCE numeric(16, 2) NOT NULL
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
);