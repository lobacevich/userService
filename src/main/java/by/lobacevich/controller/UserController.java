package by.lobacevich.controller;

import by.lobacevich.dto.request.UserDtoRequest;
import by.lobacevich.dto.response.UserDtoResponse;
import by.lobacevich.dto.response.UserWithCardsDto;
import by.lobacevich.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserWithCardsDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDtoResponse>> findAll(
            @RequestParam(value = "firstname", required = false) String firstname,
            @RequestParam(value = "surname", required = false) String surname,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "number", defaultValue = "0", required = false) int number) {
        return ResponseEntity.ok(service.findUsers(firstname, surname, number, size));
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDtoRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        service.activate(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDtoResponse> update(@Valid @RequestBody UserDtoRequest dtoRequest,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(service.update(dtoRequest, id));
    }
}
