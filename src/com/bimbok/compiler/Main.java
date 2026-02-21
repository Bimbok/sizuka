package com.bimbok.compiler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    runPrompt();
  }

  // This creates an interactive shell (REPL)
  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    System.out.println(Colors.PURPLE_BOLD + "Sizuka v1.0" + Colors.RESET);

    StringBuilder buffer = new StringBuilder();

    for (;;) {
      if (buffer.length() == 0) {
        System.out.print(Colors.GREEN_BOLD + "> " + Colors.RESET);
      } else {
        System.out.print(Colors.GREEN_BOLD + "| " + Colors.RESET);
      }
      String line = reader.readLine();
      if (line == null)
        break;

      String command = line.trim();

      if (buffer.length() == 0 && command.equals("exit")) {
        System.out.println(Colors.PURPLE + "Bye bye! See you soon!  " + Colors.RESET);
        break;
      }

      if (buffer.length() == 0 && command.equals("clear")) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        continue;
      }

      buffer.append(line).append("\n");

      if (!isComplete(buffer.toString())) {
        continue;
      }

      run(buffer.toString());
      buffer.setLength(0);
    }
  }

  // Create one interpreter to reuse
  private static final Interpreter interpreter = new Interpreter();

  private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    Parser parser = new Parser(tokens);
    List<Stmt> statements = parser.parse(); // Changed to List<Stmt>

    // Stop if there was a syntax error
    if (statements == null || statements.isEmpty())
      return;

    // EXECUTE THE LIST!
    interpreter.interpret(statements);
  }

  private static boolean isComplete(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();
    int parenBalance = 0;
    int braceBalance = 0;
    Token last = null;

    for (Token token : tokens) {
      switch (token.type) {
        case LEFT_PAREN:
          parenBalance++;
          break;
        case RIGHT_PAREN:
          parenBalance--;
          break;
        case LEFT_BRACE:
          braceBalance++;
          break;
        case RIGHT_BRACE:
          braceBalance--;
          break;
        case EOF:
          break;
        default:
          last = token;
          break;
      }
    }

    if (parenBalance > 0 || braceBalance > 0) {
      return false;
    }

    if (last != null && last.type == TokenType.ELSE) {
      return false;
    }

    return true;
  }
}
