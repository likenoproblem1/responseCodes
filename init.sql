CREATE DATABASE statusCodesDB;
CREATE TABLE Responses (
      url varchar(255) NOT NULL ,
      method ENUM('GET','PUT','POST','DELETE') NOT NULL ,
      response_code int NOT NULL ,
      response_body VARCHAR(255),
      CONSTRAINT PK_Responses PRIMARY KEY (url,method)
  );