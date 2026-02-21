package com.bimbok.compiler;

import java.util.HashMap;
import java.util.Map;

class Environment {
  private final Map<String, Object> values = new HashMap<>();
  private final Environment enclosing;

  Environment() {
    this.enclosing = null;
  }

  Environment(Environment enclosing) {
    this.enclosing = enclosing;
  }

  // "say a = 10" -> save it
  void define(String name, Object value) {
    values.put(name, value);
  }

  // "out a" -> look it up
  Object get(Token name) {
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    if (enclosing != null) {
      return enclosing.get(name);
    }

    throw new RuntimeException("Undefined variable '" + name.lexeme + "'.");
  }

  void assign(Token name, Object value) {
    // If it exists, update it. If not, create it (implicit declaration).
    // Standard Java logic usually throws if not defined, but user asked for "oops I forgot say".
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
      return;
    }

    if (enclosing != null) {
      enclosing.assign(name, value);
      return;
    }

    values.put(name.lexeme, value);
  }
}
