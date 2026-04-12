package com.bimbok.compiler;

import java.util.List;

abstract class Stmt {
  abstract <R> R accept(Visitor<R> visitor);

  interface Visitor<R> {
    R visitExpressionStmt(Expression stmt);

    R visitPrintStmt(Print stmt);

    R visitVarStmt(Var stmt);

    R visitIfStmt(If stmt);

    R visitBlockStmt(Block stmt);
    R visitInputStmt(Input stmt);
    R visitFromStmt(From stmt);
  }

  // 1. "out 10;"
  static class Print extends Stmt {
    final Expr expression;

    Print(Expr expression) {
      this.expression = expression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitPrintStmt(this);
    }
  }

  // 2. "say a = 10;"
  static class Var extends Stmt {
    final Token name;
    final Expr initializer;

    Var(Token name, Expr initializer) {
      this.name = name;
      this.initializer = initializer;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitVarStmt(this);
    }
  }

  // 3. Just an expression like "1+1;" (valid code, even if it does nothing)
  static class Expression extends Stmt {
    final Expr expression;

    Expression(Expr expression) {
      this.expression = expression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitExpressionStmt(this);
    }
  }

  // 4. "if (condition) ... else ..."
  static class If extends Stmt {
    final Expr condition;
    final Stmt thenBranch;
    final Stmt elseBranch;

    If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
      this.condition = condition;
      this.thenBranch = thenBranch;
      this.elseBranch = elseBranch;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitIfStmt(this);
    }
  }

  // 5. "{ ... }"
  static class Block extends Stmt {
    final List<Stmt> statements;

    Block(List<Stmt> statements) {
      this.statements = statements;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBlockStmt(this);
    }
  }
  // 6. "in prompt? varName"
  static class Input extends Stmt {
    final Token name;
    final Expr prompt;

    Input(Token name, Expr prompt) {
      this.name = name;
      this.prompt = prompt;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitInputStmt(this);
    }
  }

  // 7. "from start to end as i { body }"
  static class From extends Stmt {
    final Expr start;
    final Expr end;
    final Token loopVar;
    final Stmt body;

    From(Expr start, Expr end, Token loopVar, Stmt body) {
      this.start = start;
      this.end = end;
      this.loopVar = loopVar;
      this.body = body;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitFromStmt(this);
    }
  }
}
