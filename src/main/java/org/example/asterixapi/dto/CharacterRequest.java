package org.example.asterixapi.dto;

import org.example.asterixapi.model.CharacterModel;

public record CharacterRequest(String name, Integer age, String profession) {

    public CharacterModel toModel() {
        return CharacterModel.builder()
                .name(name)
                .age(age)
                .profession(profession)
                .build();
    }
}
