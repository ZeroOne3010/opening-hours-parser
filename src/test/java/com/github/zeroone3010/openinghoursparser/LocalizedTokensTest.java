package com.github.zeroone3010.openinghoursparser;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalizedTokensTest {
  @Test
  void testDefaultWeekdays() {
    final LocalizedTokens localizedTokens = new LocalizedTokens();
    assertEquals(TokenType.MONDAY, localizedTokens.match("Mon").get().getType());
    assertEquals(TokenType.TUESDAY, localizedTokens.match("Tue").get().getType());
    assertEquals(TokenType.WEDNESDAY, localizedTokens.match("Wed").get().getType());
    assertEquals(TokenType.THURSDAY, localizedTokens.match("Thu").get().getType());
    assertEquals(TokenType.FRIDAY, localizedTokens.match("Fri").get().getType());
    assertEquals(TokenType.SATURDAY, localizedTokens.match("Sat").get().getType());
    assertEquals(TokenType.SUNDAY, localizedTokens.match("Sun").get().getType());
  }

  @Test
  void testFinnishWeekdays() {
    final LocalizedTokens localizedTokens = new LocalizedTokens(new Locale("fi", "FI"));
    assertEquals(TokenType.MONDAY, localizedTokens.match("ma").get().getType());
    assertEquals(TokenType.TUESDAY, localizedTokens.match("ti").get().getType());
    assertEquals(TokenType.WEDNESDAY, localizedTokens.match("ke").get().getType());
    assertEquals(TokenType.THURSDAY, localizedTokens.match("to").get().getType());
    assertEquals(TokenType.FRIDAY, localizedTokens.match("pe").get().getType());
    assertEquals(TokenType.SATURDAY, localizedTokens.match("la").get().getType());
    assertEquals(TokenType.SUNDAY, localizedTokens.match("su").get().getType());
  }

  @Test
  void testSwedishWeekdays() {
    final LocalizedTokens localizedTokens = new LocalizedTokens(new Locale("sv", "SE"));
    assertEquals(TokenType.MONDAY, localizedTokens.match("mån").get().getType());
    assertEquals(TokenType.TUESDAY, localizedTokens.match("tis").get().getType());
    assertEquals(TokenType.WEDNESDAY, localizedTokens.match("ons").get().getType());
    assertEquals(TokenType.THURSDAY, localizedTokens.match("tors").get().getType());
    assertEquals(TokenType.FRIDAY, localizedTokens.match("fre").get().getType());
    assertEquals(TokenType.SATURDAY, localizedTokens.match("lör").get().getType());
    assertEquals(TokenType.SUNDAY, localizedTokens.match("sön").get().getType());
  }
}