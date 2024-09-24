package org.example.asterixapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.asterixapi.model.CharacterModel;
import org.example.asterixapi.repository.CharacterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/asterix/init")
@RequiredArgsConstructor
public class InitController {
    private final CharacterRepository characterRepository;

    @PostMapping
    public ResponseEntity initialize() {
        characterRepository.deleteAll();
        characterRepository.saveAll(this.getCharacters());

        return ResponseEntity.ok().build();
    }

    private List<CharacterModel> getCharacters() {
        return List.of(
                new CharacterModel("1", "Asterix", 35, "Krieger"),
                new CharacterModel("2", "Obelix", 35, "Lieferant"),
                new CharacterModel("3", "Miraculix", 60, "Druide"),
                new CharacterModel("4", "Majestix", 60, "Häuptling"),
                new CharacterModel("5", "Troubadix", 25, "Barden"),
                new CharacterModel("6", "Gutemine", 35, "Häuptlingsfrau"),
                new CharacterModel("7", "Idefix", 5, "Hund"),
                new CharacterModel("8", "Geriatrix", 70, "Rentner"),
                new CharacterModel("9", "Automatix", 35, "Schmied"),
                new CharacterModel("10", "Grockelix", 35, "Fischer"),
                new CharacterModel("11", "Psinix", 15, "Hund")
        );
    }
}
