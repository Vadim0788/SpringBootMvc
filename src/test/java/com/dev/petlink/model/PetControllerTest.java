package com.dev.petlink.model;

import com.dev.petlink.service.PetService;
import com.dev.petlink.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest
class PetControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void shouldSuccessCreatePet() throws Exception {

        UserDto user = new UserDto();
        user.setId(null);
        user.setName("Pasha");
        user.setAge(25);
        user.setEmail("pasha@gmail.com");

        user = userService.createUser(user);

        PetDto pet = new PetDto();
        pet.setId(null);
        pet.setName("Pasha");
        pet.setUserId(user.getId());

        String petJson = objectMapper.writeValueAsString(pet);

        String createdPetJson = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto petResponse = objectMapper.readValue(createdPetJson, PetDto.class);

        org.assertj.core.api.Assertions.assertThat(pet)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(petResponse);

    }

    @Test
    void shouldSuccessDeletePetById() throws Exception {

        UserDto user = new UserDto();
        user.setId(null);
        user.setName("Pasha");
        user.setAge(25);
        user.setEmail("pasha@gmail.com");

        user = userService.createUser(user);

        PetDto pet = new PetDto();
        pet.setId(null);
        pet.setName("Pasha");
        pet.setUserId(user.getId());

        pet = petService.createPet(pet);

        mockMvc.perform(delete("/pets/{id}", pet.getId()))
                .andExpect(status().is(204))
                .andExpect(content().string("Pet with ID %s has been successfully deleted."
                        .formatted(pet.getId())));

        UserDto updatedUser = userService.findById(user.getId());
        Long id = pet.getId();
        boolean petExists = updatedUser.getPets().stream()
                .anyMatch(p -> p.getId().equals(id));

        assertFalse(petExists, "Питомец не был удален из коллекции пользователя!");

    }

    @Test
    void shouldNotCreatePetWhenRequestNotValid() throws Exception {
        UserDto user = new UserDto();
        user.setId(null);
        user.setName("Pasha");
        user.setAge(25);
        user.setEmail("pasha@gmail.com");

        user = userService.createUser(user);

        PetDto pet = new PetDto();
        pet.setId(null);
        pet.setName("Pasha");
        pet.setUserId(user.getId());

        pet = petService.createPet(pet);

        String petJson =  objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().is(400));
    }
}