package fr.epita.tests;

import fr.epita.datamodels.Booking;
import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TestMVD2 {

    public static void main(String[] args) {
        System.out.println("Initializing instances of Facility, Member, and Booking...");

        // Initialize Facility
        Facility facility = new Facility(
                "Swimming Pool",
                new BigDecimal(15.00),
                new BigDecimal(30.00),
                new BigDecimal(50000.00),
                new BigDecimal(100.00));
        System.out.println("Facility: " + facility);

        // Initialize Member
        Member member = new Member(
                "Doe",
                "John",
                "123 Elm Street",
                12345,
                "123-456-7890",
                null,
                Timestamp.valueOf("2023-01-01 10:00:00"));
        System.out.println("Member: " + member);

        // Initialize Booking
        Booking booking = new Booking(
                facility,
                member,
                Timestamp.valueOf("2023-02-01 14:00:00"),
                2);
        System.out.println("Booking: " + booking);

        System.out.println("\n TestMVD2 execution completed.");
    }
}
