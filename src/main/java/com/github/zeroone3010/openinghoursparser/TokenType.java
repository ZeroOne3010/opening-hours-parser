package com.github.zeroone3010.openinghoursparser;

import java.time.DayOfWeek;
import java.util.Arrays;

enum TokenType {
  WEEKDAY(false), TIME(true), RANGE_INDICATOR(true), WHITE_SPACE(true),
  OPENING_HOURS(false), WEEKDAY_EXPRESSION(false), TIME_RANGE(false), WEEKDAY_RANGE(false),
  SCHEDULE(false), MORE_SCHEDULES(false), SCHEDULE_SEPARATOR(true), END_OF_INPUT(true), EMPTY(true),
  MONDAY(true), TUESDAY(true), WEDNESDAY(true), THURSDAY(true), FRIDAY(true), SATURDAY(true), SUNDAY(true),
  UNKNOWN(true);

  private final boolean terminal;

  TokenType(final boolean terminal) {
    this.terminal = terminal;
  }

  public boolean isTerminal() {
    return terminal;
  }

  public boolean isStart() {
    return this == OPENING_HOURS;
  }

  public boolean isEndOfInput() {
    return this == END_OF_INPUT;
  }

  public boolean isEmpty() {
    return this == EMPTY;
  }

  public boolean isWeekday() {
    return Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY).contains(this);
  }

  public boolean isUnknown() {
    return this == UNKNOWN;
  }

  public DayOfWeek asDayOfWeek() {
    if (!isWeekday()) {
      throw new IllegalStateException(this + " is not a day of week.");
    }
    return DayOfWeek.valueOf(this.name());
  }

  public static TokenType getStartSymbol() {
    return OPENING_HOURS;
  }

  public static TokenType getEndOfInputSymbol() {
    return END_OF_INPUT;
  }
}
