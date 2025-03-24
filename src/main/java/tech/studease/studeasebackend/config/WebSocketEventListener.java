package tech.studease.studeasebackend.config;

import java.time.LocalDateTime;
import tech.studease.studeasebackend.repository.entity.TestSession;
import tech.studease.studeasebackend.service.TestSessionService;
import tech.studease.studeasebackend.service.exception.TestSessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

  private final TestSessionService testSessionService;

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    try {
      TestSession testSession = testSessionService.findBySessionId(sessionId);
      testSession.setFinishedAt(LocalDateTime.now());
      testSessionService.save(testSession);
    } catch (TestSessionNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
