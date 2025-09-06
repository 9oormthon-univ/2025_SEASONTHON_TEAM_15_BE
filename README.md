# 2025 SEASONTHON TEAM 15 - 가족 다짐 공유 플랫폼

## 📋 프로젝트 개요

가족 구성원들이 함께 신년 다짐을 공유하고 서로 응원할 수 있는 웹 플랫폼입니다. 가족 세션을 통해 메모를 작성하고, 가족 구성원들의 다짐을 확인하며, 서로를 격려할 수 있습니다.

## 주요 기능

### 인증 시스템

-   **회원가입/로그인**: 사용자명, 전화번호, 비밀번호 기반 인증
-   **JWT 토큰**: 보안 인증 및 세션 관리
-   **가족 가입**: 회원가입과 동시에 가족 세션 참여 가능

### 가족 세션 관리

-   **세션 생성**: 가족별 독립적인 세션 생성
-   **초대 시스템**: 초대 코드를 통한 가족 구성원 초대
-   **가족 관계 설정**: 아빠, 엄마, 아들, 딸 등 가족 관계 정의
-   **세션 보안**: 선택적 세션 비밀번호 설정

### 메모 시스템

-   **다짐 메모 작성**: 가족 구성원을 위한 다짐 메모 작성
-   **익명 메모**: 익명으로 메모 작성 가능
-   **메모지 색상**: 다양한 색상의 메모지 선택
-   **메모 관리**: 메모 수정, 삭제, 조회 기능

### 개인화 기능

-   **세대별 구분**: 10대, 20대, 30대, 40대+ 세대별 분류
-   **메모지 템플릿**: 다양한 메모지 디자인 선택
-   **가족 별명**: 가족만의 특별한 별명 설정

## 기술 스택

### Backend

-   Java
-   Spring Boot 3.5.5
-   mysql

### API Documentation

-   Swagger/OpenAPI 3: API 문서화
-   SpringDoc OpenAPI: 자동 API 문서 생성

## 📁 프로젝트 구조

```
src/main/java/com/goormthon/team15/
├── auth/                    # 인증 관련
│   ├── controller/         # AuthController
│   ├── dto/               # 인증 DTO
│   └── service/           # AuthService
├── user/                   # 사용자 관리
│   ├── controller/        # UserController
│   ├── dto/              # 사용자 DTO
│   ├── entity/           # User 엔티티
│   ├── repository/       # UserRepository
│   └── service/          # UserService
├── family/                 # 가족 세션 관리
│   ├── controller/       # FamilySessionController, InviteController
│   ├── dto/             # 가족 세션 DTO
│   ├── entity/          # FamilySession, SessionMember 엔티티
│   ├── repository/      # 가족 세션 Repository
│   └── service/         # FamilySessionService
├── memo/                   # 메모 관리
│   ├── controller/      # MemoController
│   ├── dto/            # 메모 DTO
│   ├── entity/         # Memo 엔티티
│   ├── repository/     # MemoRepository
│   └── service/        # MemoService
└── common/                 # 공통 기능
    ├── config/         # SecurityConfig, SwaggerConfig
    ├── exception/      # 예외 처리
    ├── security/       # JWT 보안 설정
    └── util/          # 유틸리티 클래스
```

## 데이터베이스 스키마

### 주요 테이블

-   users: 사용자 정보 (사용자명, 전화번호, 세대, 역할)
-   family_sessions: 가족 세션 정보 (세션명, 초대코드, 가족별명)
-   session_members: 세션 참여자 정보 (가족 관계, 역할, 상태)
-   memos: 메모 정보 (제목, 내용, 색상, 익명 여부)

## API 엔드포인트

### 인증 API (`/v1/api/auth`)

-   `POST /register` - 회원가입
-   `POST /login` - 로그인
-   `POST /logout` - 로그아웃
-   `POST /register-with-family` - 회원가입 + 가족 가입

### 사용자 API (`/v1/api/users`)

-   `GET /profile` - 현재 사용자 프로필 조회
-   `PUT /profile` - 사용자 정보 수정
-   `GET /generations` - 세대별 정보 조회
-   `GET /memo-colors` - 메모지 색상 목록
-   `GET /family-relationships` - 가족 관계 목록

### 가족 세션 API (`/v1/api/family-sessions`)

-   `GET /my-session` - 내 가족 세션 조회
-   `POST /create` - 가족 세션 생성
-   `POST /join` - 가족 세션 참여
-   `POST /{sessionId}/leave` - 가족 세션 탈퇴
-   `DELETE /{sessionId}` - 가족 세션 삭제
-   `GET /my-session/members` - 가족 구성원 목록
-   `PUT /members/{memberId}/relationship` - 가족 관계 설정
-   `GET /my-session/dday` - D-Day 계산
-   `GET /my-session/resolutions` - 가족별 신년 다짐 조회

### 메모 API (`/v1/api/memos`)

-   `POST /create` - 메모 생성
-   `GET /list` - 메모 목록 조회 (페이징)
-   `GET /all` - 모든 메모 조회
-   `GET /family-session/{familySessionId}` - 가족 세션별 메모 조회
-   `GET /{memoId}` - 메모 상세 조회
-   `PUT /{memoId}` - 메모 수정
-   `DELETE /{memoId}` - 메모 삭제
-   `GET /count` - 메모 수 조회
