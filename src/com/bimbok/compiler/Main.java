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

    System.out.println("Sizuka v1.0");
    System.out.println("Type your code (Ctrl+C to quit):");

    for (;;) {
      System.out.print("> ");
      String line = reader.readLine();
      if (line == null)
        break;
      run(line);
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
}
