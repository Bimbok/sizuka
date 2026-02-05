package com.bimbok.compiler;

import java.util.HashMap;
import java.util.Map;

class Environment {
  private final Map<String, Object> values = new HashMap<>();

  // "say a = 10" -> save it
  void define(String name, Object value) {
    values.put(name, value);
  }

  // "out a" -> look it up
  Object get(Token name) {
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    throw new RuntimeException("Undefined variable '" + name.lexeme + "'.");
  }
}
