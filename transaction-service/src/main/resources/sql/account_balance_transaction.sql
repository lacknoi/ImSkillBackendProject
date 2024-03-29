--drop table debt.TEST_ACCOUNT_BALANCE_TRANSACTION
create table debt.TEST_ACCOUNT_BALANCE_TRANSACTION(
	TRANSACTION_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, ACCOUNT_NO varchar(50)
	, ACTION_TYPE varchar(50)
	, TRANSACTION_NO varchar(50)
	, REQUEST_AMOUNT numeric(16, 2) NOT NULL
	, TOTAL_BALANCE numeric(16, 2) NOT NULL
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
);