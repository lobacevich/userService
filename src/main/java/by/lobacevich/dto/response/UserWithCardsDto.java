package by.lobacevich.dto.response;

import java.util.List;

public record UserWithCardsDto(Long id,
                               String name,
                               String surname,
                               String birthDate,
                               String email,
                               Boolean active,
                               String createdAt,
                               String updatedAt,
                               List<PayCardDtoResponse> paymentCards) {
}
