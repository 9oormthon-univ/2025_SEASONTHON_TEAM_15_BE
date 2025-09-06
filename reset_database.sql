-- 데이터베이스 완전 초기화 스크립트
-- 주의: 이 스크립트는 모든 데이터를 삭제합니다!

-- 1. 데이터베이스 삭제
DROP DATABASE IF EXISTS team15_db;

-- 2. 데이터베이스 재생성
CREATE DATABASE team15_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. 데이터베이스 사용
USE team15_db;

-- 4. 모든 테이블 삭제 (혹시 남아있을 수 있는 테이블들)
DROP TABLE IF EXISTS session_members;
DROP TABLE IF EXISTS family_sessions;
DROP TABLE IF EXISTS memos;
DROP TABLE IF EXISTS users;

-- 5. 초기 데이터베이스 설정 완료 메시지
SELECT 'Database reset completed successfully!' as status;
