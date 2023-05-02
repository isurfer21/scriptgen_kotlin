import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

const val VERSION = "1.0.0"
const val HELP = """Usage: kotlin ScriptGenKt [options]

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
"""

fun main(args: Array<String>) {
    val options = parseOptions(args)
    when {
        "help" in options -> println(HELP)
        "version" in options -> println(VERSION)
        "interpreter" in options && "script-path" in options -> {
            val interpreter = options["interpreter"]!!
            val scriptPath = options["script-path"]!!
            val scriptName = getScriptName(scriptPath)
            createFiles(interpreter, scriptPath, scriptName)
        }
        else -> {
            System.err.println("Error: Missing required options")
            System.err.println(HELP)
        }
    }
}

fun parseOptions(args: Array<String>): Map<String, String> {
    val options = mutableMapOf<String, String>()
    var i = 0
    while (i < args.size) {
        when (args[i]) {
            "-i", "--interpreter" -> options["interpreter"] = args[++i]
            "-s", "--script-path" -> options["script-path"] = args[++i]
            "-h", "--help" -> options["help"] = ""
            "-v", "--version" -> options["version"] = ""
            else -> {
                System.err.println("Error: Unknown option: ${args[i]}")
                System.err.println(HELP)
                System.exit(1)
            }
        }
        i++
    }
    return options
}

fun getScriptName(scriptPath: String): String {
    val index = scriptPath.lastIndexOfAny(charArrayOf('/', '\\'))
    return if (index == -1) scriptPath else scriptPath.substring(index + 1)
}

fun createFiles(interpreter: String, scriptPath: String, scriptName: String) {
    val cmdContent = """
        |@ECHO off
        |SETLOCAL
        |SET dp0=%~dp0
        |
        |SET "_prog=$interpreter"
        |SET PATHEXT=%PATHEXT:;.$interpreter;=;%
        |
        |ENDLOCAL & GOTO #_undefined_# 2>NUL || title %COMSPEC% & "%_prog%" "%dp0%\\$scriptPath" %*
        """.trimMargin()
    Files.write(Paths.get("$scriptName.cmd"), cmdContent.toByteArray())

    val ps1Content = """
        |#!/usr/bin/env pwsh
        |${'$'}basedir=Split-Path ${'$'}MyInvocation.MyCommand.Definition -Parent
        |
        |${'$'}exe=""
        |if (${'$'}PSVersionTable.PSVersion -lt "6.0" -or ${'$'}IsWindows) {
        |  # Fix case when both the Windows and Linux builds of Node
        |  # are installed in the same directory
        |  ${'$'}exe=".exe"
        |}
        |${'$'}ret=0
        |
        |# Support pipeline input
        |if (${'$'}MyInvocation.ExpectingInput) {
        |  ${'$'}input | & "$interpreter${'$'}exe"  "${'$'}basedir/$scriptPath" ${'$'}args
        |} else {
        |  & "$interpreter${'$'}exe"  "${'$'}basedir/$scriptPath" ${'$'}args
        |}
        |${'$'}ret=${'$'}LASTEXITCODE
        |
        |exit ${'$'}ret
        """.trimMargin()
    Files.write(Paths.get("$scriptName.ps1"), ps1Content.toByteArray())

    val shContent = """
        |#!/bin/sh
        |basedir=$(dirname "$(echo "${'$'}0" | sed -e 's,\\,/,g')")
        |
        |case `uname` in
        |    *CYGWIN*|*MINGW*|*MSYS*) basedir=`cygpath -w "${'$'}basedir"`;;
        |esac
        |
        |exec $interpreter  "${'$'}basedir/$scriptPath" "$@"
        """.trimMargin()
    Files.write(Paths.get("$scriptName.sh"), shContent.toByteArray())
}
