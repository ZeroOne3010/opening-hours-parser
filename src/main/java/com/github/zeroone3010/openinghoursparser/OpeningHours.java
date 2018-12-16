package com.github.zeroone3010.openinghoursparser;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class OpeningHours {
  private final Parser parser;
  private final LocalizedTokens localizedTokens;

  public OpeningHours() {
    this(Grammars.defaultGrammar(), Locale.ENGLISH);
  }

  public OpeningHours(final List<Rule> grammar, final Locale locale) {
    parser = new Parser(grammar);
    localizedTokens = new LocalizedTokens(locale);
  }

  public List<Token> tokenize(final String input) {
    System.out.println("Input: '" + input + "'");
    final List<Token> tokens = new ArrayList<>();
    int startingPosition = 0;
    for (int i = 1; i <= input.length(); i++) {
      final String candidate = input.substring(startingPosition, i);
      final Optional<Token> optionalToken = localizedTokens.match(candidate);
      if (optionalToken.isPresent()) {
        tokens.add(optionalToken.get());
        startingPosition = i;
      } else if (i == input.length()) {
        tokens.add(new Token(TokenType.UNKNOWN, candidate));
      }
    }
    tokens.removeIf(token -> token.getType().equals(TokenType.WHITE_SPACE));
    return tokens;
  }

  public ValidationResult validate(final List<Token> tokens) {
    return parser.validate(tokens);
  }

  public WeeklySchedule compile(final List<Token> tokens) {
    final WeeklySchedule.Builder builder = WeeklySchedule.builder();

    DayOfWeek dayOfWeekRangeStart = null;
    DayOfWeek dayOfWeekRangeEnd = null;
    LocalTime timeRangeStart = null;
    for (final Token token : tokens) {
      if (token.getType().isWeekday()) {
        if (dayOfWeekRangeStart == null) {
          dayOfWeekRangeStart = token.getType().asDayOfWeek();
          dayOfWeekRangeEnd = token.getType().asDayOfWeek();
        } else {
          dayOfWeekRangeEnd = token.getType().asDayOfWeek();
        }
      } else if (token.getType().equals(TokenType.TIME)) {
        if (timeRangeStart == null) {
          timeRangeStart = LocalTime.parse(token.getValue());
        } else {
          final LocalTime timeRangeEnd = LocalTime.parse(token.getValue());

          for (int i = dayOfWeekRangeStart.getValue(); i <= dayOfWeekRangeEnd.getValue(); i++) {
            builder.add(DayOfWeek.of(i), new DailySchedule(timeRangeStart, timeRangeEnd));
          }

          dayOfWeekRangeStart = null;
          dayOfWeekRangeEnd = null;
          timeRangeStart = null;
        }
      }
    }
    return builder.build();
  }
}

