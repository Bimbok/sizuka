package com.bimbok.compiler;

import java.util.ArrayList;
import java.util.List;
import static com.bimbok.compiler.TokenType.*;

class Parser {
  private final List<Token> tokens;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  // NEW: We now return a LIST of commands, not just one math problem
  List<Stmt> parse() {
    List<Stmt> statements = new ArrayList<>();
    while (!isAtEnd()) {
      Stmt statement = declaration();
      if (statement != null) { // Only add it if it's real!
        statements.add(statement);
      }
    }
    return statements;
  }

  // --- Statement Layers ---

  // 1. Declaration: "say a = 10" or just a statement
  private Stmt declaration() {
    try {
      if (match(SAY))
        return varDeclaration();
      return statement();
    } catch (ParseError error) {
      synchronize(); // Panic mode recovery
      return null;
    }
  }

  // Handle: say name = value
  private Stmt varDeclaration() {
    Token name = consume(IDENTIFIER, "Expect variable name.");

    Expr initializer = null;
    if (match(EQUALS)) {
      initializer = expression();
    }

    return new Stmt.Var(name, initializer);
  }

  // 2. Statement: "out ..." or "1+1"
  private Stmt statement() {
    if (match(OUT))
      return printStatement();
    return expressionStatement();
  }

  // Handle: out expression
  private Stmt printStatement() {
    Expr value = expression();
    return new Stmt.Print(value);
  }

  // Handle: 1 + 1 (just calculating)
  private Stmt expressionStatement() {
    Expr expr = expression();
    return new Stmt.Expression(expr);
  }

  // --- Expression Layers (Same as before) ---

  private Expr expression() {
    return term();
  }

  private Expr term() {
    Expr expr = factor();
    while (match(PLUS, MINUS)) {
      Token operator = previous();
      Expr right = factor();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr factor() {
    Expr expr = unary();
    while (match(STAR)) {
      Token operator = previous();
      Expr right = unary();
      expr = new Expr.Binary(expr, operator, right);
    }
    return expr;
  }

  private Expr unary() {
    if (match(MINUS)) {
      Token operator = previous();
      Expr right = unary();
      return new Expr.Unary(operator, right);
    }
    return primary();
  }

  private Expr primary() {
    if (match(NUMBER))
      return new Expr.Literal(previous().literal);

    // NEW: We can now read variables like "a" or "score"
    if (match(IDENTIFIER))
      return new Expr.Variable(previous());

    if (match(LEFT_PAREN)) {
      Expr expr = expression();
      consume(RIGHT_PAREN, "Expect ')' after expression.");
      return new Expr.Grouping(expr);
    }

    throw error(peek(), "Expect expression.");
  }

  // --- Helpers ---

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }

  private Token consume(TokenType type, String message) {
    if (check(type))
      return advance();
    throw error(peek(), message);
  }

  private boolean check(TokenType type) {
    if (isAtEnd())
      return false;
    return peek().type == type;
  }

  private Token advance() {
    if (!isAtEnd())
      current++;
    return previous();
  }

  private boolean isAtEnd() {
    return peek().type == EOF;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private ParseError error(Token token, String message) {
    System.err.println("[Line " + token.line + "] Error: " + message);
    return new ParseError();
  }

  private void synchronize() {
    advance();
    while (!isAtEnd()) {
      if (previous().type == EOF)
        return; // Simple recovery
      switch (peek().type) {
        case SAY:
        case OUT:
          return;
      }
      advance();
    }
  }

  private static class ParseError extends RuntimeException {
  }
}
