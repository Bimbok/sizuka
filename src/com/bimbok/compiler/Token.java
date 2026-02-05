package com.bimbok.compiler;

public class Token {
  final TokenType type;
  final String lexeme; // The actual text (e.g., "say", "10", "+")
  final Object literal; // The value (e.g., the integer 10)
  final int line; // To help tell the user where errors are

  public Token(TokenType type, String lexeme, Object literal, int line) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.line = line;
  }

  public String toString() {
    return type + " " + lexeme + " " + literal;
  }
}
