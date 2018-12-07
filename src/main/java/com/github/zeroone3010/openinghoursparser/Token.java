package com.github.zeroone3010.openinghoursparser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

final class Token {
  private static final Map<String, TokenType> TOKENS = new HashMap<>();

  static {
    TOKENS.put("Mon", TokenType.WEEKDAY);
    TOKENS.put("Tue", TokenType.WEEKDAY);
    TOKENS.put("Wed", TokenType.WEEKDAY);
    TOKENS.put("Thu", TokenType.WEEKDAY);
    TOKENS.put("Fri", TokenType.WEEKDAY);
    TOKENS.put("Sat", TokenType.WEEKDAY);
    TOKENS.put("Sun", TokenType.WEEKDAY);
    TOKENS.put("-", TokenType.RANGE_INDICATOR);
    TOKENS.put("\\d\\d:\\d\\d", TokenType.TIME);
    TOKENS.put("\\s", TokenType.WHITE_SPACE);
    TOKENS.put(",", TokenType.SCHEDULE_SEPARATOR);
  }

  private final TokenType type;
  private final String value;

  public Token(final TokenType type, final String value) {
    this.type = type;
    this.value = value;
  }

  public Token(final TokenType type) {
    this.type = type;
    this.value = null;
  }

  static Optional<Token> match(final String candidate) {
    return TOKENS.keySet().stream()
        .filter(candidate::matches)
        .map(TOKENS::get)
        .findAny()
        .map(type -> new Token(type, candidate));
  }

  public TokenType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "{" + type + (value != null ? " " + value : "") + "}";
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Token token = (Token) o;
    return type == token.type && Objects.equals(value, token.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value);
  }
}
