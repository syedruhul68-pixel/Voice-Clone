@echo off
REM -----------------------------------------------------------------------------
REM Gradle startup script for Windows
REM -----------------------------------------------------------------------------

setlocal

set DEFAULT_JVM_OPTS=-Xmx64m -Xms64m

rem Determine the directory of this script
set DIRNAME=%~dp0
set PRG=%~dpnx0
set APP_BASE_NAME=%~n0

rem Setup JAVA command
if defined JAVA_HOME (
  set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVA_EXE=java"
)

"%JAVA_EXE%" %DEFAULT_JVM_OPTS% -jar "%DIRNAME%gradle\wrapper\gradle-wrapper.jar" %*
if errorlevel 1 goto :EOF
