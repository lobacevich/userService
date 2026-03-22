package by.lobacevich.mapper;

import by.lobacevich.dto.request.UserDtoRequest;
import by.lobacevich.dto.response.UserDtoResponse;
import by.lobacevich.dto.response.UserWithCardsDto;
import by.lobacevich.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDtoResponse userToDto(User user);

    User dtoToUser(UserDtoRequest dto);

    UserWithCardsDto userToDtoWithCards(User user);
}
