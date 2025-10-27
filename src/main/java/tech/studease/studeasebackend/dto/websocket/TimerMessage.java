package tech.studease.studeasebackend.dto.websocket;

import tech.studease.studeasebackend.dto.TestSessionDto;

public record TimerMessage(TimerMessageType type, int timeLeft, TestSessionDto testSession) {

  public static TimerMessage of(TimerMessageType type, int timeLeft) {
    return new TimerMessage(type, timeLeft, null);
  }

  public static TimerMessage of(TimerMessageType type, int timeLeft, TestSessionDto testSession) {
    return new TimerMessage(type, timeLeft, testSession);
  }
}
