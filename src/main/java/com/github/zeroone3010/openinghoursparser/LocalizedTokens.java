package com.github.zeroone3010.openinghoursparser;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

final class LocalizedTokens {
  private final Map<String, TokenType> tokens = new LinkedHashMap<>();

  /**
   * Creates a LocalizedTokens instance for English names of the days of the week.
   */
  public LocalizedTokens() {
    this(Locale.ENGLISH);
  }

  /**
   * Creates a LocalizedTokens instance for the names of the days of the week in the given locale.
   *
   * @param locale A Locale that specifies the language that the names of the days of the week should use.
   */
  public LocalizedTokens(final Locale locale) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", locale);
    tokens.put(formatter.format(DayOfWeek.MONDAY), TokenType.MONDAY);
    tokens.put(formatter.format(DayOfWeek.TUESDAY), TokenType.TUESDAY);
    tokens.put(formatter.format(DayOfWeek.WEDNESDAY), TokenType.WEDNESDAY);
    tokens.put(formatter.format(DayOfWeek.THURSDAY), TokenType.THURSDAY);
    tokens.put(formatter.format(DayOfWeek.FRIDAY), TokenType.FRIDAY);
    tokens.put(formatter.format(DayOfWeek.SATURDAY), TokenType.SATURDAY);
    tokens.put(formatter.format(DayOfWeek.SUNDAY), TokenType.SUNDAY);

    tokens.put("-", TokenType.RANGE_INDICATOR);
    tokens.put("\\d\\d:\\d\\d", TokenType.TIME);
    tokens.put("\\s", TokenType.WHITE_SPACE);
    tokens.put(",", TokenType.SCHEDULE_SEPARATOR);
    tokens.put(".+\\s", TokenType.UNKNOWN);
  }


  Optional<Token> match(final String candidate) {
    return tokens.keySet().stream()
        .filter(candidate::matches)
        .map(tokens::get)
        .findAny()
        .map(type -> new Token(type, candidate));
  }

}
