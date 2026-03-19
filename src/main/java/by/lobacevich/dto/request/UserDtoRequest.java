package by.lobacevich.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserDtoRequest(@NotBlank(message = "Username is required")
                             @Size(min = 2, max = 63, message = "Username length must be between 3 and 63")
                             String name,

                             @NotBlank(message = "Surname is required")
                             @Size(min = 2, max = 63, message = "Surname length must be between 3 and 63")
                             String surname,

                             @NotNull(message = "Birth date is required")
                             @Past(message = "Incorrect date")
                             LocalDate birthDate,

                             @Email(message = "Incorrect email")
                             String email) {
}
