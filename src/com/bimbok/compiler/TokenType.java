package com.bimbok.compiler;

public enum TokenType {
  // 1. Keywords
  SAY,
  OUT,

  // 2. Literals
  IDENTIFIER,
  NUMBER,

  // 3. Operators
  PLUS, // +
  MINUS, // -
  STAR, // *
  SLASH, // /
  EQUALS, // =

  // 4. Punctuation
  LEFT_PAREN, // (
  RIGHT_PAREN, // )

  // 5. Logic Operators
  BANG, BANG_EQUAL, // ! and !=
  EQUAL_EQUAL, // ==
  GREATER, GREATER_EQUAL, // > and >=
  LESS, LESS_EQUAL, // < and <=

  // 6. Data Types
  STRING,
  TRUE, FALSE,

  // 7. End of file
  EOF
}
