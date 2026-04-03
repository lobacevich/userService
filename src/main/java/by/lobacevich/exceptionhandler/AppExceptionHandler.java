package by.lobacevich.exceptionhandler;

import by.lobacevich.dto.response.ErrorDto;
import by.lobacevich.exception.EntityNotFoundException;
import by.lobacevich.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorDto> handleInvalidDataException(InvalidDataException e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(new ErrorDto(String.join(", ", errors)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("{}/{}", e.getMessage(), e.getClass().getSimpleName());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
