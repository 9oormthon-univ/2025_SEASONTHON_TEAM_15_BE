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
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    generation ENUM('TEENS', 'TWENTIES', 'THIRTIES', 'FORTIES_PLUS'),
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
*/
