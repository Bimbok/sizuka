# Sizuka Language

> A clean, interpreted programming language built from scratch in Java.

**Sizuka** is a dynamically typed programming language that runs on the Java Virtual Machine (JVM). It features a custom recursive descent parser and a tree-walk interpreter, designed to learn the core concepts of compiler design.

## ⚡ Features

- **Custom Syntax:** Clean, command-based keywords (`say`, `out`).
- **Arithmetic Engine:** Full support for mathematical expressions (`+`, `-`, `*`) with correct operator precedence.
- **Variables:** Dynamic variable declaration and resolution using environments.
- **REPL:** Interactive shell (Read-Eval-Print Loop) for instant code execution.
- **Error Handling:** graceful syntax error reporting without crashing the runtime.

## 🛠️ Installation & Usage

You can build Sizuka using any standard Java JDK (17 or newer recommended).

### 1. Build the Compiler

Navigate to the project root and compile the source code:

```bash
mkdir -p bin
javac -d bin src/com/bimbok/compiler/*.java

```

### 2. Run the REPL

Start the interactive shell:

```bash
java -cp bin com.bimbok.compiler.Main

```

## 📝 Syntax Guide

### Variables

Use the `say` keyword to declare variables. Sizuka uses dynamic typing.

```text
say a = 10
say b = 5

```

### Output

Use the `out` keyword to print values to the console.

```text
out a
// Output: 10

```

### Math

Sizuka supports complex mathematical expressions, respecting parentheses `()` and standard order of operations.

```text
say x = 10
say y = 20
out (x + y) * 2
// Output: 60

```

## 🏗️ Architecture

Sizuka is built as a three-stage pipeline:

1. **Scanner (Lexer):** \* Reads raw source code character-by-character.

- Groups them into `Tokens` (e.g., `VAR`, `NUMBER`, `PLUS`).
- Handles keyword recognition and literal parsing.

2. **Parser:**

- Implements a **Recursive Descent Parser**.
- Converts the flat list of tokens into an **Abstract Syntax Tree (AST)**.
- Defines the language grammar (Expression -> Term -> Factor -> Primary).

3. **Interpreter:**

- Uses the **Visitor Pattern** to traverse the AST.
- Executes nodes recursively.
- Manages memory via an `Environment` class (Java HashMap wrapper).

## 🗺️ Roadmap

Future updates planned for Sizuka:

- [ ] Control Flow (`if` / `else`)
- [ ] Boolean logic (`true`, `false`, `==`, `!=`)
- [ ] Logical operators (`and`, `or`)
- [ ] Code blocks and Scope `{ ... }`
- [ ] While loops

## 📄 License

This project is open source and available for educational purposes.

---

_Built with ❤️ by Bimbok_
