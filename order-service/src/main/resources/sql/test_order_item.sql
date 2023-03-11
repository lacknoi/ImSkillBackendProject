--drop table debt.TEST_ORDER_ITEM
create table debt.TEST_ORDER_ITEM(
	ORDER_ITEM_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, ORDER_ID int
	, ORDER_NO varchar(10)
	, ACCOUNT_ID varchar(50)
	, ACCOUNT_NAME varchar(50)
	, ACCOUNT_LEVEL varchar(50)
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
);