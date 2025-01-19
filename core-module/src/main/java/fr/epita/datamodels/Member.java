package fr.epita.datamodels;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memid")
    private int memid;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "zipcode", nullable = false)
    private int zipcode;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "recommendedby")
    private Integer recommendedBy;

    @Column(name = "joindate", nullable = false)
    private Timestamp joinDate;

    // Constructors
    public Member() {
    }

    public Member(String surname, String firstname, String address, int zipcode, String telephone,
            Integer recommendedBy, Timestamp joinDate) {
        this.surname = surname;
        this.firstname = firstname;
        this.address = address;
        this.zipcode = zipcode;
        this.telephone = telephone;
        this.recommendedBy = recommendedBy;
        this.joinDate = joinDate;
    }

    // Getters and setters
    public int getMemid() {
        return memid;
    }

    public void setMemid(int memid) {
        this.memid = memid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getRecommendedBy() {
        return recommendedBy;
    }

    public void setRecommendedBy(Integer recommendedBy) {
        this.recommendedBy = recommendedBy;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    // toString
    @Override
    public String toString() {
        return "\nMember{" +
                "memid=" + memid +
                ", surname='" + surname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", address='" + address + '\'' +
                ", zipcode=" + zipcode +
                ", telephone='" + telephone + '\'' +
                ", recommendedBy=" + recommendedBy +
                ", joinDate=" + joinDate +
                '}';
    }
}
