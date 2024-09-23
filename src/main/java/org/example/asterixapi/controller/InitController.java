package org.example.asterixapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.asterixapi.model.Character;
import org.example.asterixapi.repository.CharacterRepository;
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
    public void initialize() {
        characterRepository.deleteAll();
        characterRepository.saveAll(this.getCharacters());
    }

    private List<Character> getCharacters() {
        return List.of(
                new Character("1", "Asterix", 35, "Krieger"),
                new Character("2", "Obelix", 35, "Lieferant"),
                new Character("3", "Miraculix", 60, "Druide"),
                new Character("4", "Majestix", 60, "Häuptling"),
                new Character("5", "Troubadix", 25, "Barden"),
                new Character("6", "Gutemine", 35, "Häuptlingsfrau"),
                new Character("7", "Idefix", 5, "Hund"),
                new Character("8", "Geriatrix", 70, "Rentner"),
                new Character("9", "Automatix", 35, "Schmied"),
                new Character("10", "Grockelix", 35, "Fischer"),
                new Character("11", "Psinix", 15, "Hund")
        );
    }
}
