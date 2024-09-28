package org.example.asterixapi.controller;

import org.example.asterixapi.model.CharacterModel;
import org.example.asterixapi.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@AutoConfigureMockMvc
class CharacterControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CharacterRepository repository;

    @Test
    @DirtiesContext
    void createCharacter_successfully() throws Exception {
        try (MockedStatic<UUID> uuidStatic = mockStatic(UUID.class)) {
            UUID uuid = new UUID(000, 001);
            uuidStatic.when(UUID::randomUUID).thenReturn(uuid);
            Example<CharacterModel> example = Example.of(
                    new CharacterModel(
                            null,
                            "Gutemine",
                            null,
                            null
                    ));
            List<CharacterModel> expected = List.of(
                    new CharacterModel(
                            "00000000-0000-0000-0000-000000000001",
                            "Gutemine",
                            35,
                            "Häuptlingsfrau"
                    )
            );

            mvc.perform(MockMvcRequestBuilders.post("/asterix/characters")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            """
                                     {
                                          "name": "Gutemine",
                                          "age": 35,
                                          "profession": "Häuptlingsfrau"
                                     }
                                    """
                    ));

            List<CharacterModel> actual = repository.findAll(example);
            assertEquals(expected, actual);
        }
    }

    @Test
    @DirtiesContext
    void updateCharacter_existent() throws Exception {
        String characterId = "1";
        String newName = "Aster X";
        CharacterModel character = new CharacterModel(characterId, "Asterix", 35, "Krieger");
        CharacterModel expected = character.withName(newName);
        repository.insert(character);

        mvc.perform(MockMvcRequestBuilders.put("/asterix/characters/{id}", characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                         {
                                              "name": "%s",
                                              "age": 35,
                                              "profession": "Krieger"
                                         }
                                        """.formatted(newName)
                        )
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "objectId": "%s"
                        }
                        """.formatted(characterId)));
        Optional<CharacterModel> actual = repository.findById(characterId);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    @DirtiesContext
    void updateCharacter_nonExistent() throws Exception {
        String characterId = "1";
        String incorrectId = "2";
        CharacterModel character = new CharacterModel(characterId, "Asterix", 35, "Krieger");
        repository.insert(character);

        mvc.perform(MockMvcRequestBuilders.put("/asterix/characters/{id}", incorrectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                         {
                                              "name": "Aster x",
                                              "age": 35,
                                              "profession": "Krieger"
                                         }
                                        """
                        )
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                          "message": "Character with id %s does not exist"
                        }
                        """.formatted(incorrectId)));
        Optional<CharacterModel> actual = repository.findById(characterId);

        assertTrue(actual.isPresent());
        assertEquals(character, actual.get());
    }

    @Test
    @DirtiesContext
    void deleteCharacter_existent() throws Exception {
        String characterId = "1";
        repository.insert(new CharacterModel(characterId, "Asterix", 35, "Krieger"));

        mvc.perform(MockMvcRequestBuilders.delete("/asterix/characters/{id}", characterId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Optional<CharacterModel> actual = repository.findById(characterId);

        assertTrue(actual.isEmpty());
    }

    @Test
    @DirtiesContext
    void deleteCharacter_nonExistent() throws Exception {
        String characterId = "1";
        String incorrectId = "2";
        repository.insert(new CharacterModel(characterId, "Asterix", 35, "Krieger"));

        mvc.perform(MockMvcRequestBuilders.delete("/asterix/characters/{id}", incorrectId))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "message": "Character with id %s does not exist"
                        }
                        """.formatted(incorrectId)));
        Optional<CharacterModel> actual = repository.findById(characterId);

        assertTrue(actual.isPresent());
    }

    @Test
    @DirtiesContext
    void getAverageAgeByProfession_existent() throws Exception {
        repository.insert(new CharacterModel("8", "Geriatrix", 70, "Rentner"));
        repository.insert(new CharacterModel("7", "Idefix", 5, "Hund"));
        repository.insert(new CharacterModel("11", "Psinix", 15, "Hund"));

        mvc.perform(MockMvcRequestBuilders.get("/asterix/characters/profession/{profession}", "Hund"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                  {
                                    "profession": "Hund",
                                    "averageAge": 10
                                  }
                                """
                ));
    }

    @Test
    @DirtiesContext
    void getAverageAgeByProfession_nonExistent() throws Exception {
        repository.insert(new CharacterModel("8", "Geriatrix", 70, "Rentner"));
        repository.insert(new CharacterModel("7", "Idefix", 5, "Hund"));
        repository.insert(new CharacterModel("11", "Psinix", 15, "Hund"));

        mvc.perform(MockMvcRequestBuilders.get("/asterix/characters/profession/{profession}", "Krieger"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                  {
                                    "profession": "Krieger",
                                    "averageAge": 0
                                  }
                                """
                ));
    }

    @Test
    @DirtiesContext
    void getAllCharacters_getAllExistent() throws Exception {
        repository.insert(new CharacterModel("1", "Asterix", 35, "Krieger"));
        repository.insert(new CharacterModel("2", "Obelix", 35, "Lieferant"));

        mvc.perform(MockMvcRequestBuilders.get("/asterix/characters"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                  {
                                    "id": "1",
                                    "name": "Asterix",
                                    "age": 35,
                                    "profession": "Krieger"
                                  },
                                  {
                                    "id": "2",
                                    "name": "Obelix",
                                    "age": 35,
                                    "profession": "Lieferant"
                                  }
                                ]
                                """
                ));
    }

    @Test
    @DirtiesContext
    void getAllCharacters_getFiltered() throws Exception {
        repository.insert(new CharacterModel("8", "Geriatrix", 70, "Rentner"));
        repository.insert(new CharacterModel("9", "Automatix", 35, "Schmied"));
        repository.insert(new CharacterModel("10", "Grockelix", 35, "Fischer"));

        mvc.perform(MockMvcRequestBuilders.get("/asterix/characters?age=35"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [
                                  {
                                    "id": "9",
                                    "name": "Automatix",
                                    "age": 35,
                                    "profession": "Schmied"
                                  },
                                  {
                                    "id": "10",
                                    "name": "Grockelix",
                                    "age": 35,
                                    "profession": "Fischer"
                                  }
                                ]
                                """
                ));
    }

    @Test
    void getAllCharacters_getAllEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/asterix/characters"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        "[]"
                ));
    }
}
