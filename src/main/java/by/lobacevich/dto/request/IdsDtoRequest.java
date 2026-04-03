package by.lobacevich.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record IdsDtoRequest(@NotEmpty(message = "User ids are required")
                            List<Long> ids) {
}
