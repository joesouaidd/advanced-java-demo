package fr.epita.rest;

import fr.epita.datamodels.Facility;
import fr.epita.dtos.FacilityDTO;
import fr.epita.services.data.FacilityJPADAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/facilities")
public class FacilitiesRestController {

    private final FacilityJPADAO facilityJPADAO;

    @Autowired
    public FacilitiesRestController(FacilityJPADAO facilityJPADAO) {
        this.facilityJPADAO = facilityJPADAO;
    }

    // GET: Fetch all facilities using listAll
    @GetMapping
    public List<FacilityDTO> getAllFacilities() {
        List<Facility> facilities = facilityJPADAO.listAll();
        return facilities.stream().map(FacilityDTO::fromEntity).collect(Collectors.toList());
    }

    // POST: Create a new facility
    @PostMapping
    public void createFacility(@RequestBody FacilityDTO facilityDTO) {
        Facility facility = facilityDTO.toEntity();
        facilityJPADAO.add(facility); // Using the add() method instead of save
    }
}
