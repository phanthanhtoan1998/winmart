@echo off
setlocal enabledelayedexpansion

echo ======================================
echo    Winmart Backend Deployment Script
echo ======================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker is not installed. Please install Docker Desktop first.
    pause
    exit /b 1
)

REM Check if Docker Compose is installed
docker compose version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker Compose is not installed. Please install Docker Desktop first.
    pause
    exit /b 1
)

echo Step 1: Stopping existing containers...
docker compose down

echo.
echo Step 2: Building Docker images...
docker compose build --no-cache

if errorlevel 1 (
    echo [ERROR] Build failed! Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Step 3: Starting containers...
docker compose up -d

if errorlevel 1 (
    echo [ERROR] Failed to start containers! Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Step 4: Waiting for services to be healthy...

REM Wait for database
echo Waiting for PostgreSQL...
set /a counter=0
:wait_db
set /a counter+=1
docker compose exec -T postgres-db pg_isready -U winmart -d winmart_db >nul 2>&1
if errorlevel 1 (
    if !counter! GEQ 30 (
        echo [ERROR] PostgreSQL failed to start after 30 seconds
        pause
        exit /b 1
    )
    echo|set /p="."
    timeout /t 1 /nobreak >nul
    goto wait_db
)
echo [OK] PostgreSQL is ready!

REM Wait for application
echo.
echo Waiting for Application...
set /a counter=0
:wait_app
set /a counter+=1
curl -s http://localhost:3333/actuator/health >nul 2>&1
if errorlevel 1 (
    if !counter! GEQ 60 (
        echo [ERROR] Application failed to start after 60 seconds
        echo Checking logs...
        docker compose logs --tail=50 winmart-app
        pause
        exit /b 1
    )
    echo|set /p="."
    timeout /t 1 /nobreak >nul
    goto wait_app
)
echo [OK] Application is ready!

echo.
echo ======================================
echo    Deployment Successful!
echo ======================================
echo.
echo Application is running on:
echo   - Local: http://localhost:3333
echo   - Health Check: http://localhost:3333/actuator/health
echo.
echo Useful commands:
echo   - View logs: docker compose logs -f
echo   - Stop: docker compose stop
echo   - Restart: docker compose restart
echo   - Remove: docker compose down
echo.
pause

