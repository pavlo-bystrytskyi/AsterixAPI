package org.example.asterixapi.controller;

import lombok.AllArgsConstructor;
import org.example.asterixapi.dto.AverageResponse;
import org.example.asterixapi.dto.CharacterRequest;
import org.example.asterixapi.dto.ObjectIdResponse;
import org.example.asterixapi.model.CharacterModel;
import org.example.asterixapi.service.CharacterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/asterix/characters")
@AllArgsConstructor
public class CharacterController {
    private CharacterService characterService;

    @GetMapping
    public List<CharacterModel> getAllCharacters(
            @RequestParam Optional<String> id,
            @RequestParam Optional<String> name,
            @RequestParam Optional<Integer> age,
            @RequestParam Optional<String> profession
    ) {
        return characterService.getAll(
                id.orElse(null),
                name.orElse(null),
                age.orElse(null),
                profession.orElse(null)
        );
    }

    @PostMapping
    public ObjectIdResponse createCharacter(@RequestBody CharacterRequest characterRequest) {
        CharacterModel characterModel = characterRequest.toModel();
        characterService.addCharacter(characterModel);

        return ObjectIdResponse.fromCharacter(characterModel);
    }

    @PutMapping("/{id}")
    public ObjectIdResponse updateCharacter(@PathVariable String id, @RequestBody CharacterRequest characterRequest) {
        CharacterModel characterModel = this.characterService.updateCharacter(id, characterRequest.toModel());

        return ObjectIdResponse.fromCharacter(characterModel);
    }

    @GetMapping("/profession/{profession}")
    public AverageResponse getAverageAgeByProfession(@PathVariable String profession) {
        Double average = this.characterService.getAverageAgeByProfession(profession);

        return new AverageResponse(profession, average);
    }
}
