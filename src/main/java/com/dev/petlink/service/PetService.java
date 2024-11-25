package com.dev.petlink.service;

import com.dev.petlink.model.PetDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetService {

    private Long idCounter;

    private final Map<Long, PetDto> petMap;

    private final UserService userService;

    public PetService(UserService userService) {
        this.userService = userService;
        this.petMap = new HashMap<>();
        this.idCounter = 0L;
    }

    public PetDto findById(Long id) {
        return Optional.ofNullable(petMap.get(id))
                .orElseThrow(() -> new NoSuchElementException(
                        "No found pet by id=%s"
                                .formatted(id)
                ));
    }

    public void deletePet(Long id) {
        var deletedPet  = petMap.remove(id);
        if (deletedPet  == null) {
            throw new NoSuchElementException(
                    "No found pet by id=%s"
                            .formatted(id));
        }
        userService.removePet(id);
    }

    public PetDto updatePet(Long id, @Valid PetDto petToUpdate) {
        if (petMap.get(id) == null) {
            throw new NoSuchElementException(
                    "No found pet by id=%s"
                            .formatted(id));
        }

        userService.findById(petToUpdate.getUserId());

        var updatedPet = petMap.get(id);
        if (!Objects.equals(updatedPet.getUserId(), petToUpdate.getUserId())){
            userService.removePet(updatedPet.getId());
            userService.addPet(petToUpdate.getUserId(), updatedPet);
        }

        updatedPet.setUserId(petToUpdate.getUserId());
        updatedPet.setName(petToUpdate.getName());

        petMap.put(id, petToUpdate);
        return updatedPet;
    }

    public PetDto createPet(@Valid PetDto petToCreate) {
        var newId = ++idCounter;
        PetDto newPet = new PetDto();
        newPet.setId(newId);
        newPet.setName(petToCreate.getName());
        newPet.setUserId(petToCreate.getUserId());

        userService.addPet(newPet.getUserId(), newPet);
        petMap.put(newId, newPet);
        return newPet;
    }
}
