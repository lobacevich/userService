package by.lobacevich.service;

import by.lobacevich.dto.request.PayCardDtoRequest;
import by.lobacevich.dto.response.PayCardDtoResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PaymentCardService {

    PayCardDtoResponse create(PayCardDtoRequest dto);

    PayCardDtoResponse update(PayCardDtoRequest dto, Long id);

    PayCardDtoResponse findById(Long id);

    Page<PayCardDtoResponse> findCards(int number,
                                       int size);

    List<PayCardDtoResponse> findCardsByUserId(Long id);

    void activate(Long id);

    void deactivate(Long id);
}
