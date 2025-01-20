package fr.epita.dtos;

import fr.epita.datamodels.Facility;

import java.math.BigDecimal;

public class FacilityDTO {

    private String name;
    private BigDecimal memberCost;
    private BigDecimal guestCost;
    private BigDecimal initialOutlay;
    private BigDecimal monthlyMaintenance;

    // Getters and setters
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

    // Convert from Facility entity
    public static FacilityDTO fromEntity(Facility facility) {
        FacilityDTO dto = new FacilityDTO();
        dto.setName(facility.getName());
        dto.setMemberCost(facility.getMemberCost());
        dto.setGuestCost(facility.getGuestCost());
        dto.setInitialOutlay(facility.getInitialOutlay());
        dto.setMonthlyMaintenance(facility.getMonthlyMaintenance());
        return dto;
    }

    // Convert to Facility entity
    public Facility toEntity() {
        Facility facility = new Facility();
        facility.setName(this.name);
        facility.setMemberCost(this.memberCost);
        facility.setGuestCost(this.guestCost);
        facility.setInitialOutlay(this.initialOutlay);
        facility.setMonthlyMaintenance(this.monthlyMaintenance);
        return facility;
    }
}
