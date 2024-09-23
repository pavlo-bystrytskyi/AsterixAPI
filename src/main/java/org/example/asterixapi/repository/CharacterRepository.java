package org.example.asterixapi.repository;

import org.example.asterixapi.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {

}
