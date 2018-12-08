package com.github.zeroone3010.openinghoursparser;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpeningHoursTest {
  @Test
  void tokenizeAndValidate_withOnlyOneSingleDay() {
    final OpeningHours openingHours = new OpeningHours();
    final List<Token> result = openingHours.tokenize("Mon 10:00-18:00");
    assertEquals(Arrays.asList(
        new Token(TokenType.MONDAY, "Mon"),
        new Token(TokenType.TIME, "10:00"),
        new Token(TokenType.RANGE_INDICATOR, "-"),
        new Token(TokenType.TIME, "18:00")
    ), result);

    assertTrue(openingHours.validate(result).isValid());
  }

  @Test
  void tokenizeAndValidate_withTwoSingleDays() {
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
  }

  @Test
  void tokenizeAndValidate_withOneSingleDayAndDayRange() {
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
  }

  @Test
  void tokenizeAndValidate_withTwoDayRanges() {
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
  }

  @Test
  void tokenizeAndValidate_withOneRange() {
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
  }

  @Test
  void tokenizeAndValidate_withOneRange_withSpaces() {
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
  void tokenizeAndValidate_invalidSingleDayOrRangeInput() {
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