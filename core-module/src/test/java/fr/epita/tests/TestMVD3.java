package fr.epita.tests;

import fr.epita.datamodels.Booking;
import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//This type of database communication is called JDBC (Java Database Connectivity). It is a low-level API provided by Java to directly interact with a relational database using SQL queries.

public class TestMVD3 {
    private static final String JDBC_URL = "jdbc:h2:mem:testdb"; // In-memory H2 DB
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // Load and execute SQL scripts
            executeSQLFromFile(connection, "./core-module/src/main/resources/create-members.sql");
            executeSQLFromFile(connection, "./core-module/src/main/resources/create-facilities.sql");
            executeSQLFromFile(connection, "./core-module/src/main/resources/create-bookings.sql");
            executeSQLFromFile(connection, "./core-module/src/main/resources/insert-members.sql");
            executeSQLFromFile(connection, "./core-module/src/main/resources/insert-facilities.sql");
            executeSQLFromFile(connection, "./core-module/src/main/resources/insert-bookings.sql");

            // Fetch and display data from the database
            List<Member> members = getMembers(connection);
            List<Facility> facilities = getFacilities(connection);
            List<Booking> bookings = getBookings(connection, members, facilities);

            System.out.println("Members:");
            members.forEach(System.out::println);

            System.out.println("\nFacilities:");
            facilities.forEach(System.out::println);

            System.out.println("\nBookings:");
            bookings.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void executeSQLFromFile(Connection connection, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
                Statement statement = connection.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append(" ");
            }

            statement.execute(sql.toString());
            System.out.println("Executed: " + filePath);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Member> getMembers(Connection connection) {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM members";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Member member = new Member();
                member.setMemid(rs.getInt("memid"));
                member.setSurname(rs.getString("surname"));
                member.setFirstname(rs.getString("firstname"));
                member.setAddress(rs.getString("address"));
                member.setZipcode(rs.getInt("zipcode"));
                member.setTelephone(rs.getString("telephone"));
                member.setRecommendedBy(rs.getObject("recommendedby") != null ? rs.getInt("recommendedby") : null);
                member.setJoinDate(rs.getTimestamp("joindate"));

                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    private static List<Facility> getFacilities(Connection connection) {
        List<Facility> facilities = new ArrayList<>();
        String query = "SELECT * FROM facilities";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Facility facility = new Facility();
                facility.setFacid(rs.getInt("facid"));
                facility.setName(rs.getString("name"));
                facility.setMemberCost(rs.getBigDecimal("membercost"));
                facility.setGuestCost(rs.getBigDecimal("guestcost"));
                facility.setInitialOutlay(rs.getBigDecimal("initialoutlay"));
                facility.setMonthlyMaintenance(rs.getBigDecimal("monthlymaintenance"));

                facilities.add(facility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilities;
    }

    private static List<Booking> getBookings(Connection connection, List<Member> members, List<Facility> facilities) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookid(rs.getInt("bookid"));
                booking.setStartTime(rs.getTimestamp("starttime"));
                booking.setSlots(rs.getInt("slots"));

                // Map member and facility using IDs
                int memId = rs.getInt("memid");
                int facId = rs.getInt("facid");

                Member member = members.stream().filter(m -> m.getMemid() == memId).findFirst().orElse(null);
                Facility facility = facilities.stream().filter(f -> f.getFacid() == facId).findFirst().orElse(null);

                booking.setMember(member);
                booking.setFacility(facility);

                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
