FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Gradle 캐시를 위해 먼저 설정 파일만 복사
COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew
RUN ./gradlew dependencies || true

# 실제 소스 복사 후 빌드
COPY src src
RUN ./gradlew clean bootJar -x test

# 실행 이미지
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 로그 디렉토리 등 필요하면 생성
RUN mkdir -p /app/logs

COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
