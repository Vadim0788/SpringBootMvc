package com.dev.petlink.model;

import com.dev.petlink.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserService userService;

    @Test
    void shouldSuccessCreateUser() throws Exception {
        UserDto user = new UserDto();
        user.setId(null);
        user.setName("Pasha");
        user.setAge(25);
        user.setEmail("pasha@gmail.com");

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userResponse = objectMapper.readValue(createdUserJson, UserDto.class);

        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(userResponse);

    }

    @Test
    void shouldSuccessGetUserById() throws Exception {
        UserDto user = new UserDto();
        user.setId(null);
        user.setName("Pasha");
        user.setAge(25);
        user.setEmail("pasha@gmail.com");

        user = userService.createUser(user);

        String foundUserJson = mockMvc.perform(get("/users/{if}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto foundUser = objectMapper.readValue(foundUserJson,UserDto.class);

        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(foundUser);

    }
}