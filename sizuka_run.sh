#!/bin/bash

# 1. Compile the latest code into the bin folder
javac -d bin src/com/bimbok/compiler/*.java

# 2. Check if the compilation was successful
if [ $? -eq 0 ]; then
  # 3. If it succeeded, run the interpreter and pass any arguments (like test files)
  java -cp bin com.bimbok.compiler.Main "$@"
else
  echo -e "\033[0;31mCompilation failed! Fix the Java errors above.\033[0m"
fi
