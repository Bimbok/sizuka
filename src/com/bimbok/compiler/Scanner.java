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
    keywords.put("if", TokenType.IF);
    keywords.put("else", TokenType.ELSE);
    keywords.put("and", TokenType.AND);
    keywords.put("or", TokenType.OR);
    keywords.put("true", TokenType.TRUE);
    keywords.put("false", TokenType.FALSE);
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
      // --- Single Character Tokens ---
      case '(':
        addToken(TokenType.LEFT_PAREN);
        break;
      case ')':
        addToken(TokenType.RIGHT_PAREN);
        break;
      case '{':
        addToken(TokenType.LEFT_BRACE);
        break;
      case '}':
        addToken(TokenType.RIGHT_BRACE);
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

      // --- Division & Comments ---
      case '/':
        if (match('/')) {
          // A comment goes until the end of the line.
          while (peek() != '\n' && !isAtEnd())
            advance();
        } else {
          addToken(TokenType.SLASH);
        }
        break;

      // --- Logic Operators (NEW: These replace your old '=' case) ---

      // Checks: Is it "!" or "!="?
      case '!':
        addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
        break;

      // Checks: Is it "=" or "=="?
      case '=':
        addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUALS);
        break;

      // Checks: Is it "<" or "<="?
      case '<':
        addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
        break;

      // Checks: Is it ">" or ">="?
      case '>':
        addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
        break;

      // --- Strings (NEW) ---
      case '"':
        string();
        break;

      // --- Whitespace & Newlines ---
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

  // Add this with other helper methods like number()

  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n')
        line++;
      advance();
    }

    if (isAtEnd()) {
      System.err.println("Unterminated string.");
      return;
    }

    advance(); // The closing "

    // Trim the surrounding quotes
    String value = source.substring(start + 1, current - 1);
    addToken(TokenType.STRING, value);
  }

  // Helper to check for two-character operators like '!='
  private boolean match(char expected) {
    if (isAtEnd())
      return false;
    if (source.charAt(current) != expected)
      return false;

    current++;
    return true;
  }
}
