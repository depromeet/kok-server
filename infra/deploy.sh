#!/bin/bash

LATEST_TAG=$1

# .env 최신 태그 업데이트
sed -i '/^KOK_PROD_TAG=/d' .env || true
echo "KOK_PROD_TAG=$LATEST_TAG" >> .env

# 현재 사용 중인 Blue/Green 확인
CURRENT_PORT=$(grep -o 'localhost:[0-9]\+' /etc/nginx/conf.d/service-url.inc | awk -F: '{print $2}')

if [ "$CURRENT_PORT" == "8081" ]; then
  CURRENT_ENV="kok-blue"
  NEW_ENV="kok-green"
  NEW_PORT=8082
  COMPOSE_FILE="docker-compose-green.yml"
  NEW_SERVICE_URL_PATH="/etc/nginx/conf.d/service-url-green.inc"
elif [ "$CURRENT_PORT" == "8082" ]; then
  CURRENT_ENV="kok-green"
  NEW_ENV="kok-blue"
  NEW_PORT=8081
  COMPOSE_FILE="docker-compose-blue.yml"
  NEW_SERVICE_URL_PATH="/etc/nginx/conf.d/service-url-blue.inc"
else
  echo "❌ 현재 service-url.inc에 알 수 없는 포트값이 있습니다: $CURRENT_PORT"
  exit 1
fi

echo "현재 환경: $CURRENT_ENV → 새 환경: $NEW_ENV"

# 새 환경 배포
docker compose -f $COMPOSE_FILE pull
docker compose -f $COMPOSE_FILE up -d

echo "🩺 Health Check (60초 대기)"
sleep 60
HEALTH=$(curl -s http://localhost:$NEW_PORT/v1/api/health)
echo "Health Check 결과: $HEALTH"
CODE=$(echo "$HEALTH" | jq -r '.code')
DATA=$(echo "$HEALTH" | jq -r '.data')

if [[ "$CODE" != "200" || "$DATA" != "OK" ]]; then
  echo "❌ Health Check 실패 (code: $CODE, data: $DATA), 롤백!"
  docker compose -f $COMPOSE_FILE stop $NEW_ENV
  docker compose -f $COMPOSE_FILE rm -f $NEW_ENV
  exit 1
fi

echo "⚙️ service-url.inc 교체: $NEW_SERVICE_URL_PATH → /etc/nginx/conf.d/service-url.inc"
sudo cp $NEW_SERVICE_URL_PATH /etc/nginx/conf.d/service-url.inc

echo "🔄 Nginx 설정 reload"
sudo nginx -t && sudo systemctl reload nginx

echo "🧹 이전 환경($CURRENT_ENV) 정리"
docker compose -f docker-compose-${CURRENT_ENV#kok-}.yml stop $CURRENT_ENV
docker compose -f docker-compose-${CURRENT_ENV#kok-}.yml rm -f $CURRENT_ENV
