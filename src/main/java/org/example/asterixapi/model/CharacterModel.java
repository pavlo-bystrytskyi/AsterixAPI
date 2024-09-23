package org.example.asterixapi.model;

import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Id;

@Builder
@With
public record CharacterModel(String id, String name, Integer age, String profession) {
}
