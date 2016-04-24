DROP TABLE IF EXISTS appUser;
DROP TABLE IF EXISTS appUserPassword;
DROP TABLE IF EXISTS Tags;
DROP TABLE IF EXISTS Friends;
DROP TABLE IF EXISTS Airports;

CREATE TABLE appUser(P_Id int NOT NULL, 
     username varchar(255), 
     firstName varchar(255),
	 lastName varchar(255), 
	 sex varchar(1), 
	 dateCreated date, 
	 activityScore int,
	 password varchar(255),
     CONSTRAINT user_ID UNIQUE (P_Id, username));

CREATE TABLE Tags(P_Id int, 
	 FOREIGN KEY (P_Id) REFERENCES appUser(P_Id),
     latitude int,
     longitude int,
     message varchar(255));

CREATE TABLE Friends(UserId1 int,
	 UserId2 int,
	 FOREIGN KEY (UserId1) REFERENCES appUser(P_Id),
	 FOREIGN Key (UserId2) REFERENCES appUser(P_Id));

CREATE TABLE Airports(name varchar(255), 
     city varchar(255), 
     country varchar(255),
     latitude int, 
     longitude int);


