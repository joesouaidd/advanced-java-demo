package fr.epita.datamodels;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "facilities")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facid")
    private int facid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "membercost", nullable = false)
    private BigDecimal memberCost;

    @Column(name = "guestcost", nullable = false)
    private BigDecimal guestCost;

    @Column(name = "initialoutlay", nullable = false)
    private BigDecimal initialOutlay;

    @Column(name = "monthlymaintenance", nullable = false)
    private BigDecimal monthlyMaintenance;

    // Constructors
    public Facility() {}

    public Facility(String name, BigDecimal memberCost, BigDecimal guestCost, BigDecimal initialOutlay, BigDecimal monthlyMaintenance) {
        this.name = name;
        this.memberCost = memberCost;
        this.guestCost = guestCost;
        this.initialOutlay = initialOutlay;
        this.monthlyMaintenance = monthlyMaintenance;
    }

    // Getters and setters
    public int getFacid() {
        return facid;
    }

    public void setFacid(int facid) {
        this.facid = facid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMemberCost() {
        return memberCost;
    }

    public void setMemberCost(BigDecimal memberCost) {
        this.memberCost = memberCost;
    }

    public BigDecimal getGuestCost() {
        return guestCost;
    }

    public void setGuestCost(BigDecimal guestCost) {
        this.guestCost = guestCost;
    }

    public BigDecimal getInitialOutlay() {
        return initialOutlay;
    }

    public void setInitialOutlay(BigDecimal initialOutlay) {
        this.initialOutlay = initialOutlay;
    }

    public BigDecimal getMonthlyMaintenance() {
        return monthlyMaintenance;
    }

    public void setMonthlyMaintenance(BigDecimal monthlyMaintenance) {
        this.monthlyMaintenance = monthlyMaintenance;
    }

    // toString
    @Override
    public String toString() {
        return "\nFacility{" +
                "facid=" + facid +
                ", name='" + name + '\'' +
                ", memberCost=" + memberCost +
                ", guestCost=" + guestCost +
                ", initialOutlay=" + initialOutlay +
                ", monthlyMaintenance=" + monthlyMaintenance +
                '}';
    }
}
