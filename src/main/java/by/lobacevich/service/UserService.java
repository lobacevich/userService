package by.lobacevich.service;

import by.lobacevich.dto.request.UserDtoRequest;
import by.lobacevich.dto.response.UserDtoResponse;
import by.lobacevich.dto.response.UserWithCardsDto;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDtoResponse create(UserDtoRequest dto);

    UserDtoResponse update(UserDtoRequest dto, Long id);

    UserWithCardsDto findById(Long id);

    Page<UserDtoResponse> findUsers(String firstname,
                                    String surname,
                                    int number,
                                    int size);

    void activate(Long id);

    void deactivate(Long id);
}
