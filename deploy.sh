#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}   Winmart Backend Deployment Script${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker is not installed. Please install Docker first.${NC}"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker compose &> /dev/null; then
    echo -e "${RED}Docker Compose is not installed. Please install Docker Compose first.${NC}"
    exit 1
fi

echo -e "${YELLOW}Step 1: Stopping existing containers...${NC}"
docker compose down

echo ""
echo -e "${YELLOW}Step 2: Building Docker images...${NC}"
docker compose build --no-cache

if [ $? -ne 0 ]; then
    echo -e "${RED}Build failed! Please check the error messages above.${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}Step 3: Starting containers...${NC}"
docker compose up -d

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to start containers! Please check the error messages above.${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}Step 4: Waiting for services to be healthy...${NC}"

# Wait for database
echo -e "${YELLOW}Waiting for PostgreSQL...${NC}"
for i in {1..30}; do
    if docker compose exec -T postgres-db pg_isready -U winmart -d winmart_db &> /dev/null; then
        echo -e "${GREEN}PostgreSQL is ready!${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}PostgreSQL failed to start after 30 seconds${NC}"
        exit 1
    fi
    echo -n "."
    sleep 1
done

# Wait for application
echo ""
echo -e "${YELLOW}Waiting for Application...${NC}"
for i in {1..60}; do
    if curl -s http://localhost:3333/actuator/health > /dev/null 2>&1; then
        echo -e "${GREEN}Application is ready!${NC}"
        break
    fi
    if [ $i -eq 60 ]; then
        echo -e "${RED}Application failed to start after 60 seconds${NC}"
        echo -e "${YELLOW}Checking logs...${NC}"
        docker compose logs --tail=50 winmart-app
        exit 1
    fi
    echo -n "."
    sleep 1
done

echo ""
echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}   Deployment Successful!${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""
echo -e "${GREEN}Application is running on:${NC}"
echo -e "  - Local: http://localhost:3333"
echo -e "  - Health Check: http://localhost:3333/actuator/health"
echo ""
echo -e "${YELLOW}Useful commands:${NC}"
echo -e "  - View logs: ${GREEN}docker compose logs -f${NC}"
echo -e "  - Stop: ${GREEN}docker compose stop${NC}"
echo -e "  - Restart: ${GREEN}docker compose restart${NC}"
echo -e "  - Remove: ${GREEN}docker compose down${NC}"
echo ""

