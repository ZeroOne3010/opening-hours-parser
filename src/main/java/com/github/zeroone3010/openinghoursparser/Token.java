package com.github.zeroone3010.openinghoursparser;

import java.util.Objects;

final class Token {
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
