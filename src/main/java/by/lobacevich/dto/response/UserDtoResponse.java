package by.lobacevich.dto.response;

public record UserDtoResponse(Long id,
                              String name,
                              String surname,
                              String birthDate,
                              String email,
                              Boolean active,
                              String createdAt,
                              String updatedAt) {
}
