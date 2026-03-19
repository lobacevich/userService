package by.lobacevich.service.impl;

import by.lobacevich.dto.request.UserDtoRequest;
import by.lobacevich.dto.response.UserDtoResponse;
import by.lobacevich.dto.response.UserWithCardsDto;
import by.lobacevich.entity.User;
import by.lobacevich.exception.EntityNotFoundException;
import by.lobacevich.exception.InvalidDataException;
import by.lobacevich.mapper.UserMapper;
import by.lobacevich.repository.PaymentCardRepository;
import by.lobacevich.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    public static final Long ID = 1L;
    public static final int NUMBER = 0;
    public static final int SIZE = 2;

    @Mock
    UserRepository repository;

    @Mock
    UserMapper mapper;

    @Mock
    PaymentCardRepository cardRepository;

    @Mock
    UserDtoRequest dtoRequest;

    @Mock
    UserDtoResponse dtoResponse;

    @Mock
    UserWithCardsDto dtoWithCards;

    @Mock
    User user;

    @Mock
    User newUser;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void create_ShouldCallSaveMethodOfRepositoryAndReturnUserDtoResponse() {
        when(mapper.dtoToUser(dtoRequest)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.userToDto(user)).thenReturn(dtoResponse);

        UserDtoResponse actual = service.create(dtoRequest);

        verify(repository, times(1)).save(user);
        assertEquals(dtoResponse, actual);
    }

    @Test
    void create_ShouldThrowInvalidDataException() {
        when(mapper.dtoToUser(dtoRequest)).thenReturn(user);
        when(repository.save(user)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(InvalidDataException.class, () -> service.create(dtoRequest));
    }

    @Test
    void update_ShouldCallSaveMethodOfRepositoryAndReturnUserDtoResponse() {
        when(repository.findById(ID)).thenReturn(Optional.of(user));
        when(mapper.dtoToUser(dtoRequest)).thenReturn(newUser);
        when(repository.save(newUser)).thenReturn(newUser);
        when(mapper.userToDto(newUser)).thenReturn(dtoResponse);

        UserDtoResponse actual = service.update(dtoRequest, ID);

        verify(repository, times(1)).save(newUser);
        assertEquals(dtoResponse, actual);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(dtoRequest, ID));
    }

    @Test
    void update_ShouldThrowInvalidDataException() {
        when(repository.findById(ID)).thenReturn(Optional.of(user));
        when(mapper.dtoToUser(dtoRequest)).thenReturn(newUser);
        when(repository.save(newUser)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(InvalidDataException.class, () -> service.update(dtoRequest, ID));
    }

    @Test
    void findById_ShouldReturnUserWithCardsDto() {
        when(repository.findByIdWithCards(ID)).thenReturn(Optional.of(user));
        when(mapper.userToDtoWithCards(user)).thenReturn(dtoWithCards);

        UserWithCardsDto actual = service.findById(ID);

        assertEquals(dtoWithCards, actual);
    }

    @Test
    void findById_ShouldThrowEntityNotFoundException() {
        when(repository.findByIdWithCards(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(ID));
    }

    @Test
    void findUsers_ShouldCallFindAllMethodOfRepositoryAndReturnPageOfUserDtoResponse() {
        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(user)));
        when(mapper.userToDto(user)).thenReturn(dtoResponse);

        Page<UserDtoResponse> actual = service.findUsers(null, null, NUMBER, SIZE);

        verify(repository, times(1))
                .findAll(any(Specification.class), any(Pageable.class));
        assertEquals(new PageImpl<>(List.of(dtoResponse)), actual);
    }

    @Test
    void activate_ShouldCallActivateUserMethodOfRepository() {
        when(repository.activateUser(ID)).thenReturn(1);

        service.activate(ID);

        verify(repository, times(1)).activateUser(ID);
    }

    @Test
    void activate_ShouldThrowEntityNotFoundException() {
        when(repository.activateUser(ID)).thenReturn(0);

        assertThrows(EntityNotFoundException.class, () -> service.activate(ID));
    }

    @Test
    void deactivate_ShouldCallDeactivateUserMethodOfRepository() {
        when(repository.deactivateUser(ID)).thenReturn(1);

        service.deactivate(ID);

        verify(repository, times(1)).deactivateUser(ID);
        verify(cardRepository, times(1)).deactivateByUserId(ID);
    }

    @Test
    void deactivate_ShouldThrowEntityNotFoundException() {
        when(repository.deactivateUser(ID)).thenReturn(0);

        assertThrows(EntityNotFoundException.class, () -> service.deactivate(ID));
    }
}