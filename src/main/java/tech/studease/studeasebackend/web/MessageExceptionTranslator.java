package tech.studease.studeasebackend.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import tech.studease.studeasebackend.common.CustomErrorResponse;
import tech.studease.studeasebackend.service.exception.TestNotFoundException;
import tech.studease.studeasebackend.service.exception.TestSessionAlreadyExistsException;
import tech.studease.studeasebackend.service.exception.TestSessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class MessageExceptionTranslator {

  private final SimpMessagingTemplate messagingTemplate;

  @MessageExceptionHandler({TestNotFoundException.class, TestSessionNotFoundException.class})
  public CustomErrorResponse handleNotFoundException(RuntimeException exc,
      @Header("credentials") String credentials) {
    CustomErrorResponse errorResponse = CustomErrorResponse.builder()
        .status(NOT_FOUND.value())
        .error(NOT_FOUND.getReasonPhrase())
        .message(exc.getMessage())
        .build();
    messagingTemplate.convertAndSendToUser(credentials, "/queue/errors", errorResponse);
    return errorResponse;
  }

  @MessageExceptionHandler({TestSessionAlreadyExistsException.class, IllegalStateException.class,
      IllegalArgumentException.class})
  public CustomErrorResponse handleBadRequestException(RuntimeException exc,
      @Header("credentials") String credentials) {
    CustomErrorResponse errorResponse = CustomErrorResponse.builder()
        .status(BAD_REQUEST.value())
        .error(BAD_REQUEST.getReasonPhrase())
        .message(exc.getMessage())
        .build();
    messagingTemplate.convertAndSendToUser(credentials, "/queue/errors", errorResponse);
    return errorResponse;
  }
}
