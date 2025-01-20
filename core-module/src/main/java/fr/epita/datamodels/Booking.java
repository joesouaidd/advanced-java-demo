package fr.epita.datamodels;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookid")
    private int bookid;

    @ManyToOne
    @JoinColumn(name = "facid", nullable = false)
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "memid", nullable = false)
    private Member member;

    @Column(name = "starttime", nullable = false)
    private Timestamp startTime;

    @Column(name = "slots", nullable = false)
    private int slots;

    // Constructors
    public Booking() {
    }

    public Booking(Facility facility, Member member, Timestamp startTime, int slots) {
        this.facility = facility;
        this.member = member;
        this.startTime = startTime;
        this.slots = slots;
    }

    // Getters and setters
    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    // toString
    @Override
    public String toString() {
        return " \nBooking{" +
                "bookid=" + bookid +
                ", facility=" + facility +
                ", member=" + member +
                ", startTime=" + startTime +
                ", slots=" + slots +
                '}';
    }
}
