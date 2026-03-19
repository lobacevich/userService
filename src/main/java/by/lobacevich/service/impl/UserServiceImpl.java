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
import by.lobacevich.service.UserService;
import by.lobacevich.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PaymentCardRepository cardRepository;

    @Override
    public UserDtoResponse create(UserDtoRequest dto) {
        try {
            return mapper.userToDto(repository.save(mapper.dtoToUser(dto)));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate email");
        }
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    @Override
    public UserDtoResponse update(UserDtoRequest dto, Long id) {
        User oldUser = repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id " + id));
        User newUser = mapper.dtoToUser(dto);
        newUser.setId(id);
        newUser.setActive(oldUser.getActive());
        newUser.setCreatedAt(oldUser.getCreatedAt());
        try {
            return mapper.userToDto(repository.save(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Duplicate email");
        }
    }

    @Cacheable(value = "users", key = "#id")
    @Override
    public UserWithCardsDto findById(Long id) {
        return mapper.userToDtoWithCards(repository.findByIdWithCards(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id " + id)));
    }

    @Override
    public Page<UserDtoResponse> findUsers(String firstname,
                                           String surname,
                                           int number,
                                           int size) {
        Pageable pageable = PageRequest.of(number, size);
        Specification<User> spec = UserSpecification.filterBy(firstname, surname);
        return repository.findAll(spec, pageable).map(mapper::userToDto);
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    @Override
    public void activate(Long id) {
        if (repository.activateUser(id) == 0) {
            throw new EntityNotFoundException("User not found with id " + id);
        }
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    @Override
    public void deactivate(Long id) {
        if (repository.deactivateUser(id) == 0) {
            throw new EntityNotFoundException("User not found with id " + id);
        }
        cardRepository.deactivateByUserId(id);
    }
}
