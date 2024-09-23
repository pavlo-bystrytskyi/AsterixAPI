package org.example.asterixapi.dto;

import org.example.asterixapi.model.CharacterModel;

public record ObjectIdResponse(String objectId) {

    public static ObjectIdResponse fromCharacter(CharacterModel character) {
        return new ObjectIdResponse(character.id());
    }
}
