@echo off
REM Wrapper for SmartCommit Docker CLI
REM Detect if current directory is a Git repository
git rev-parse --is-inside-work-tree >nul 2>&1
IF %ERRORLEVEL%==0 (
    docker run --rm -it -v "%cd%":/work -w /work --entrypoint bash smartcommit -c "java -jar /app/app.jar %*"
) ELSE (
    docker run --rm -it smartcommit %*
)