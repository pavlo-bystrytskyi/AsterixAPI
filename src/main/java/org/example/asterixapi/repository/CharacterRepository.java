package org.example.asterixapi.repository;

import org.example.asterixapi.model.Character;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends MongoRepository<Character, String> {

    @Aggregation(
            pipeline = {
                    "{ $match: { 'profession': ?0 } }",
                    "{ $group: { _id: null, averageAge: { $avg: '$age' } } }"
            }
    )
    Double getAverageAgeByProfession(String profession);
}
