--drop table debt.TEST_ORDER
create table debt.TEST_ORDER(
	ORDER_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, ORDER_NO varchar(10)
	, ORDER_TYPE varchar(50)
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
);