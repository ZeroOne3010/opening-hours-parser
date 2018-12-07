package com.github.zeroone3010.openinghoursparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class OpeningHours {
  private final ParsingTable parsingTable;

  // OpeningHours -> Schedule
  // Schedule -> WeekdayExpression TimeRange MoreSchedules
  // MoreSchedules -> schedule_separator Schedule
  // MoreSchedules -> ε
  // WeekdayExpression -> weekday WeekdayRange
  // WeekdayRange -> range_indicator weekday
  // WeekdayRange -> ε
  // TimeRange -> time range_indicator time

  public OpeningHours() {
    List<Rule> grammar = new ArrayList<>();
    grammar.add(new Rule(TokenType.OPENING_HOURS, asList(TokenType.SCHEDULE)));
    grammar.add(new Rule(TokenType.SCHEDULE, asList(TokenType.WEEKDAY_EXPRESSION, TokenType.TIME_RANGE, TokenType.MORE_SCHEDULES)));
    grammar.add(new Rule(TokenType.MORE_SCHEDULES, asList(TokenType.SCHEDULE_SEPARATOR, TokenType.SCHEDULE)));
    grammar.add(new Rule(TokenType.MORE_SCHEDULES, asList(TokenType.EMPTY)));
    grammar.add(new Rule(TokenType.WEEKDAY_EXPRESSION, asList(TokenType.WEEKDAY, TokenType.WEEKDAY_RANGE)));
    grammar.add(new Rule(TokenType.WEEKDAY_RANGE, asList(TokenType.RANGE_INDICATOR, TokenType.WEEKDAY)));
    grammar.add(new Rule(TokenType.WEEKDAY_RANGE, asList(TokenType.EMPTY)));
    grammar.add(new Rule(TokenType.TIME_RANGE, asList(TokenType.TIME, TokenType.RANGE_INDICATOR, TokenType.TIME)));
    parsingTable = new ParsingTable(grammar);
  }

  public List<Token> tokenize(final String input) {
    System.out.println("Input: '" + input + "'");
    final List<Token> tokens = new ArrayList<>();
    int startingPosition = 0;
    for (int i = 1; i <= input.length(); i++) {
      final String candidate = input.substring(startingPosition, i);
      if (Token.match(candidate).isPresent()) {
        tokens.add(Token.match(candidate).get());
        startingPosition = i;
      }
    }
    tokens.removeIf(token -> token.getType().equals(TokenType.WHITE_SPACE));
    return tokens;
  }

  public ValidationResult validate(final List<Token> tokens) {
    final List<TokenType> input = tokens.stream().map(Token::getType).collect(Collectors.toList());
    if (!input.get(input.size() - 1).isEndOfInput()) {
      input.add(TokenType.END_OF_INPUT);
    }
    final Stack<TokenType> stack = new Stack<>();
    stack.push(TokenType.END_OF_INPUT);
    stack.push(TokenType.getStartSymbol());
    do {
      final TokenType top = TokenType.class.cast(stack.peek());
      final TokenType nextInput = input.get(0);
      if (Objects.equals(top, nextInput)) {
        if (Objects.equals(top, TokenType.END_OF_INPUT)) {
          return new ValidationResult(true);
        }
        stack.pop();
        input.remove(0);
        continue;
      } else if (!top.isTerminal()) {
        stack.pop();
        final Rule parsingTableValue = parsingTable.getValue(top, nextInput);
        if (parsingTableValue == null) {
          return new ValidationResult(false);
        }
        final List<TokenType> parsingTableTokens = parsingTableValue.getRight();
        if (singletonList(TokenType.EMPTY).equals(parsingTableTokens)) {
          continue;
        }
        for (int i = parsingTableTokens.size(); i > 0; i--) {
          stack.push(parsingTableTokens.get(i - 1));
        }
      } else {
        return new ValidationResult(false);
      }
    } while (true);
  }


}

