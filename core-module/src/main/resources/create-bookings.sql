CREATE TABLE bookings (
            bookid INTEGER PRIMARY KEY AUTO_INCREMENT,
            facid INTEGER NOT NULL,
            memid INTEGER NOT NULL,
            starttime TIMESTAMP NOT NULL,
            slots INTEGER NOT NULL,
            FOREIGN KEY (facid) REFERENCES facilities(facid),
            FOREIGN KEY (memid) REFERENCES members(memid)
);
