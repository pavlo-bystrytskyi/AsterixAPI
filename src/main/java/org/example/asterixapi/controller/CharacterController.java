package org.example.asterixapi.controller;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import org.example.asterixapi.model.Character;
import org.example.asterixapi.repository.CharacterRepository;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asterix/characters")
@AllArgsConstructor
public class CharacterController {
    private final CharacterRepository characterRepository;

    @GetMapping
    public List<Character> getAllCharacters(
            @RequestParam Optional<String> id,
            @RequestParam Optional<Integer> age,
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> profession) {
        Example<Character> example = Example.of(
                new Character(
                        id.orElse(null),
                        name.orElse(null),
                        age.orElse(null),
                        profession.orElse(null)
                ));

        return characterRepository.findAll(example);
    }

    @PostMapping
    public Character createCharacter(@RequestBody Character character) {
        return characterRepository.save(character);
    }

    @PutMapping
    public Character updateCharacter(@RequestBody Character character) {
        if (character.id() == null || !characterRepository.existsById(character.id())) {
            throw new IllegalArgumentException("Character with id " + character.id() + " does not exist");
        }

        return characterRepository.save(character);
    }

    @GetMapping("/profession/{profession}")
    public Double getAverageAgeByProfession(@PathVariable String profession) {
        return this.characterRepository.getAverageAgeByProfession(profession);
    }
}
