# Team 15 Backend

JWT 기반 인증 시스템을 갖춘 Spring Boot 백엔드 애플리케이션입니다.

## 🚀 주요 기능

- JWT 기반 사용자 인증
- 회원가입/로그인 API
- Spring Security 통합
- Swagger UI API 문서화
- MySQL 데이터베이스 연동

## 📋 사전 요구사항

- Java 21
- MySQL 8.0+
- Gradle

## 🛠️ 설치 및 실행

### 1. MySQL 데이터베이스 설정

```bash
# MySQL에 접속
mysql -u root -p

# 데이터베이스 생성 스크립트 실행
source database_setup.sql
```

또는 수동으로:

```sql
CREATE DATABASE team15_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 데이터베이스 연결 설정

`src/main/resources/application.properties` 파일에서 데이터베이스 연결 정보를 수정하세요:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/team15_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 애플리케이션 실행

```bash
# 의존성 설치
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

## 📚 API 문서

애플리케이션 실행 후 다음 URL에서 Swagger UI에 접근할 수 있습니다:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **API 문서**: http://localhost:8080/v3/api-docs

## 🔐 API 엔드포인트

### 인증 관련

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| GET | `/api/auth/test` | 인증 테스트 |

### 사용 예시

#### 회원가입
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

#### 로그인
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### 인증이 필요한 API 호출
```bash
curl -X GET http://localhost:8080/api/auth/test \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🗄️ 데이터베이스 스키마

### users 테이블

| 컬럼명 | 타입 | 설명 |
|--------|------|------|
| id | BIGINT | 기본키 (자동증가) |
| username | VARCHAR(50) | 사용자명 (유니크) |
| email | VARCHAR(255) | 이메일 (유니크) |
| password | VARCHAR(255) | 암호화된 비밀번호 |
| role | ENUM | 사용자 역할 (USER, ADMIN) |
| created_at | TIMESTAMP | 생성일시 |
| updated_at | TIMESTAMP | 수정일시 |

## 🔧 설정

### JWT 설정

`application.properties`에서 JWT 관련 설정을 변경할 수 있습니다:

```properties
jwt.secret=your_secret_key
jwt.expiration=86400000  # 24시간 (밀리초)
```

### 데이터베이스 설정

```properties
spring.jpa.hibernate.ddl-auto=update  # 개발환경: update, 운영환경: validate
spring.jpa.show-sql=true  # SQL 쿼리 로그 출력
```

## 🧪 테스트

```bash
# 테스트 실행
./gradlew test

# 빌드 및 테스트
./gradlew build
```

## 📝 개발 가이드

### 새로운 API 추가

1. Controller 클래스 생성
2. Service 클래스 생성
3. DTO 클래스 생성 (필요시)
4. Swagger 어노테이션 추가

### 보안 설정

- JWT 토큰 기반 인증
- Spring Security 설정
- CORS 및 CSRF 설정

## 🐛 문제 해결

### MySQL 연결 오류

1. MySQL 서비스가 실행 중인지 확인
2. 데이터베이스가 생성되었는지 확인
3. 사용자 권한이 올바른지 확인
4. 방화벽 설정 확인

### JWT 토큰 오류

1. JWT 시크릿 키 확인
2. 토큰 만료 시간 확인
3. Authorization 헤더 형식 확인

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.
