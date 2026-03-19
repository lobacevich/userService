package by.lobacevich.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PayCardDtoRequest(@NotNull(message = "User id is required")
                                Long userId,

                                @NotBlank(message = "Card number is required")
                                @Size(min = 16, max = 16, message = "Card number must contain 16")
                                String number,

                                @NotBlank(message = "Card holder is required")
                                @Size(min = 5, max = 63, message = "Holder name length must be between 5 and 63")
                                String holder,

                                @NotNull(message = "Expiration date is required")
                                LocalDate expirationDate) {
}
