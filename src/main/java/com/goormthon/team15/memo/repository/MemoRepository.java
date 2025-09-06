package com.goormthon.team15.memo.repository;

import com.goormthon.team15.memo.entity.Memo;
import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * 제목으로 메모 검색
     */
    @Query("SELECT m FROM Memo m WHERE m.user = :user AND m.status = :status AND m.title LIKE %:keyword% ORDER BY m.createdAt DESC")
    List<Memo> findByUserAndStatusAndTitleContaining(@Param("user") User user, @Param("status") Memo.MemoStatus status, @Param("keyword") String keyword);
    
    /**
     * 내용으로 메모 검색
     */
    @Query("SELECT m FROM Memo m WHERE m.user = :user AND m.status = :status AND m.content LIKE %:keyword% ORDER BY m.createdAt DESC")
    List<Memo> findByUserAndStatusAndContentContaining(@Param("user") User user, @Param("status") Memo.MemoStatus status, @Param("keyword") String keyword);
    
    /**
     * 제목 또는 내용으로 메모 검색
     */
    @Query("SELECT m FROM Memo m WHERE m.user = :user AND m.status = :status AND (m.title LIKE %:keyword% OR m.content LIKE %:keyword%) ORDER BY m.createdAt DESC")
    List<Memo> findByUserAndStatusAndTitleOrContentContaining(@Param("user") User user, @Param("status") Memo.MemoStatus status, @Param("keyword") String keyword);
    
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
