package com.dev.petlink.service;

import com.dev.petlink.model.PetDto;
import com.dev.petlink.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private Long idCounter;

    private final Map<Long, UserDto> userMap;

    public UserService() {
        this.userMap = new HashMap<>();
        this.idCounter = 0L;
    }

    public UserDto createUser(UserDto userToCreate) {
        var newId = ++idCounter;
        UserDto newUser = new UserDto();
        newUser.setId(newId);
        newUser.setName(userToCreate.getName());
        newUser.setAge(userToCreate.getAge());
        newUser.setEmail(userToCreate.getEmail());
        newUser.setPets(userToCreate.getPets());

        userMap.put(newId, newUser);
        return newUser;
    }

    public UserDto updateUser(Long id, UserDto userToUpdate) {
        if (userMap.get(id) == null) {
            throw new NoSuchElementException(
                    "No found user by id=%s"
                            .formatted(id));
        }
        var updatedUser = new UserDto();
        updatedUser.setId(id);
        updatedUser.setName(userToUpdate.getName());
        updatedUser.setAge(userToUpdate.getAge());
        updatedUser.setEmail(userToUpdate.getEmail());
        updatedUser.setPets(userToUpdate.getPets());
        userMap.put(id, userToUpdate);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        var deletedUser = userMap.remove(id);
        if (deletedUser == null) {
            throw new NoSuchElementException(
                    "No found user by id=%s"
                            .formatted(id));
        }

    }

    public UserDto findById(long id) {
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException(
                        "No found user by id=%s"
                                .formatted(id)
                ));
    }

    public void addPet(Long id, PetDto newPet) {
        if (userMap.get(id) == null) {
            throw new NoSuchElementException(
                    "No found user by id=%s"
                            .formatted(id));
        }
        userMap.get(id).addPet(newPet);
    }

    public void removePet(Long petId) {
        userMap.values().forEach(user -> Optional.ofNullable(user.getPets())
                .ifPresent(pets -> pets.removeIf(pet -> pet.getId().equals(petId))
                ));
    }
}