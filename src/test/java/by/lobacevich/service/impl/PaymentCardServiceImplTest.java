package by.lobacevich.service.impl;

import by.lobacevich.dto.request.PayCardDtoRequest;
import by.lobacevich.dto.response.PayCardDtoResponse;
import by.lobacevich.entity.PaymentCard;
import by.lobacevich.entity.User;
import by.lobacevich.exception.CardsLimitException;
import by.lobacevich.exception.EntityNotFoundException;
import by.lobacevich.exception.InvalidDataException;
import by.lobacevich.mapper.PaymentCardMapper;
import by.lobacevich.repository.PaymentCardRepository;
import by.lobacevich.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceImplTest {

    public static final Long ID = 1L;
    public static final int CARDS_LIMIT = 5;
    public static final int NUMBER = 0;
    public static final int SIZE = 2;
    public static final String CACHE_NAME = "users";

    @Mock
    PaymentCardRepository repository;

    @Mock
    PaymentCardMapper mapper;

    @Mock
    UserRepository userRepository;

    @Mock
    CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    PayCardDtoRequest dtoRequest;

    @Mock
    PayCardDtoResponse dtoResponse;

    @Mock
    PaymentCard card;

    @Mock
    PaymentCard newCard;

    @Mock
    User user;

    @InjectMocks
    PaymentCardServiceImpl service;

    @Test
    void create_ShouldCallSaveMethodOfRepositoryAndReturnPayCardDtoResponse() {
        when(dtoRequest.userId()).thenReturn(ID);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(repository.countByUserId(ID)).thenReturn(CARDS_LIMIT - 1);
        when(mapper.dtoToEntity(dtoRequest)).thenReturn(card);
        when(repository.save(card)).thenReturn(card);
        when(mapper.entityToDto(card)).thenReturn(dtoResponse);

        PayCardDtoResponse actual = service.create(dtoRequest);

        verify(repository, times(1)).save(card);
        assertEquals(dtoResponse, actual);
    }

    @Test
    void create_ShouldThrowEntityNotFoundException() {
        when(dtoRequest.userId()).thenReturn(ID);
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.create(dtoRequest));
    }

    @Test
    void create_ShouldThrowCardsLimitException() {
        when(dtoRequest.userId()).thenReturn(ID);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(repository.countByUserId(ID)).thenReturn(CARDS_LIMIT);

        assertThrows(CardsLimitException.class, () -> service.create(dtoRequest));
    }

    @Test
    void create_ShouldThrowInvalidDataException() {
        when(dtoRequest.userId()).thenReturn(ID);
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(repository.countByUserId(ID)).thenReturn(CARDS_LIMIT - 1);
        when(mapper.dtoToEntity(dtoRequest)).thenReturn(card);
        when(repository.save(card)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(InvalidDataException.class, () -> service.create(dtoRequest));
    }

    @Test
    void update_ShouldCallSaveMethodOfRepositoryAndReturnUserDtoResponse() {
        when(repository.findByIdWithUser(ID)).thenReturn(Optional.of(card));
        when(mapper.dtoToEntity(dtoRequest)).thenReturn(newCard);
        when(repository.save(newCard)).thenReturn(newCard);
        when(mapper.entityToDto(newCard)).thenReturn(dtoResponse);

        PayCardDtoResponse actual = service.update(dtoRequest, ID);

        verify(repository, times(1)).save(newCard);
        assertEquals(dtoResponse, actual);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException() {
        when(repository.findByIdWithUser(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(dtoRequest, ID));
    }

    @Test
    void update_ShouldThrowInvalidDataException() {
        when(repository.findByIdWithUser(ID)).thenReturn(Optional.of(card));
        when(mapper.dtoToEntity(dtoRequest)).thenReturn(newCard);
        when(repository.save(newCard)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(InvalidDataException.class, () -> service.update(dtoRequest, ID));
    }

    @Test
    void findById_ShouldReturnPayCardDtoResponse() {
        when(repository.findById(ID)).thenReturn(Optional.of(card));
        when(mapper.entityToDto(card)).thenReturn(dtoResponse);

        PayCardDtoResponse actual = service.findById(ID);

        assertEquals(dtoResponse, actual);
    }

    @Test
    void findById_ShouldThrowEntityNotFoundException() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(ID));
    }

    @Test
    void findCards_ShouldCallFindAllMethodOfRepositoryAndReturnPageOfPayCardDtoResponse() {
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(card)));
        when(mapper.entityToDto(card)).thenReturn(dtoResponse);

        Page<PayCardDtoResponse> actual = service.findCards(NUMBER, SIZE);

        verify(repository, times(1)).findAll(any(Pageable.class));
        assertEquals(new PageImpl<>(List.of(dtoResponse)), actual);
    }

    @Test
    void findCardsByUserId_ShouldCallFindByUserIdMethodOfRepositoryAndReturnListOfPayCardDtoResponse() {
        when(repository.findByUserId(ID)).thenReturn(List.of(card));
        when(mapper.entityToDto(card)).thenReturn(dtoResponse);

        List<PayCardDtoResponse> actual = service.findCardsByUserId(ID);

        verify(repository, times(1)).findByUserId(ID);
        assertEquals(List.of(dtoResponse), actual);
    }

    @Test
    void activate_ShouldCallActivateCardMethodOfRepository() {
        when(repository.findByIdWithUser(ID)).thenReturn(Optional.of(card));
        when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);
        when(card.getUser()).thenReturn(user);

        service.activate(ID);

        verify(cacheManager, times(1)).getCache(CACHE_NAME);
        verify(repository, times(1)).activateCard(ID);
    }

    @Test
    void activate_ShouldThrowEntityNotFoundException() {
        when(repository.findByIdWithUser(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.activate(ID));
    }

    @Test
    void deactivate_ShouldCallDeactivateCardMethodOfRepository() {
        when(repository.findByIdWithUser(ID)).thenReturn(Optional.of(card));
        when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);
        when(card.getUser()).thenReturn(user);

        service.deactivate(ID);

        verify(cacheManager, times(1)).getCache(CACHE_NAME);
        verify(repository, times(1)).deactivateCard(ID);
    }

    @Test
    void deactivate_ShouldThrowEntityNotFoundException() {
        when(repository.findByIdWithUser(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deactivate(ID));
    }
}