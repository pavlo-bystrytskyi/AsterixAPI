package org.example.asterixapi.service;

import lombok.RequiredArgsConstructor;
import org.example.asterixapi.model.CharacterModel;
import org.example.asterixapi.repository.CharacterRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final IdService idService;
    private final CharacterRepository characterRepository;

    public CharacterModel addCharacter(CharacterModel characterModel) {
        String id = idService.generateId();
        CharacterModel newCharacter = this.characterRepository.save(characterModel.withId(id));

        return newCharacter;
    }

    public CharacterModel updateCharacter(String id, CharacterModel characterModel) {
        if (id == null || !characterRepository.existsById(id)) {
            throw new IllegalArgumentException("Character with id " + characterModel.id() + " does not exist");
        }

        return characterRepository.save(characterModel.withId(id));
    }

    public List<CharacterModel> getAll(String id, String name, Integer age, String profession) {
        Example<CharacterModel> example = Example.of(
                new CharacterModel(
                        id,
                        name,
                        age,
                        profession
                ));

        return characterRepository.findAll(example);
    }

    public Double getAverageAgeByProfession(String profession) {
        return this.characterRepository.getAverageAgeByProfession(profession);
    }
}
