package com.github.zeroone3010.openinghoursparser;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
  void sevenDaysInFinnish() {
    final OpeningHours openingHours = new OpeningHours(Grammars.defaultGrammar(), new Locale("fi", "FI"));
    final List<Token> tokens = openingHours.tokenize("ma 10:00-20:00, ti 11:00-20:30, ke 12:00-21:00, to 13:00-21:30, pe 14:00-22:00, la 15:00-22:30, su 16:00-23:00");
    assertTrue(openingHours.validate(tokens).isValid());

    final WeeklySchedule schedule = openingHours.compile(tokens);
    assertEquals(new DailySchedule(LocalTime.of(10, 0), LocalTime.of(20, 0)), schedule.get(DayOfWeek.MONDAY));
    assertEquals(new DailySchedule(LocalTime.of(11, 0), LocalTime.of(20, 30)), schedule.get(DayOfWeek.TUESDAY));
    assertEquals(new DailySchedule(LocalTime.of(12, 0), LocalTime.of(21, 0)), schedule.get(DayOfWeek.WEDNESDAY));
    assertEquals(new DailySchedule(LocalTime.of(13, 0), LocalTime.of(21, 30)), schedule.get(DayOfWeek.THURSDAY));
    assertEquals(new DailySchedule(LocalTime.of(14, 0), LocalTime.of(22, 0)), schedule.get(DayOfWeek.FRIDAY));
    assertEquals(new DailySchedule(LocalTime.of(15, 0), LocalTime.of(22, 30)), schedule.get(DayOfWeek.SATURDAY));
    assertEquals(new DailySchedule(LocalTime.of(16, 0), LocalTime.of(23, 0)), schedule.get(DayOfWeek.SUNDAY));
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

  @Test
  void invalidMultiDayInput() {
    final OpeningHours openingHours = new OpeningHours();
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue 10:00-12:00, Wed")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue 10:00-12:00, Wed -")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue 10:00-12:00, Wed - Fri")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue 10:00-12:00, Wed - Fri 08:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue 10:00-12:00, Wed - Fri 08:00-")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue 10:00-, Wed - Fri 08:00-20:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue -10:00, Wed - Fri 08:00-20:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon- -10:00, Wed - Fri 08:00-20:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("-Tue 08:00-10:00, Wed - Fri 08:00-20:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Tue Mon-Tue, Mon-Tue Mon-Tue")).isValid());
  }

  @Test
  void invalidInput() {
    final OpeningHours openingHours = new OpeningHours();
    assertFalse(openingHours.validate(openingHours.tokenize("Foo")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Foo ")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Foo  ")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Bar 10:00-12:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Baz Mon-Sun 10:00-12:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon foo-12:00")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon 12:00-quux")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon 12:00-13:00, zok")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("12:00-13:00, baz")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Sun 10:00-19:00 (except on Fridays)")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Sun 10:00-19:00 something")).isValid());
    assertFalse(openingHours.validate(openingHours.tokenize("Mon-Fri 10:00-19:00 (closed next week), Sat-Sun 12:00-16:00")).isValid());
  }

}