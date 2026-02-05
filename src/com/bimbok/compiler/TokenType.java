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
  STAR, // * <-- ADD THIS
  EQUALS, // =

  // 4. Punctuation
  LEFT_PAREN, // ( <-- ADD THIS
  RIGHT_PAREN, // ) <-- ADD THIS

  // 5. End of file
  EOF
}
