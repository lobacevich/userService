package by.lobacevich.dto.response;

public record PayCardDtoResponse(Long id,
                                 String number,
                                 String holder,
                                 String expirationDate,
                                 Boolean active,
                                 String createdAt,
                                 String updatedAt) {
}
