@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  server startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and SERVER_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\server-1.0.jar;%APP_HOME%\lib\netty-all-4.0.25.Final.jar;%APP_HOME%\lib\log4j-core-2.4.1.jar;%APP_HOME%\lib\log4j-api-2.4.1.jar;%APP_HOME%\lib\gson-2.4.jar;%APP_HOME%\lib\guava-19.0-rc2.jar;%APP_HOME%\lib\reflections-0.9.10.jar;%APP_HOME%\lib\xstream-1.4.8.jar;%APP_HOME%\lib\postgresql-9.4-1204-jdbc42.jar;%APP_HOME%\lib\commons-lang3-3.4.jar;%APP_HOME%\lib\jedis-2.7.3.jar;%APP_HOME%\lib\mockito-core-2.0.31-beta.jar;%APP_HOME%\lib\raven-6.0.0.jar;%APP_HOME%\lib\raven-log4j2-6.0.0.jar;%APP_HOME%\lib\config-1.3.0.jar;%APP_HOME%\lib\kotlin-compiler-1.0.0-beta-1038.jar;%APP_HOME%\lib\kotlin-runtime-1.0.0-beta-1038.jar;%APP_HOME%\lib\c3p0-0.9.5.1.jar;%APP_HOME%\lib\jbcrypt-0.3m.jar;%APP_HOME%\lib\spark-core-2.3.jar;%APP_HOME%\lib\dawnguard-2.2.jar;%APP_HOME%\lib\skript-2.14.jar;%APP_HOME%\lib\kotlin-stdlib-1.0.0-beta-1038.jar;%APP_HOME%\lib\groovy-all-2.4.6.jar;%APP_HOME%\lib\javassist-3.18.2-GA.jar;%APP_HOME%\lib\annotations-2.0.1.jar;%APP_HOME%\lib\xmlpull-1.1.3.1.jar;%APP_HOME%\lib\xpp3_min-1.1.4c.jar;%APP_HOME%\lib\commons-pool2-2.3.jar;%APP_HOME%\lib\byte-buddy-0.6.14.jar;%APP_HOME%\lib\objenesis-2.1.jar;%APP_HOME%\lib\jackson-core-2.5.0.jar;%APP_HOME%\lib\mchange-commons-java-0.2.10.jar;%APP_HOME%\lib\slf4j-simple-1.7.12.jar;%APP_HOME%\lib\jetty-server-9.3.2.v20150730.jar;%APP_HOME%\lib\jetty-webapp-9.3.2.v20150730.jar;%APP_HOME%\lib\websocket-server-9.3.2.v20150730.jar;%APP_HOME%\lib\websocket-servlet-9.3.2.v20150730.jar;%APP_HOME%\lib\javax.servlet-api-3.1.0.jar;%APP_HOME%\lib\jetty-http-9.3.2.v20150730.jar;%APP_HOME%\lib\jetty-io-9.3.2.v20150730.jar;%APP_HOME%\lib\jetty-xml-9.3.2.v20150730.jar;%APP_HOME%\lib\jetty-servlet-9.3.2.v20150730.jar;%APP_HOME%\lib\websocket-common-9.3.2.v20150730.jar;%APP_HOME%\lib\websocket-client-9.3.2.v20150730.jar;%APP_HOME%\lib\websocket-api-9.3.2.v20150730.jar;%APP_HOME%\lib\jetty-util-9.3.2.v20150730.jar;%APP_HOME%\lib\jetty-security-9.3.2.v20150730.jar;%APP_HOME%\lib\slf4j-api-1.7.12.jar

@rem Execute server
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %SERVER_OPTS%  -classpath "%CLASSPATH%" nl.bartpelle.veteres.GameServer %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable SERVER_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%SERVER_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
