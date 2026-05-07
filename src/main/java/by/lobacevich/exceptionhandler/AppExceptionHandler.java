package by.lobacevich.exceptionhandler;

import by.lobacevich.dto.response.ErrorDto;
import by.lobacevich.exception.EntityNotFoundException;
import by.lobacevich.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    private static final String ERROR_LOG_FRAME = "{}, {}, {}";

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorDto> handleInvalidDataException(InvalidDataException e) {
        log.error(ERROR_LOG_FRAME, e.getMessage(), e.getClass().getSimpleName(), e.getStackTrace());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorDto> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error(ERROR_LOG_FRAME, e.getMessage(), e.getClass().getSimpleName(), e.getStackTrace());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        log.error(ERROR_LOG_FRAME, e.getMessage(), e.getClass().getSimpleName(), e.getStackTrace());
        return new ResponseEntity<>(new ErrorDto(String.join(", ", errors)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error(ERROR_LOG_FRAME, e.getMessage(), e.getClass().getSimpleName(), e.getStackTrace());
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
