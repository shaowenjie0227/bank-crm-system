@echo off
set ERROR_CODE=0

if not "%JAVA_HOME%" == "" goto OkJHome
echo.
echo Error: JAVA_HOME environment variable is not set.
echo.
set ERROR_CODE=1
goto end

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init
echo.
echo Error: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
set ERROR_CODE=1
goto end

:init
setlocal
set "MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%"
if NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

set EXEC_DIR=%CD%
set WDIR=%EXEC_DIR%
:findBaseDir
IF EXIST "%WDIR%"\pom.xml goto baseDirFound
cd ..
IF "%WDIR%"=="%CD%" goto baseDirNotFound
set WDIR=%CD%
goto findBaseDir

:baseDirFound
set MAVEN_PROJECTBASEDIR=%WDIR%
cd "%EXEC_DIR%"
goto endDetectBaseDir

:baseDirNotFound
set MAVEN_PROJECTBASEDIR=%EXEC_DIR%
cd "%EXEC_DIR%"

:endDetectBaseDir

if NOT "%MAVEN_PROJECTBASEDIR%"=="" (
    cd "%MAVEN_PROJECTBASEDIR%"
)

set MAVEN_CMD_LINE_ARGS=%*

set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

if not exist "%WRAPPER_JAR%" (
    echo.
    echo Error: Could not find or load main class %WRAPPER_LAUNCHER%
    echo.
    set ERROR_CODE=1
    goto error
)

set MAVEN_OPTS=%MAVEN_OPTS% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%"

set JAVA_EXE="%JAVA_HOME%\bin\java.exe"

%JAVA_EXE% %MAVEN_OPTS% -classpath %WRAPPER_JAR% %WRAPPER_LAUNCHER% %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
endlocal & set ERROR_CODE=%ERROR_CODE%
exit /B %ERROR_CODE%