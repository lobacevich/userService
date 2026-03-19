package by.lobacevich.mapper;

import by.lobacevich.dto.request.PayCardDtoRequest;
import by.lobacevich.dto.response.PayCardDtoResponse;
import by.lobacevich.entity.PaymentCard;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentCardMapper {

    PayCardDtoResponse entityToDto(PaymentCard card);

    PaymentCard dtoToEntity(PayCardDtoRequest dto);
}
