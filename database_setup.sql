-- MySQL 데이터베이스 설정 스크립트
-- 이 스크립트를 MySQL에서 실행하여 데이터베이스와 사용자를 생성하세요

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS team15_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 (선택사항 - 보안을 위해 별도 사용자 생성 권장)
-- CREATE USER 'team15_user'@'localhost' IDENTIFIED BY 'team15_password';
-- GRANT ALL PRIVILEGES ON team15_db.* TO 'team15_user'@'localhost';
-- FLUSH PRIVILEGES;

-- 데이터베이스 사용
USE team15_db;

-- 테이블은 JPA가 자동으로 생성합니다 (spring.jpa.hibernate.ddl-auto=update)
-- 하지만 수동으로 생성하려면 아래 주석을 해제하세요:

/*
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    generation ENUM('TEENS', 'TWENTIES', 'THIRTIES', 'FORTIES_PLUS'),
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
*/

-- 기존 테이블이 있다면 컬럼을 변경하는 마이그레이션 스크립트
-- ALTER TABLE users CHANGE COLUMN email phone_number VARCHAR(20) NOT NULL UNIQUE;

-- 가족 세션 테이블
CREATE TABLE IF NOT EXISTS family_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    invite_code VARCHAR(20) NOT NULL UNIQUE,
    session_password VARCHAR(20),
    creator_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'COMPLETED') NOT NULL DEFAULT 'ACTIVE',
    max_members INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 세션 멤버 테이블
CREATE TABLE IF NOT EXISTS session_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    family_session_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role ENUM('ADMIN', 'MEMBER') NOT NULL DEFAULT 'MEMBER',
    status ENUM('ACTIVE', 'INACTIVE', 'LEFT') NOT NULL DEFAULT 'ACTIVE',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_active_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (family_session_id) REFERENCES family_sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_session_user (family_session_id, user_id)
);
