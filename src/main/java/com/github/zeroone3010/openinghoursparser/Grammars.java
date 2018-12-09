package com.github.zeroone3010.openinghoursparser;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public final class Grammars {
  private Grammars() {
    // prevent instantiation
  }

  public static List<Rule> defaultGrammar() {
    // OpeningHours -> Schedule
    // Schedule -> WeekdayExpression TimeRange MoreSchedules
    // MoreSchedules -> schedule_separator Schedule
    // MoreSchedules -> ε
    // WeekdayExpression -> Weekday WeekdayRange
    // Weekday -> monday
    // Weekday -> tuesday
    // Weekday -> wednesday
    // Weekday -> thursday
    // Weekday -> friday
    // Weekday -> saturday
    // Weekday -> sunday
    // WeekdayRange -> range_indicator Weekday
    // WeekdayRange -> ε
    // TimeRange -> time range_indicator time

    final List<Rule> grammar = new ArrayList<>();
    grammar.add(new Rule(TokenType.OPENING_HOURS, asList(TokenType.SCHEDULE)));
    grammar.add(new Rule(TokenType.SCHEDULE, asList(TokenType.WEEKDAY_EXPRESSION, TokenType.TIME_RANGE, TokenType.MORE_SCHEDULES)));
    grammar.add(new Rule(TokenType.MORE_SCHEDULES, asList(TokenType.SCHEDULE_SEPARATOR, TokenType.SCHEDULE)));
    grammar.add(new Rule(TokenType.MORE_SCHEDULES, asList(TokenType.EMPTY)));
    grammar.add(new Rule(TokenType.WEEKDAY_EXPRESSION, asList(TokenType.WEEKDAY, TokenType.WEEKDAY_RANGE)));
    grammar.add(new Rule(TokenType.WEEKDAY, asList(TokenType.MONDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY, asList(TokenType.TUESDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY, asList(TokenType.WEDNESDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY, asList(TokenType.THURSDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY, asList(TokenType.FRIDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY, asList(TokenType.SATURDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY, asList(TokenType.SUNDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY_RANGE, asList(TokenType.RANGE_INDICATOR, TokenType.WEEKDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY_RANGE, asList(TokenType.EMPTY)));
    grammar.add(new Rule(TokenType.TIME_RANGE, asList(TokenType.TIME, TokenType.RANGE_INDICATOR, TokenType.TIME)));
    return grammar;
  }
}
