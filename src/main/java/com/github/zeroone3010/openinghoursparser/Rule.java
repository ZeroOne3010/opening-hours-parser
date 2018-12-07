package com.github.zeroone3010.openinghoursparser;

import java.util.List;
import java.util.Objects;

final class Rule {
  final TokenType left;
  final List<TokenType> right;

  public Rule(final TokenType left, final List<TokenType> right) {
    if (left.isTerminal()) {
      throw new IllegalArgumentException("Terminal symbol " + left + " cannot appear at the left hand side of a rule.");
    }
    this.left = left;
    this.right = right;
  }

  public TokenType getLeft() {
    return left;
  }

  public List<TokenType> getRight() {
    return right;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Rule rule = (Rule) o;
    if (!left.equals(rule.left)) return false;
    if (!right.equals(rule.right)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(left, right);
  }

  @Override
  public String toString() {
    return "Rule{" + left + " -> " + right + '}';
  }
}