package com.bimbok.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  // This map tells the scanner which words are special
  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("say", TokenType.SAY);
    keywords.put("out", TokenType.OUT);
  }

  public Scanner(String source) {
    this.source = source;
  }

  public List<Token> scanTokens() {
    while (!isAtEnd()) {
      start = current;
      scanToken();
    }
    tokens.add(new Token(TokenType.EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(':
        addToken(TokenType.LEFT_PAREN);
        break; // optional if you want parens
      case ')':
        addToken(TokenType.RIGHT_PAREN);
        break;
      case '+':
        addToken(TokenType.PLUS);
        break;
      case '-':
        addToken(TokenType.MINUS);
        break;
      case '*':
        addToken(TokenType.STAR);
        break;
      case '=':
        addToken(TokenType.EQUALS);
        break;

      // Ignore whitespace
      case ' ':
      case '\r':
      case '\t':
        break;

      case '\n':
        line++;
        break;

      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          System.err.println("Unexpected character: " + c);
        }
        break;
    }
  }

  // Helper: Reads a full number (e.g., "123")
  private void number() {
    // 1. Consume digits
    while (isDigit(peek()))
      advance();

    // 2. (Optional) Support decimals like 12.5
    if (peek() == '.' && isDigit(peekNext())) {
      advance(); // Consume the "."
      while (isDigit(peek()))
        advance();
    }

    // 3. CHANGE THIS LINE: Parse as Double, not Integer
    addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
  }

  private char peekNext() {
    if (current + 1 >= source.length())
      return '\0';
    return source.charAt(current + 1);
  }

  // Helper: Reads a word. Is it "say" or just a variable name like "score"?
  private void identifier() {
    while (isAlphaNumeric(peek()))
      advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null)
      type = TokenType.IDENTIFIER;

    addToken(type);
  }

  // --- Low level helper methods ---
  private boolean isAtEnd() {
    return current >= source.length();
  }

  private char advance() {
    return source.charAt(current++);
  }

  private char peek() {
    return isAtEnd() ? '\0' : source.charAt(current);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }
}
