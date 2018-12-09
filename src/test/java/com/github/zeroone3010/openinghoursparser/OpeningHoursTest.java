package com.github.zeroone3010.openinghoursparser;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpeningHoursTest {
  @Test
  void oneSingleDay() {
    final OpeningHours openingHours = new OpeningHours();
    final List<Token> result = openingHours.tokenize("Mon 10:00-18:00");
    assertEquals(Arrays.asList(
        new Token(TokenType.MONDAY, "Mon"),
        new Token(TokenType.TIME, "10:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "18:00")
    ), result);

    assertTrue(openingHours.validate(result).isValid());

    final WeeklySchedule schedule = openingHours.compile(result);
    assertEquals(new DailySchedule(LocalTime.of(10, 0), LocalTime.of(18, 0)), schedule.get(DayOfWeek.MONDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.TUESDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.WEDNESDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.THURSDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.FRIDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.SATURDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.SUNDAY));
  }

  @Test
  void twoSingleDays() {
    final OpeningHours openingHours = new OpeningHours();
    final List<Token> result = openingHours.tokenize("Mon 10:00-18:00, Tue 12:00-20:00");
    assertEquals(Arrays.asList(
        new Token(TokenType.MONDAY, "Mon"),
        new Token(TokenType.TIME, "10:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "18:00"),
        new Token(TokenType.SCHEDULE_SEPARATOR, ","),
        new Token(TokenType.TUESDAY, "Tue"),
        new Token(TokenType.TIME, "12:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "20:00")
    ), result);

    assertTrue(openingHours.validate(result).isValid());

    final WeeklySchedule schedule = openingHours.compile(result);
    assertEquals(new DailySchedule(LocalTime.of(10, 0), LocalTime.of(18, 0)), schedule.get(DayOfWeek.MONDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.TUESDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.WEDNESDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.THURSDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.FRIDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.SATURDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.SUNDAY));
  }

  @Test
  void oneSingleDayAndDayRange() {
    final OpeningHours openingHours = new OpeningHours();
    final List<Token> result = openingHours.tokenize("Mon 10:00-18:00, Tue-Sun 12:00-20:00");
    assertEquals(Arrays.asList(
        new Token(TokenType.MONDAY, "Mon"),
        new Token(TokenType.TIME, "10:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "18:00"),
        new Token(TokenType.SCHEDULE_SEPARATOR, ","),
        new Token(TokenType.TUESDAY, "Tue"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.SUNDAY, "Sun"),
        new Token(TokenType.TIME, "12:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "20:00")
    ), result);

    assertTrue(openingHours.validate(result).isValid());

    final WeeklySchedule schedule = openingHours.compile(result);
    assertEquals(new DailySchedule(LocalTime.of(10, 0), LocalTime.of(18, 0)), schedule.get(DayOfWeek.MONDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.TUESDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.WEDNESDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.THURSDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.FRIDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.SATURDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.SUNDAY));
  }

  @Test
  void twoDayRanges() {
    final OpeningHours openingHours = new OpeningHours();
    final List<Token> result = openingHours.tokenize("Mon-Wed 10:00-18:00, Thu-Sun 12:00-20:00");
    assertEquals(Arrays.asList(
        new Token(TokenType.MONDAY, "Mon"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.WEDNESDAY, "Wed"),
        new Token(TokenType.TIME, "10:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "18:00"),
        new Token(TokenType.SCHEDULE_SEPARATOR, ","),
        new Token(TokenType.THURSDAY, "Thu"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.SUNDAY, "Sun"),
        new Token(TokenType.TIME, "12:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "20:00")
    ), result);

    assertTrue(openingHours.validate(result).isValid());

    final WeeklySchedule schedule = openingHours.compile(result);
    assertEquals(new DailySchedule(LocalTime.of(10, 0), LocalTime.of(18, 0)), schedule.get(DayOfWeek.MONDAY));
    assertEquals(new DailySchedule(LocalTime.of(10, 0), LocalTime.of(18, 0)), schedule.get(DayOfWeek.TUESDAY));
    assertEquals(new DailySchedule(LocalTime.of(10, 0), LocalTime.of(18, 0)), schedule.get(DayOfWeek.WEDNESDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.THURSDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.FRIDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.SATURDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.SUNDAY));
  }

  @Test
  void oneRange() {
    final OpeningHours openingHours = new OpeningHours();
    final List<Token> result = openingHours.tokenize("Fri-Sun 09:00-22:00");
    assertEquals(Arrays.asList(
        new Token(TokenType.FRIDAY, "Fri"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.SUNDAY, "Sun"),
        new Token(TokenType.TIME, "09:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "22:00")
    ), result);

    assertTrue(openingHours.validate(result).isValid());

    final WeeklySchedule schedule = openingHours.compile(result);
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.MONDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.TUESDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.WEDNESDAY));
    assertEquals(DailySchedule.closed(), schedule.get(DayOfWeek.THURSDAY));
    assertEquals(new DailySchedule(LocalTime.of(9, 0), LocalTime.of(22, 0)), schedule.get(DayOfWeek.FRIDAY));
    assertEquals(new DailySchedule(LocalTime.of(9, 0), LocalTime.of(22, 0)), schedule.get(DayOfWeek.SATURDAY));
    assertEquals(new DailySchedule(LocalTime.of(9, 0), LocalTime.of(22, 0)), schedule.get(DayOfWeek.SUNDAY));
  }

  @Test
  void oneRange_withSpaces() {
    final OpeningHours openingHours = new OpeningHours();
    final List<Token> result = openingHours.tokenize(" Fri -  Sun   09:00  - 22:00  ");
    assertEquals(Arrays.asList(
        new Token(TokenType.FRIDAY, "Fri"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.SUNDAY, "Sun"),
        new Token(TokenType.TIME, "09:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "22:00")
    ), result);

    assertTrue(openingHours.validate(result).isValid());
  }

  @Test
  void invalidSingleDayOrRangeInput() {
    final OpeningHours openingHours = new OpeningHours();
    assertFalse(openingHours.validate(openingHours.tokenize("-")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("- ")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("10:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon 10:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon 10:00 - ")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon 10:00 - Tue")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon Tue")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon - Tue")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon - Tue 10:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon - Tue 10:00-")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon - Tue 10:00 -")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon - Tue 10:00 - ")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon - Tue 10:00 - Wed")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon - 12:00 13:00 - 14:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("10:00 - Mon 13:00 - 14:00")).isValid());
  }

}