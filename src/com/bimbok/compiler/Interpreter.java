package com.bimbok.compiler;

import java.util.List;

import javax.management.RuntimeErrorException;

// Notice: It now implements BOTH Visitor interfaces
class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

  private Environment environment = new Environment();

  // The new entry point for a list of statements
  void interpret(List<Stmt> statements) {
    try {
      for (Stmt statement : statements) {
        execute(statement);
      }
    } catch (RuntimeErrorException error) {
      System.err.println(error.getMessage());
    }
  }

  private void execute(Stmt stmt) {
    stmt.accept(this);
  }

  // --- Statement Visitors (COMMANDS) ---

  @Override
  public Void visitPrintStmt(Stmt.Print stmt) {
    Object value = evaluate(stmt.expression);
    System.out.println(stringify(value));
    return null;
  }

  @Override
  public Void visitVarStmt(Stmt.Var stmt) {
    Object value = null;
    if (stmt.initializer != null) {
      value = evaluate(stmt.initializer);
    }

    environment.define(stmt.name.lexeme, value);
    return null;
  }

  @Override
  public Void visitExpressionStmt(Stmt.Expression stmt) {
    evaluate(stmt.expression);
    return null;
  }

  // --- Expression Visitors (MATH) ---

  @Override
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.value;
  }

  @Override
  public Object visitGroupingExpr(Expr.Grouping expr) {
    return evaluate(expr.expression);
  }

  @Override
  public Object visitUnaryExpr(Expr.Unary expr) {
    Object right = evaluate(expr.right);
    if (expr.operator.type == TokenType.MINUS) {
      return -(double) right;
    }
    return null;
  }

  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
      case MINUS:
        return (double) left - (double) right;
      case PLUS:
        return (double) left + (double) right;
      case STAR:
        return (double) left * (double) right;
    }
    return null;
  }

  // --- Variable Access (NEW!) ---

  // We need to add a Variable expression to Expr.java next!
  // But for now, we can leave this part out until we update Expr.java.
  // Wait, we DO need to read variables.
  // We missed adding "Variable" to Expr.java in the previous step.
  // Let's add it in the next response to keep this clean.

  private Object evaluate(Expr expr) {
    return expr.accept(this);
  }

  private String stringify(Object object) {
    if (object == null)
      return "nil";
    if (object instanceof Double) {
      String text = object.toString();
      if (text.endsWith(".0"))
        text = text.substring(0, text.length() - 2);
      return text;
    }
    return object.toString();
  }

  @Override
  public Object visitVariableExpr(Expr.Variable expr) {
    return environment.get(expr.name);
  }
}
