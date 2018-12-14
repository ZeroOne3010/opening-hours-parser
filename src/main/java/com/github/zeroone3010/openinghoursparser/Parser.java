package com.github.zeroone3010.openinghoursparser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * A top-down LL(1) parser for an opening times grammar.
 */
final class Parser {
  private final Map<TokenType, Map<TokenType, Rule>> parsingTable;
  private final Map<TokenType, Set<TokenType>> firsts;
  private final Map<TokenType, Set<TokenType>> follows;
  private final Map<Rule, Set<TokenType>> ruleFirsts;

  public Parser(final List<Rule> grammar) {
    this.firsts = computeTokenFirsts(grammar);
    this.ruleFirsts = computeTokenStringFirsts(grammar);
    this.follows = computeFollows(grammar);
    this.parsingTable = computeParsingTable(grammar);
  }

  public ValidationResult validate(final List<Token> tokens) {
    final List<TokenType> input = tokens.stream().map(Token::getType).collect(Collectors.toList());
    if (input.isEmpty()) {
      return new ValidationResult(true);
    }
    final Optional<Token> unknownTokem = tokens.stream().filter(t -> t.getType().isUnknown()).findFirst();

    if (unknownTokem.isPresent()) {
      return new ValidationResult(false, "Unknown token '" + unknownTokem.get().getValue() + "'");
    }

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
        final Rule parsingTableValue = getValue(top, nextInput);
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


  private Rule getValue(final TokenType nonTerminal, final TokenType terminal) {
    if (nonTerminal.isTerminal()) {
      throw new IllegalArgumentException(nonTerminal + " is not a nonterminal token");
    }
    if (!terminal.isTerminal()) {
      throw new IllegalArgumentException(terminal + " is not a terminal token");
    }
    return parsingTable.get(nonTerminal).get(terminal);
  }

  private Map<TokenType, Map<TokenType, Rule>> computeParsingTable(final List<Rule> grammar) {
    final Map<TokenType, Map<TokenType, Rule>> nonterminalToTerminalToRule = new HashMap<>();
    for (final Rule rule : grammar) {
      final Set<TokenType> firsts = this.ruleFirsts.get(rule);
      final Set<TokenType> firstsWithoutEmpty = new HashSet<>(firsts);
      final boolean hasEmpty = firstsWithoutEmpty.remove(TokenType.EMPTY);
      for (final TokenType first : firstsWithoutEmpty) {
        final Map<TokenType, Rule> terminals = nonterminalToTerminalToRule.getOrDefault(rule.getLeft(), new HashMap<>());
        final Rule putResult = terminals.put(first, rule);
        if (putResult != null) {
          throw new IllegalStateException("The given grammar is not LL(1).");
        }
        nonterminalToTerminalToRule.put(rule.getLeft(), terminals);
      }
      if (hasEmpty) {
        for (final TokenType follow : follows.get(rule.getLeft())) {
          final Map<TokenType, Rule> terminals = nonterminalToTerminalToRule.getOrDefault(rule.getLeft(), new HashMap<>());
          terminals.put(follow, rule);
          nonterminalToTerminalToRule.put(rule.getLeft(), terminals);
        }
      }
    }
    return nonterminalToTerminalToRule;
  }

  private Map<TokenType, Set<TokenType>> computeTokenFirsts(final List<Rule> grammar) {
    final Predicate<TokenType> isTerminal = TokenType::isTerminal;
    final Predicate<TokenType> isNonTerminal = isTerminal.negate();
    final List<TokenType> terminals = Stream.of(TokenType.values()).filter(isTerminal).collect(toList());
    final List<TokenType> nonTerminals = Stream.of(TokenType.values()).filter(isNonTerminal).collect(toList());

    final Map<TokenType, Set<TokenType>> firsts = new HashMap<>();

    // Initialize terminals as their own firsts:
    terminals.forEach(terminal -> firsts.put(terminal, singleton(terminal)));

    // Initialize nonterminals to have empty first sets:
    nonTerminals.forEach(nonTerminal -> firsts.put(nonTerminal, new HashSet<>()));

    boolean changed;
    do {
      changed = false;
      for (final Rule rule : grammar) {
        final TokenType left = rule.getLeft();
        final Set<TokenType> currentFirsts = firsts.get(left);
        boolean hasEmptyPrefix = true;
        final List<TokenType> right = rule.getRight();
        for (int i = 0; i < right.size(); i++) {
          final Set<TokenType> productionFirsts = new HashSet<>(firsts.get(right.get(i)));
          hasEmptyPrefix = productionFirsts.contains(TokenType.EMPTY);
          productionFirsts.remove(TokenType.EMPTY);
          changed |= currentFirsts.addAll(productionFirsts);
          if (!hasEmptyPrefix) {
            break;
          }
        }
        if (hasEmptyPrefix) {
          changed |= currentFirsts.add(TokenType.EMPTY);
        }
      }
    } while (changed);

    return firsts;
  }

  private Map<Rule, Set<TokenType>> computeTokenStringFirsts(final List<Rule> grammar) {
    final Map<Rule, Set<TokenType>> results = new HashMap<>();
    for (final Rule rule : grammar) {
      results.put(rule, computeFirsts(rule.getRight()));
    }
    return results;
  }

  private Set<TokenType> computeFirsts(final List<TokenType> tokens) {
    final Set<TokenType> first = new HashSet<>();
    boolean hasEmptyPrefix = true;
    for (int i = 0; i < tokens.size(); i++) {
      final Set<TokenType> productionFirsts = new HashSet<>(firsts.get(tokens.get(i)));
      hasEmptyPrefix = productionFirsts.contains(TokenType.EMPTY);
      productionFirsts.remove(TokenType.EMPTY);
      first.addAll(productionFirsts);
      if (!hasEmptyPrefix) {
        break;
      }
    }
    if (hasEmptyPrefix) {
      first.add(TokenType.EMPTY);
    }
    return first;
  }

  private Map<TokenType, Set<TokenType>> computeFollows(final List<Rule> grammar) {
    final Predicate<TokenType> isTerminal = TokenType::isTerminal;
    final Predicate<TokenType> isNonTerminal = isTerminal.negate();
    final List<TokenType> nonTerminals = Stream.of(TokenType.values()).filter(isNonTerminal).collect(toList());

    final Map<TokenType, Set<TokenType>> follows = new HashMap<>();

    // Initialize nonterminals to have empty follow sets:
    nonTerminals.forEach(nonTerminal -> follows.put(nonTerminal, new HashSet<>()));

    follows.put(TokenType.getStartSymbol(), singleton(TokenType.END_OF_INPUT));

    boolean changed;
    do {
      changed = false;
      for (final Rule rule : grammar) {
        final TokenType left = rule.getLeft();
        final List<TokenType> right = rule.getRight();
        for (int i = 0; i < right.size(); i++) {
          final TokenType currentToken = right.get(i);
          if (!currentToken.isTerminal()) {
            final List<TokenType> followingTokens = right.subList(i + 1, right.size());
            final Set<TokenType> follow = computeFirsts(followingTokens); // x_i+1 -> !!!!!
            final boolean containsEmpty = follow.contains(TokenType.EMPTY);
            follow.remove(TokenType.EMPTY);
            changed = follows.get(currentToken).addAll(follow);
            if (containsEmpty) {
              changed |= follows.get(currentToken).addAll(follows.get(left));
            }
          }
        }
      }
    } while (changed);

    return follows;
  }

  @Override
  public String toString() {
    return "Parser{" +
        "parsingTable=" + parsingTable +
        '}';
  }
}
