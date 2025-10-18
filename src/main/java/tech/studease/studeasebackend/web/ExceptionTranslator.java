package tech.studease.studeasebackend.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static tech.studease.studeasebackend.util.ValidationUtils.getErrorResponseOfFieldErrors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tech.studease.studeasebackend.common.CustomErrorResponse;
import tech.studease.studeasebackend.service.exception.CollectionAlreadyExistsException;
import tech.studease.studeasebackend.service.exception.CollectionNotFoundException;
import tech.studease.studeasebackend.service.exception.QuestionMergeConflictException;
import tech.studease.studeasebackend.service.exception.TestNotFoundException;
import tech.studease.studeasebackend.service.exception.TestSessionNotFoundException;

@ControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<CustomErrorResponse> handleBadCredentialsException(WebRequest request) {
    CustomErrorResponse errorResponse =
        CustomErrorResponse.builder()
            .status(UNAUTHORIZED.value())
            .error(UNAUTHORIZED.getReasonPhrase())
            .message("Wrong password")
            .path(request.getDescription(false).substring(4))
            .build();
    return ResponseEntity.status(UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler({
    TestNotFoundException.class,
    CollectionNotFoundException.class,
    TestSessionNotFoundException.class
  })
  public ResponseEntity<CustomErrorResponse> handleNotFoundException(
      RuntimeException exc, WebRequest request) {
    CustomErrorResponse errorResponse =
        CustomErrorResponse.builder()
            .status(NOT_FOUND.value())
            .error(NOT_FOUND.getReasonPhrase())
            .message(exc.getMessage())
            .path(request.getDescription(false).substring(4))
            .build();
    return ResponseEntity.status(NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler({
    CollectionAlreadyExistsException.class,
    IllegalArgumentException.class,
    QuestionMergeConflictException.class
  })
  public ResponseEntity<CustomErrorResponse> handleBadRequestException(
      RuntimeException exc, WebRequest request) {
    CustomErrorResponse errorResponse =
        CustomErrorResponse.builder()
            .status(BAD_REQUEST.value())
            .error(BAD_REQUEST.getReasonPhrase())
            .message(exc.getMessage())
            .path(request.getDescription(false).substring(4))
            .build();
    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(getErrorResponseOfFieldErrors(ex.getBindingResult().getAllErrors(), request));
  }
}
