package tech.studease.studeasebackend.dto.websocket;

public record TimerMessage(TimerMessageType type, int timeLeft) {

  public static TimerMessage of(TimerMessageType type, int timeLeft) {
    return new TimerMessage(type, timeLeft);
  }
}
