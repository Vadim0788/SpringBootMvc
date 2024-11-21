package com.dev.petlink.controller;

import com.dev.petlink.model.PetDto;
import com.dev.petlink.service.PetService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
public class PetController {

    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping()
    public ResponseEntity<PetDto> createPet(
            @RequestBody @Valid PetDto petToCreate
    ) {
        log.info("Get request for create pet: pet={}", petToCreate);
        var createdPet = petService.createPet(petToCreate);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPet);
    }

    @PutMapping("/{id}")
    public PetDto updatePet(
            @PathVariable("id") Long id,
            @RequestBody @Valid PetDto petToUpdate
    ) {
        log.info("Get request for put pet by id: id={}, petToUpdate={}}", id, petToUpdate);
        return petService.updatePet(id, petToUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(
            @PathVariable("id") Long id
    ) {
        log.info("Get request for delete pet by id: id={}", id);
        petService.deletePet(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Pet with ID %s has been successfully deleted."
                        .formatted(id));

    }

    @GetMapping("/{id}")
    public PetDto findById(
            @PathVariable("id") Long id
    ) {
        log.info("Get request for find pet by id: id={}", id);
        return petService.findById(id);
    }
}
