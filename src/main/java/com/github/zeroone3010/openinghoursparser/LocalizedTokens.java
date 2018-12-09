package com.github.zeroone3010.openinghoursparser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class LocalizedTokens {
  private static final Map<String, TokenType> TOKENS = new HashMap<>();

  static {
    TOKENS.put("Mon", TokenType.MONDAY);
    TOKENS.put("Tue", TokenType.TUESDAY);
    TOKENS.put("Wed", TokenType.WEDNESDAY);
    TOKENS.put("Thu", TokenType.THURSDAY);
    TOKENS.put("Fri", TokenType.FRIDAY);
    TOKENS.put("Sat", TokenType.SATURDAY);
    TOKENS.put("Sun", TokenType.SUNDAY);
    TOKENS.put("-", TokenType.RANGE_INDICATOR);
    TOKENS.put("\\d\\d:\\d\\d", TokenType.TIME);
    TOKENS.put("\\s", TokenType.WHITE_SPACE);
    TOKENS.put(",", TokenType.SCHEDULE_SEPARATOR);
  }

  static Optional<Token> match(final String candidate) {
    return TOKENS.keySet().stream()
        .filter(candidate::matches)
        .map(TOKENS::get)
        .findAny()
        .map(type -> new Token(type, candidate));
  }

}
