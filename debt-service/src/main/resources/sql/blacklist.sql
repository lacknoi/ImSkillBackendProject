--drop TABLE debt.TEST_BLACKLIST
CREATE TABLE debt.TEST_BLACKLIST(
	BLACKLIST_ID int NOT NULL  IDENTITY (1, 1) PRIMARY KEY
	, BA_NO varchar(50)
	, STATUS_CD varchar(50)
	, CREATED datetime
	, CREATED_BY varchar(50)
	, LAST_UPD datetime
	, LAST_UPD_BY varchar(50)
)