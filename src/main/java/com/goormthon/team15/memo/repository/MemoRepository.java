package com.goormthon.team15.memo.repository;

import com.goormthon.team15.memo.entity.Memo;
import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    
    /**
     * 사용자의 활성 메모 목록 조회 (최신순)
     */
    List<Memo> findByUserAndStatusOrderByCreatedAtDesc(User user, Memo.MemoStatus status);
    
    /**
     * 사용자의 활성 메모 목록 조회 (페이징)
     */
    Page<Memo> findByUserAndStatusOrderByCreatedAtDesc(User user, Memo.MemoStatus status, Pageable pageable);
    
    /**
     * 사용자의 특정 메모 조회
     */
    Optional<Memo> findByIdAndUserAndStatus(Long id, User user, Memo.MemoStatus status);
    
    /**
     * 사용자의 메모 수 조회
     */
    long countByUserAndStatus(User user, Memo.MemoStatus status);
    
    
    /**
     * 사용자가 특정 메모의 소유자인지 확인
     */
    boolean existsByIdAndUserAndStatus(Long id, User user, Memo.MemoStatus status);
    
    /**
     * 가족 세션별 메모 목록 조회 (최신순)
     */
    List<Memo> findByFamilySessionAndStatusOrderByCreatedAtDesc(FamilySession familySession, Memo.MemoStatus status);
    
    /**
     * 가족 세션별 메모 목록 조회 (페이징)
     */
    Page<Memo> findByFamilySessionAndStatusOrderByCreatedAtDesc(FamilySession familySession, Memo.MemoStatus status, Pageable pageable);
    
    /**
     * 가족 세션의 메모 수 조회
     */
    long countByFamilySessionAndStatus(FamilySession familySession, Memo.MemoStatus status);
    
}
