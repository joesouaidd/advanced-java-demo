package fr.epita.tests;

import fr.epita.datamodels.Booking;
import fr.epita.datamodels.Facility;
import fr.epita.datamodels.Member;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestJUN1 {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb"; // In-memory H2 DB
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private Connection connection;

    @BeforeAll
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testDatabaseSetup() {
        assertDoesNotThrow(() -> {

            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            executeSQLFromFile("./src/main/resources/create-members.sql");
            executeSQLFromFile("./src/main/resources/create-facilities.sql");
            executeSQLFromFile("./src/main/resources/create-bookings.sql");
            executeSQLFromFile("./src/main/resources/insert-members.sql");
            executeSQLFromFile("./src/main/resources/insert-facilities.sql");
            executeSQLFromFile("./src/main/resources/insert-bookings.sql");
        });
    }

    @Test
    public void testFetchMembers() throws SQLException {
        executeSQLFromFile("./src/main/resources/create-members.sql");
        executeSQLFromFile("./src/main/resources/insert-members.sql");

        List<Member> members = getMembers();
        assertFalse(members.isEmpty(), "Members list should not be empty");
    }

    @Test
    public void testFetchFacilities() throws SQLException {
        executeSQLFromFile("./src/main/resources/create-facilities.sql");
        executeSQLFromFile("./src/main/resources/insert-facilities.sql");

        List<Facility> facilities = getFacilities();
        assertFalse(facilities.isEmpty(), "Facilities list should not be empty");
    }

    @Test
    public void testFetchBookings() throws SQLException {

        executeSQLFromFile("./src/main/resources/create-members.sql");
        executeSQLFromFile("./src/main/resources/insert-members.sql");
        executeSQLFromFile("./src/main/resources/create-facilities.sql");
        executeSQLFromFile("./src/main/resources/insert-facilities.sql");
        executeSQLFromFile("./src/main/resources/create-bookings.sql");
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        executeSQLFromFile("./src/main/resources/insert-bookings.sql");

        List<Member> members = getMembers();
        List<Facility> facilities = getFacilities();
        List<Booking> bookings = getBookings(members, facilities);

        assertFalse(bookings.isEmpty(), "Bookings list should not be empty");
    }

    private void executeSQLFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
                Statement statement = connection.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append(" ");
            }

            statement.execute(sql.toString());
        } catch (IOException | SQLException e) {
            fail("Failed to execute SQL file: " + filePath, e);
        }
    }

    private List<Member> getMembers() {
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
            fail("Failed to fetch members", e);
        }
        return members;
    }

    private List<Facility> getFacilities() {
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
            fail("Failed to fetch facilities", e);
        }
        return facilities;
    }

    private List<Booking> getBookings(List<Member> members, List<Facility> facilities) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookid(rs.getInt("bookid"));
                booking.setStartTime(rs.getTimestamp("starttime"));
                booking.setSlots(rs.getInt("slots"));

                int memId = rs.getInt("memid");
                int facId = rs.getInt("facid");

                Member member = members.stream().filter(m -> m.getMemid() == memId).findFirst().orElse(null);
                Facility facility = facilities.stream().filter(f -> f.getFacid() == facId).findFirst().orElse(null);

                booking.setMember(member);
                booking.setFacility(facility);

                bookings.add(booking);
            }
        } catch (SQLException e) {
            fail("Failed to fetch bookings", e);
        }
        return bookings;
    }
}
