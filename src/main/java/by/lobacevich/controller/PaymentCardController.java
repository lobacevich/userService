package by.lobacevich.controller;

import by.lobacevich.dto.request.PayCardDtoRequest;
import by.lobacevich.dto.response.PayCardDtoResponse;
import by.lobacevich.service.PaymentCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN') or @paymentCardServiceImpl.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<PayCardDtoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PayCardDtoResponse>> findAll(
            @RequestParam(value = "size", defaultValue = "20", required = false) int size,
            @RequestParam(value = "number", defaultValue = "0", required = false) int number) {
        return ResponseEntity.ok(service.findCards(number, size));
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.getUserId()")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<PayCardDtoResponse>> findByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findCardsByUserId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PayCardDtoResponse> createCard(@Valid @RequestBody PayCardDtoRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        service.activate(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivate(id);
    }

    @PreAuthorize("hasRole('ADMIN') or @paymentCardServiceImpl.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<PayCardDtoResponse> update(@Valid @RequestBody PayCardDtoRequest dtoRequest,
                                                     @PathVariable Long id) {
        return ResponseEntity.ok(service.update(dtoRequest, id));
    }
}
