-- Table for Facilities
CREATE TABLE facilities (
                            facid INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(100) NOT NULL,
                            membercost DECIMAL(10, 2) NOT NULL,
                            guestcost DECIMAL(10, 2) NOT NULL,
                            initialoutlay DECIMAL(10, 2) NOT NULL,
                            monthlymaintenance DECIMAL(10, 2) NOT NULL
);

-- Table for Members
CREATE TABLE members (
                         memid INT PRIMARY KEY AUTO_INCREMENT,
                         surname VARCHAR(200) NOT NULL,
                         firstname VARCHAR(200) NOT NULL,
                         address VARCHAR(300) NOT NULL,
                         zipcode INT NOT NULL,
                         telephone VARCHAR(20) NOT NULL,
                         recommendedby INT,
                         joindate TIMESTAMP NOT NULL
);

-- Table for Bookings
CREATE TABLE bookings (
                          bookid INT PRIMARY KEY AUTO_INCREMENT,
                          facid INT NOT NULL,
                          memid INT NOT NULL,
                          starttime TIMESTAMP NOT NULL,
                          slots INT NOT NULL,
                          FOREIGN KEY (facid) REFERENCES facilities(facid),
                          FOREIGN KEY (memid) REFERENCES members(memid)
);
