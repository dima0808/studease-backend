package tech.studease.studeasebackend.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    // Handle disconnection logic here
  }
}
