package by.lobacevich.controller;

import by.lobacevich.dto.request.PayCardDtoRequest;
import by.lobacevich.dto.response.PayCardDtoResponse;
import by.lobacevich.service.PaymentCardService;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cards")
public class PaymentCardController {

    private final PaymentCardService service;

    @GetMapping("/{id}")
    public ResponseEntity<PayCardDtoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PayCardDtoResponse>> findAll(
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "number", defaultValue = "0", required = false) int number) {
        return ResponseEntity.ok(service.findCards(number, size));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PayCardDtoResponse>> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findCardsByUserId(id));
    }

    @PostMapping
    public ResponseEntity<PayCardDtoResponse> createCard(@Valid @RequestBody PayCardDtoRequest request) {
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
    public ResponseEntity<PayCardDtoResponse> update(@Valid @RequestBody PayCardDtoRequest dtoRequest,
                                                     @PathVariable Long id) {
        return ResponseEntity.ok(service.update(dtoRequest, id));
    }
}
