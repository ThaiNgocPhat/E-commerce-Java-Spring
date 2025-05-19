# Sử dụng image Java chính thức
FROM openjdk:17-jdk-slim

# Tạo thư mục ứng dụng
WORKDIR /app

# Copy file build từ Gradle (bạn cần build trước bằng ./gradlew build)
COPY build/libs/*.jar app.jar

# Expose port (ví dụ 8085)
EXPOSE 8085

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
