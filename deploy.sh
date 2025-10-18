#!/bin/bash

echo "🚀 Deploying Winmart Backend..."

# Pull latest code
echo "📥 Pulling latest code from git..."
git pull

# Stop old containers
echo "🛑 Stopping old containers..."
docker compose down

# Build and start
echo "🔨 Building and starting containers..."
docker compose up -d --build

# Show logs
echo ""
echo "✅ Deploy complete!"
echo "📊 Logs:"
docker compose logs --tail=20

echo ""
echo "🌐 Application: http://localhost:3333"
echo "💡 View logs: docker compose logs -f"

