# Sizuka Language

> A clean, interpreted programming language built from scratch in Java.

**Sizuka** is a dynamically typed programming language that runs on the Java Virtual Machine (JVM). It features a custom recursive descent parser and a tree-walk interpreter, designed to learn the core concepts of compiler design.

## ⚡ Features

- **Custom Syntax:** Clean, command-based keywords (`say`, `out`).
- **Arithmetic Engine:** Full support for mathematical expressions (`+`, `-`, `*`) with correct operator precedence.
- **Variables:** Dynamic variable declaration and resolution using environments.
- **Control Flow:** Full support for `if`/`else` branching and logical operators (`and`, `or`).
- **Bounded Loops:** Powerful `from` loop for iteration.
- **REPL:** Interactive shell (Read-Eval-Print Loop) for instant code execution.
- **Error Handling:** Graceful syntax error reporting without crashing the runtime.

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

### Input

Use the `in` keyword to read input from the user. You can optionally provide a prompt string.

```text
in "What is your name? " name
out "Hello, " + name

in age
out "Next year you will be: " + (age + 1)
```

**Features:**
- **Optional Prompt:** If a string is provided immediately after `in`, it is used as a prompt. Otherwise, a default `?` is shown.
- **Smart Parsing:** Sizuka automatically converts input to numbers if possible, enabling immediate mathematical operations.
- **Implicit Declaration:** If the variable name doesn't exist, it is created automatically in the current scope.

### Math

Sizuka supports complex mathematical expressions, respecting parentheses `()` and standard order of operations.

```text
say x = 10
say y = 20
out (x + y) * 2
// Output: 60
```

### Control Flow

Use `if` / `else` with blocks to branch:

```text
say x = 5
if (x > 3) {
  out "big"
} else {
  out "small"
}
```

You can combine conditions with `and` / `or`:

```text
if (x > 0 and x < 10) {
  out "single digit"
}
```

### Loops (`from` ... `to` ... `as`)

Sizuka features a bounded `from` loop for iteration. It allows you to specify a start value, an end value, and a loop variable name.

**Syntax:**
```text
from <start_expr> to <end_expr> as <variable_name> {
    // body statements
}
```

**Key Characteristics:**
- **Dynamic Bounds:** Both `start` and `end` can be any valid expression (e.g., `(i + 1) * 2`).
- **Scoping:** The loop variable is automatically managed in the environment on each iteration.
- **Nesting:** Supports infinite nesting of loops, `if` statements, and blocks.

**Example:**
```text
from 1 to 5 as i {
    out "Outer: " + i
    from (i + 1) to 10 as j {
        if (j > 7) {
            out "Inner: " + j
        }
    }
}
```

## 🏗️ Architecture

Sizuka is built as a three-stage pipeline:

1.  **Scanner (Lexer):**
    - Reads raw source code character-by-character.
    - Groups them into `Tokens` (e.g., `VAR`, `NUMBER`, `PLUS`).
    - Handles keyword recognition and literal parsing.

2.  **Parser:**
    - Implements a **Recursive Descent Parser**.
    - Converts the flat list of tokens into an **Abstract Syntax Tree (AST)**.
    - Defines the language grammar (Expression -> Term -> Factor -> Primary).

3.  **Interpreter:**
    - Uses the **Visitor Pattern** to traverse the AST.
    - Executes nodes recursively.
    - Manages memory via an `Environment` class (Java HashMap wrapper).

## 🗺️ Roadmap

Future updates planned for Sizuka:

- [ ] While loops
- [ ] Function definitions (`fun`)
- [ ] List/Array types
- [ ] Standard Library (Math, String utils)

## 📄 License

This project is open source and available for educational purposes.

---

_Built with ❤️ by Bimbok_
