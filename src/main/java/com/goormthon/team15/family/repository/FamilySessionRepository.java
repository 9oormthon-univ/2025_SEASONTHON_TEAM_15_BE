package com.goormthon.team15.family.repository;

import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilySessionRepository extends JpaRepository<FamilySession, Long> {
    
    /**
     * 초대 코드로 가족 세션 찾기
     */
    Optional<FamilySession> findByInviteCode(String inviteCode);
    
    /**
     * 초대 코드 중복 확인
     */
    boolean existsByInviteCode(String inviteCode);
    
    /**
     * 사용자가 생성한 가족 세션 목록
     */
    List<FamilySession> findByCreatorOrderByCreatedAtDesc(User creator);
    
    /**
     * 사용자가 참여한 가족 세션 목록 (멤버로 참여)
     */
    @Query("SELECT fs FROM FamilySession fs " +
           "JOIN fs.members sm " +
           "WHERE sm.user = :user AND sm.status = 'ACTIVE' " +
           "ORDER BY fs.createdAt DESC")
    List<FamilySession> findSessionsByMember(@Param("user") User user);
    
    /**
     * 사용자가 참여한 모든 가족 세션 (생성자 + 멤버)
     */
    @Query("SELECT DISTINCT fs FROM FamilySession fs " +
           "LEFT JOIN fs.members sm " +
           "WHERE fs.creator = :user OR (sm.user = :user AND sm.status = 'ACTIVE') " +
           "ORDER BY fs.createdAt DESC")
    List<FamilySession> findSessionsByUser(@Param("user") User user);
    
    /**
     * 활성 상태인 가족 세션만 조회
     */
    List<FamilySession> findByStatusOrderByCreatedAtDesc(FamilySession.SessionStatus status);
    
    /**
     * 사용자가 특정 세션의 멤버인지 확인
     */
    @Query("SELECT COUNT(sm) > 0 FROM SessionMember sm " +
           "WHERE sm.familySession.id = :sessionId AND sm.user = :user AND sm.status = 'ACTIVE'")
    boolean isUserMemberOfSession(@Param("sessionId") Long sessionId, @Param("user") User user);
    
    /**
     * 사용자가 특정 세션의 생성자인지 확인
     */
    boolean existsByIdAndCreator(Long sessionId, User creator);
    
    /**
     * 사용자가 활성 상태인 가족 세션에 참여하고 있는지 확인
     */
    @Query("SELECT COUNT(fs) > 0 FROM FamilySession fs " +
           "WHERE (fs.creator = :user OR EXISTS (SELECT 1 FROM SessionMember sm WHERE sm.familySession = fs AND sm.user = :user AND sm.status = 'ACTIVE')) " +
           "AND fs.status = 'ACTIVE'")
    boolean hasUserActiveSessions(@Param("user") User user);
}
