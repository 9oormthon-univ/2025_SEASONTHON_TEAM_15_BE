package com.goormthon.team15.family.repository;

import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.family.entity.SessionMember;
import com.goormthon.team15.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionMemberRepository extends JpaRepository<SessionMember, Long> {
    
    /**
     * 특정 세션의 모든 멤버 조회
     */
    List<SessionMember> findByFamilySessionOrderByJoinedAtAsc(FamilySession familySession);
    
    /**
     * 특정 세션의 활성 멤버만 조회
     */
    List<SessionMember> findByFamilySessionAndStatusOrderByJoinedAtAsc(
            FamilySession familySession, SessionMember.MemberStatus status);
    
    /**
     * 사용자가 특정 세션의 멤버인지 확인
     */
    Optional<SessionMember> findByFamilySessionAndUser(FamilySession familySession, User user);
    
    /**
     * 사용자가 특정 세션의 활성 멤버인지 확인
     */
    Optional<SessionMember> findByFamilySessionAndUserAndStatus(
            FamilySession familySession, User user, SessionMember.MemberStatus status);
    
    /**
     * 사용자가 참여한 모든 세션의 멤버 정보 조회
     */
    List<SessionMember> findByUserAndStatusOrderByJoinedAtDesc(
            User user, SessionMember.MemberStatus status);
    
    /**
     * 특정 세션의 멤버 수 조회
     */
    long countByFamilySessionAndStatus(FamilySession familySession, SessionMember.MemberStatus status);
    
    /**
     * 특정 세션의 관리자 멤버 조회
     */
    List<SessionMember> findByFamilySessionAndRoleAndStatus(
            FamilySession familySession, SessionMember.MemberRole role, SessionMember.MemberStatus status);
    
    /**
     * 사용자가 특정 세션의 관리자인지 확인
     */
    @Query("SELECT COUNT(sm) > 0 FROM SessionMember sm " +
           "WHERE sm.familySession = :familySession AND sm.user = :user " +
           "AND sm.role = 'ADMIN' AND sm.status = 'ACTIVE'")
    boolean isUserAdminOfSession(@Param("familySession") FamilySession familySession, @Param("user") User user);
    
    /**
     * 특정 세션에서 사용자 제거 (상태를 LEFT로 변경)
     */
    @Query("UPDATE SessionMember sm SET sm.status = 'LEFT' " +
           "WHERE sm.familySession = :familySession AND sm.user = :user")
    void leaveSession(@Param("familySession") FamilySession familySession, @Param("user") User user);
}
