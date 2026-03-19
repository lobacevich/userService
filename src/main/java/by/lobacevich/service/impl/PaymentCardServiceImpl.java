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
import by.lobacevich.service.PaymentCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository repository;
    private final PaymentCardMapper mapper;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @CacheEvict(value = "users", key = "#dto.userId")
    @Transactional
    @Override
    public PayCardDtoResponse create(PayCardDtoRequest dto) {
        Long userId = dto.userId();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("User not found with id " + userId));
        if (repository.countByUserId(userId) >= 5) {
            throw new CardsLimitException("User with id" + userId + " can not have more then 5 cards");
        }
        PaymentCard card = mapper.dtoToEntity(dto);
        card.setUser(user);
        try {
            return mapper.entityToDto(repository.save(card));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate card number");
        }
    }

    @CacheEvict(value = "users", key = "#dto.userId")
    @Transactional
    @Override
    public PayCardDtoResponse update(PayCardDtoRequest dto, Long id) {
        PaymentCard oldCard = repository.findByIdWithUser(id).orElseThrow(() ->
                new EntityNotFoundException("Card not found with id " + id));
        PaymentCard newCard = mapper.dtoToEntity(dto);
        newCard.setId(oldCard.getId());
        newCard.setUser(oldCard.getUser());
        newCard.setCreatedAt(oldCard.getCreatedAt());
        newCard.setActive(oldCard.getActive());
        try {
            return mapper.entityToDto(repository.save(newCard));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate card number");
        }
    }

    @Override
    public PayCardDtoResponse findById(Long id) {
        return mapper.entityToDto(repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Card not found with id " + id)));
    }

    @Override
    public Page<PayCardDtoResponse> findCards(int number,
                                              int size) {
        Pageable pageable = PageRequest.of(number, size);
        return repository.findAll(pageable).map(mapper::entityToDto);
    }

    @Override
    public List<PayCardDtoResponse> findCardsByUserId(Long id) {
        return repository.findByUserId(id).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void activate(Long id) {
        PaymentCard card = repository.findByIdWithUser(id).orElseThrow(() ->
                new EntityNotFoundException("Card not found with id " + id));
        Objects.requireNonNull(cacheManager.getCache("users")).evict(card.getUser().getId());
        repository.activateCard(id);
    }

    @Transactional
    @Override
    public void deactivate(Long id) {
        PaymentCard card = repository.findByIdWithUser(id).orElseThrow(() ->
                new EntityNotFoundException("Card not found with id " + id));
        Objects.requireNonNull(cacheManager.getCache("users")).evict(card.getUser().getId());
        repository.deactivateCard(id);
    }
}
