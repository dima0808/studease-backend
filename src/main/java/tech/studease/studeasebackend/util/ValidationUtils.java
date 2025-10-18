package tech.studease.studeasebackend.util;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;
import tech.studease.studeasebackend.common.CustomErrorResponse;

public class ValidationUtils {

  public static CustomErrorResponse getErrorResponseOfFieldErrors(
      List<ObjectError> errors, WebRequest request) {
    return CustomErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message(
            errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")))
        .path(request.getDescription(false).substring(4))
        .build();
  }
}
