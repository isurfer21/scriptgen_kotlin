# scriptgen_kotlin

_ScriptGen_ is a command-line based script generator tool that simplifies the process of running scripts in any language.

## Introduction

When a script is executed using an interpreter, the command line statement can become quite long as it includes the interpreter path, script path, and all required arguments. _ScriptGen_ addresses this issue by consolidating the interpreter and script paths into a short command that can be easily accessed from anywhere in the system. This means that when calling the script via command, only the arguments need to be passed.

_ScriptGen_ generates commands for all operating systems. For Windows, it generates _batch_ and _PowerShell_ scripts. For _macOS_, it generates _Shell_ scripts and _PowerShell_ scripts which can be used if _PowerShell_ is installed. For Linux, it generates _Shell_ scripts. This makes it easy to run scripts on any platform without having to manually create the appropriate command.

### Working

This program uses a `parseOptions` function to extract the values of the `--interpreter` and `--script-path` options from the command line arguments. It then uses these values to create three files with the same name as the script but different extensions: `.cmd`, `.ps1`, and `.sh`. The content of these files is based on a template where the interpreter and script path values are replaced.

The program also supports the `--help` and `--version` options. When run with the `--help` option, it prints a help menu. When run with the `--version` option, it prints version information.

## Build

Here is the procedure for building the `scriptgen_kotlin` project on Windows, macOS, and Linux:

1. Open a command prompt (Windows) or terminal (macOS and Linux) and navigate to the directory where you want to clone the `scriptgen_kotlin` repository.

2. Clone the `scriptgen_kotlin` repository from GitHub by running the following command:
```
git clone https://github.com/isurfer21/scriptgen_kotlin.git
```

3. Navigate to the cloned repository by running the following command:
```
cd scriptgen_kotlin
```

### For Development

4. Build the project using `kotlinc`. Run the following command:
```
kotlinc .\ScriptGen.kt
```

5. After running these commands, the executable binary can be found in the current directory with a name `ScriptGenKt.class`. Run the command to see if it works.
```
kotlin ScriptGenKt -h
```

### For Kotlin Distribution

6. We then create a _JAR_ file named `ScriptGen.jar` by entering the following command:
```
kotlinc .\ScriptGen.kt -d ScriptGen.jar
```

7. After this, you can get the `ScriptGen.jar` file. Run the command to see if it works.
```
kotlin -classpath .\ScriptGen.jar ScriptGenKt -v
```

### For Java Distribution

8. We next input the following command to create a Java-compatible _JAR_ file called 'ScriptGen.jar' that includes the runtime:
```
kotlinc .\ScriptGen.kt -include-runtime -d ScriptGen.jar
```

9. You can then obtain the 'ScriptGen.jar' file. Run the command to see if it is working.
```
java -jar ScriptGen.jar -h
```

#### Important Note

| Distributed build for | Compatible & works with | Due to difference |
|-----------------------|-------------------------|-------------------|
| Kotlin                | Only Kotlin             |                   |
| Java                  | Both (Kotlin & Java)    | Includes runtime  |

## Usage

To use _ScriptGen_, run the `ScriptGen` command followed by any options, the interpreter, and the path to the script you want to run. Here is the basic usage syntax:

```
kotlin ScriptGen [options] [interpreter] [script-path]
```

### Options

_ScriptGen_ supports the following options:

- `-h` or `--help`: Show the help menu.
- `-v` or `--version`: Show version information.

### Examples

The following help menu shows the available options and arguments for using the tool:

```
> kotlin ScriptGenKt -h
Usage: kotlin ScriptGenKt [options]

Options:
  -i, --interpreter INTERPRETER   Specify the interpreter to use
  -s, --script-path SCRIPT_PATH   Specify the path to the script
  -h, --help                      Show this help message
  -v, --version                   Show version information

Examples:
  kotlin ScriptGenKt -h                            View help menu
  kotlin ScriptGenKt -v                            View version info
  kotlin ScriptGenKt -i node -s sample.js          Using Node as the interpreter
  kotlin ScriptGenKt -i python -s sample.py        Using Python as the interpreter
  kotlin ScriptGenKt -i 'java -jar' -s sample.jar  Using Java as the interpreter

Note: The --interpreter and --script options must be used together.
```

Here are some examples of how to use _ScriptGen_ with different interpreters:

- **Node.js:** To run a Node.js script, specify `node` as the interpreter and provide the path to the script. For example:
```
kotlin ScriptGenKt node /path/to/script.js
```

- **Python:** To run a Python script, specify `python` as the interpreter and provide the path to the script. For example:
```
kotlin ScriptGenKt python /path/to/script.py
```

- **Java:** To run a Java class file, specify `java` as the interpreter and provide the path to the class file (without the `.class` extension). For example:
```
kotlin ScriptGenKt 'java -jar' /path/to/script.jar
```

#### Installation Tips

Using the tool itself, you can easily create a short command for this tool. To do this, simply run the following command:
```
kotlin ScriptGenKt kotlin ScriptGenKt
```
or 
```
kotlin ScriptGenKt "java -jar" ScriptGen.jar
```
Give it a try and see for yourself!