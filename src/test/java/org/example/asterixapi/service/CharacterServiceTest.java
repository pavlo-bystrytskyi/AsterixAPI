package org.example.asterixapi.service;

import org.example.asterixapi.model.CharacterModel;
import org.example.asterixapi.repository.CharacterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CharacterServiceTest {

    @Test
    void updateCharacter_existingCharacter() {
        String id = "123";
        CharacterModel model = new CharacterModel(id, "John Doe", 20, "Student");
        CharacterModel expected = new CharacterModel(id, "John Doe", 70, "Rentner");
        CharacterRepository repository = mock(CharacterRepository.class);
        CharacterService service = new CharacterService(new IdService(), repository);
        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(model)).thenReturn(expected);

        CharacterModel updatedModel = service.updateCharacter(id, model);

        verify(repository).existsById(id);
        verify(repository).save(model);
        verifyNoMoreInteractions(repository);
        assertEquals(expected, updatedModel);
    }

    @Test
    void updateCharacter_absentCharacter() {
        String id = "123";
        CharacterModel model = new CharacterModel(id, "John Doe", 20, "Student");
        CharacterRepository repository = mock(CharacterRepository.class);
        CharacterService service = new CharacterService(new IdService(), repository);
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.updateCharacter(id, model));

        verify(repository).existsById(id);
        verifyNoMoreInteractions(repository);
    }


    @Test
    void deleteCharacter_existingCharacter() {
        String id = "123";
        CharacterRepository repository = mock(CharacterRepository.class);
        CharacterService service = new CharacterService(new IdService(), repository);
        when(repository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteCharacter(id));

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteCharacter_absentCharacter() {
        String id = "123";
        CharacterRepository repository = mock(CharacterRepository.class);
        CharacterService service = new CharacterService(new IdService(), repository);
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.deleteCharacter(id));

        verify(repository).existsById(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getCharacters_existingCharacter() {
        String id = "123";
        List<CharacterModel> expected = List.of(new CharacterModel(id, "John Doe", 70, "Rentner"));
        CharacterRepository repository = mock(CharacterRepository.class);
        CharacterService service = new CharacterService(new IdService(), repository);
        Example<CharacterModel> example = Example.of(
                new CharacterModel(
                        id,
                        null,
                        null,
                        null
                ));
        when(repository.findAll(example)).thenReturn(expected);

        List<CharacterModel> actual = service.getCharacters(id, null, null, null);

        verify(repository).findAll(example);
        verifyNoMoreInteractions(repository);
        assertEquals(expected, actual);
    }

    @Test
    void getCharacters_absentCharacter() {
        String id = "123";
        CharacterRepository repository = mock(CharacterRepository.class);
        CharacterService service = new CharacterService(new IdService(), repository);
        Example<CharacterModel> example = Example.of(
                new CharacterModel(
                        id,
                        null,
                        null,
                        null
                ));
        when(repository.findAll(example)).thenReturn(List.of());

        List<CharacterModel> result = service.getCharacters(id, null, null, null);

        verify(repository).findAll(example);
        verifyNoMoreInteractions(repository);
        assertEquals(0, result.size());
    }

    @Test
    void addCharacter_successful() {
        String originalId = "0";
        String generatedId = "123";
        CharacterModel model = new CharacterModel(originalId, "John Doe", 20, "Student");
        CharacterRepository repository = mock(CharacterRepository.class);
        IdService idService = mock(IdService.class);
        CharacterService service = new CharacterService(idService, repository);
        when(idService.generateId()).thenReturn(generatedId);
        CharacterModel expected = model.withId(generatedId);
        when(repository.save(expected)).thenReturn(expected);

        CharacterModel actual = service.addCharacter(model);

        verify(repository).save(expected);
        verifyNoMoreInteractions(repository);
        verify(idService).generateId();
        verifyNoMoreInteractions(idService);
        assertEquals(expected, actual);
    }
}