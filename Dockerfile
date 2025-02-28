# 1. Base Image 설정
FROM openjdk:21-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사 (GitHub Actions에서 빌드된 결과물 사용)
COPY kok-api/build/libs/*.jar app.jar

# 4. 기본 환경 변수 설정 (외부에서 변경 가능)
ENV SPRING_PROFILES_ACTIVE=dev

# 5. 컨테이너 실행 명령어
CMD ["java", "-jar", "app.jar"]
