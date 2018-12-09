package com.github.zeroone3010.openinghoursparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedTokensTest {
  @Test
  void testWeekdays() {
    assertEquals(TokenType.MONDAY, LocalizedTokens.match("Mon").get().getType());
    assertEquals(TokenType.TUESDAY, LocalizedTokens.match("Tue").get().getType());
    assertEquals(TokenType.WEDNESDAY, LocalizedTokens.match("Wed").get().getType());
    assertEquals(TokenType.THURSDAY, LocalizedTokens.match("Thu").get().getType());
    assertEquals(TokenType.FRIDAY, LocalizedTokens.match("Fri").get().getType());
    assertEquals(TokenType.SATURDAY, LocalizedTokens.match("Sat").get().getType());
    assertEquals(TokenType.SUNDAY, LocalizedTokens.match("Sun").get().getType());
  }
}