CREATE TABLE members (
    memid INTEGER PRIMARY KEY AUTO_INCREMENT,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    address VARCHAR(300) NOT NULL,
    zipcode INTEGER NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    recommendedby INTEGER,
    joindate TIMESTAMP NOT NULL
);