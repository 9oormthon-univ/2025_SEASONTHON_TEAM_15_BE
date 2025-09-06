package com.goormthon.team15.family.service;

import com.goormthon.team15.family.dto.*;
import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.family.entity.SessionMember;
import com.goormthon.team15.family.repository.FamilySessionRepository;
import com.goormthon.team15.family.repository.SessionMemberRepository;
import com.goormthon.team15.user.entity.User;
import com.goormthon.team15.user.repository.UserRepository;
import com.goormthon.team15.memo.entity.Memo;
import com.goormthon.team15.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FamilySessionService {
    
    @Autowired
    private FamilySessionRepository familySessionRepository;
    
    @Autowired
    private SessionMemberRepository sessionMemberRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MemoRepository memoRepository;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    /**
     * 가족 세션 생성
     */
    public FamilySessionResponse createFamilySession(CreateFamilySessionRequest request, User creator) {
        // 초대 코드 생성 (중복되지 않을 때까지)
        String inviteCode = generateUniqueInviteCode();
        System.out.println("Generated invite code: " + inviteCode);
        
        // 가족 세션 생성
        FamilySession familySession = new FamilySession(
                request.getName(),
                request.getDescription(),
                inviteCode,
                request.getSessionPassword(),
                creator
        );
        
        if (request.getMaxMembers() != null) {
            familySession.setMaxMembers(request.getMaxMembers());
        }
        
        FamilySession savedSession = familySessionRepository.save(familySession);
        
        // 생성자를 관리자로 세션에 추가
        SessionMember creatorMember = new SessionMember(savedSession, creator, SessionMember.MemberRole.ADMIN);
        sessionMemberRepository.save(creatorMember);
        
        // 생성자의 hasFamily를 true로 설정
        creator.setHasFamily(true);
        userRepository.save(creator);
        
        return convertToResponse(savedSession);
    }
    
    /**
     * 초대 코드로 가족 세션 참여
     */
    public FamilySessionResponse joinFamilySession(JoinSessionRequest request, User user) {
        // 초대 코드로 세션 찾기
        FamilySession familySession = familySessionRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 코드입니다"));
        
        // 세션이 활성 상태인지 확인
        if (familySession.getStatus() != FamilySession.SessionStatus.ACTIVE) {
            throw new IllegalStateException("비활성 상태인 세션입니다");
        }
        
        // 이미 참여한 멤버인지 확인
        if (familySessionRepository.isUserMemberOfSession(familySession.getId(), user)) {
            throw new IllegalStateException("이미 참여한 세션입니다");
        }
        
        // 세션이 가득 찼는지 확인 (활성 멤버 수 기준)
        int currentActiveMemberCount = (int) sessionMemberRepository.countByFamilySessionAndStatus(
                familySession, SessionMember.MemberStatus.ACTIVE);
        if (currentActiveMemberCount >= familySession.getMaxMembers()) {
            throw new IllegalStateException("세션이 가득 찼습니다");
        }
        
        // 세션 비밀번호 확인
        if (familySession.getSessionPassword() != null && !familySession.getSessionPassword().isEmpty()) {
            if (request.getSessionPassword() == null || request.getSessionPassword().isEmpty()) {
                throw new IllegalArgumentException("세션 비밀번호가 필요합니다");
            }
            if (!familySession.getSessionPassword().equals(request.getSessionPassword())) {
                throw new IllegalArgumentException("세션 비밀번호가 올바르지 않습니다");
            }
        }
        
        // 멤버 추가
        SessionMember newMember = new SessionMember(familySession, user, SessionMember.MemberRole.MEMBER);
        sessionMemberRepository.save(newMember);
        
        // 사용자의 hasFamily를 true로 설정
        user.setHasFamily(true);
        userRepository.save(user);
        
        return convertToResponse(familySession);
    }
    
    /**
     * 사용자의 가족 세션 조회 (단일 세션)
     */
    @Transactional(readOnly = true)
    public FamilySessionResponse getUserFamilySession(User user) {
        List<FamilySession> sessions = familySessionRepository.findSessionsByUser(user);
        if (sessions.isEmpty()) {
            throw new IllegalArgumentException("참여한 가족 세션이 없습니다");
        }
        
        // 첫 번째 세션 반환 (사용자는 하나의 가족에만 속할 수 있음)
        return convertToResponse(sessions.get(0));
    }
    
    /**
     * 사용자가 참여한 모든 가족 세션 조회
     */
    @Transactional(readOnly = true)
    public List<FamilySessionResponse> getUserFamilySessions(User user) {
        List<FamilySession> sessions = familySessionRepository.findSessionsByUser(user);
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 가족 세션 상세 조회
     */
    @Transactional(readOnly = true)
    public FamilySessionResponse getFamilySession(Long sessionId, User user) {
        FamilySession familySession = familySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다"));
        
        // 사용자가 세션에 참여했는지 확인
        if (!familySessionRepository.isUserMemberOfSession(sessionId, user) && 
            !familySession.isCreator(user)) {
            throw new IllegalStateException("세션에 접근할 권한이 없습니다");
        }
        
        return convertToResponse(familySession);
    }
    
    /**
     * 가족 세션에서 탈퇴
     */
    public void leaveFamilySession(Long sessionId, User user) {
        FamilySession familySession = familySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다"));
        
        // 생성자는 탈퇴할 수 없음
        if (familySession.isCreator(user)) {
            throw new IllegalStateException("세션 생성자는 탈퇴할 수 없습니다");
        }
        
        // 멤버 상태를 LEFT로 변경
        sessionMemberRepository.leaveSession(familySession, user);
        
        // 사용자가 다른 가족 세션에 참여하고 있는지 확인
        boolean hasOtherActiveSessions = familySessionRepository.hasUserActiveSessions(user);
        if (!hasOtherActiveSessions) {
            // 다른 활성 세션이 없으면 hasFamily를 false로 설정
            user.setHasFamily(false);
            userRepository.save(user);
        }
    }
    
    /**
     * 가족 세션 삭제 (생성자만 가능)
     */
    public void deleteFamilySession(Long sessionId, User user) {
        FamilySession familySession = familySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다"));
        
        // 생성자인지 확인
        if (!familySession.isCreator(user)) {
            throw new IllegalStateException("세션을 삭제할 권한이 없습니다");
        }
        
        // 세션 상태를 INACTIVE로 변경
        familySession.setStatus(FamilySession.SessionStatus.INACTIVE);
        familySessionRepository.save(familySession);
    }
    
    /**
     * 내 가족 세션 멤버 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FamilySessionResponse.MemberInfo> getMyFamilySessionMembers(User user) {
        List<FamilySession> sessions = familySessionRepository.findSessionsByUser(user);
        if (sessions.isEmpty()) {
            throw new IllegalArgumentException("참여한 가족 세션이 없습니다");
        }
        
        FamilySession familySession = sessions.get(0);
        
        // 멤버 정보 조회
        List<SessionMember> activeMembers = sessionMemberRepository
                .findByFamilySessionAndStatusOrderByJoinedAtAsc(
                        familySession, SessionMember.MemberStatus.ACTIVE);
        
        // Member 정보 리스트 생성
        return activeMembers.stream()
                .map(member -> {
                    FamilySessionResponse.CreatorInfo memberUserInfo = new FamilySessionResponse.CreatorInfo(
                            member.getUser().getId(),
                            member.getUser().getUsername(),
                            member.getUser().getPhoneNumber(),
                            member.getUser().getGeneration() != null ? 
                                    member.getUser().getGeneration().name() : null
                    );
                    
                    return new FamilySessionResponse.MemberInfo(
                            member.getId(),
                            memberUserInfo,
                            member.getRole().name(),
                            member.getStatus().name(),
                            member.getJoinedAt()
                    );
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 가족 세션 멤버 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FamilySessionResponse.MemberInfo> getFamilySessionMembers(Long sessionId, User user) {
        FamilySession familySession = familySessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션을 찾을 수 없습니다"));
        
        // 사용자가 세션에 참여했는지 확인
        if (!familySessionRepository.isUserMemberOfSession(sessionId, user) && 
            !familySession.isCreator(user)) {
            throw new IllegalStateException("세션에 접근할 권한이 없습니다");
        }
        
        // 멤버 정보 조회
        List<SessionMember> activeMembers = sessionMemberRepository
                .findByFamilySessionAndStatusOrderByJoinedAtAsc(
                        familySession, SessionMember.MemberStatus.ACTIVE);
        
        // Member 정보 리스트 생성
        return activeMembers.stream()
                .map(member -> {
                    FamilySessionResponse.CreatorInfo memberUserInfo = new FamilySessionResponse.CreatorInfo(
                            member.getUser().getId(),
                            member.getUser().getUsername(),
                            member.getUser().getPhoneNumber(),
                            member.getUser().getGeneration() != null ? 
                                    member.getUser().getGeneration().name() : null
                    );
                    
                    return new FamilySessionResponse.MemberInfo(
                            member.getId(),
                            memberUserInfo,
                            member.getRole().name(),
                            member.getStatus().name(),
                            member.getJoinedAt()
                    );
                })
                .collect(Collectors.toList());
    }
    
    
    /**
     * 고유한 초대 코드 생성
     */
    private String generateUniqueInviteCode() {
        String inviteCode;
        do {
            inviteCode = generateRandomCode();
        } while (familySessionRepository.existsByInviteCode(inviteCode));
        
        return inviteCode;
    }
    
    /**
     * 랜덤 코드 생성 (6자리 영문+숫자)
     */
    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * chars.length());
            code.append(chars.charAt(index));
        }
        
        return code.toString();
    }
    
    /**
     * FamilySession 엔티티를 Response DTO로 변환
     */
    private FamilySessionResponse convertToResponse(FamilySession familySession) {
        // 멤버 정보 조회
        List<SessionMember> activeMembers = sessionMemberRepository
                .findByFamilySessionAndStatusOrderByJoinedAtAsc(
                        familySession, SessionMember.MemberStatus.ACTIVE);
        
        // Creator 정보 생성
        FamilySessionResponse.CreatorInfo creatorInfo = new FamilySessionResponse.CreatorInfo(
                familySession.getCreator().getId(),
                familySession.getCreator().getUsername(),
                familySession.getCreator().getPhoneNumber(),
                familySession.getCreator().getGeneration() != null ? 
                        familySession.getCreator().getGeneration().name() : null
        );
        
        // Member 정보 리스트 생성
        List<FamilySessionResponse.MemberInfo> memberInfos = activeMembers.stream()
                .map(member -> {
                    FamilySessionResponse.CreatorInfo memberUserInfo = new FamilySessionResponse.CreatorInfo(
                            member.getUser().getId(),
                            member.getUser().getUsername(),
                            member.getUser().getPhoneNumber(),
                            member.getUser().getGeneration() != null ? 
                                    member.getUser().getGeneration().name() : null
                    );
                    
                    return new FamilySessionResponse.MemberInfo(
                            member.getId(),
                            memberUserInfo,
                            member.getRole().name(),
                            member.getStatus().name(),
                            member.getFamilyRelationship() != null ? member.getFamilyRelationship().name() : null,
                            member.getJoinedAt()
                    );
                })
                .collect(Collectors.toList());
        
        FamilySessionResponse response = new FamilySessionResponse(
                familySession.getId(),
                familySession.getName(),
                familySession.getDescription(),
                familySession.getInviteCode(),
                creatorInfo,
                familySession.getStatus().name(),
                familySession.getMaxMembers(),
                activeMembers.size(),
                memberInfos,
                familySession.getCreatedAt(),
                familySession.getUpdatedAt()
        );
        
        System.out.println("Created FamilySessionResponse with invite code: " + response.getInviteCode());
        return response;
    }
    
    /**
     * 가족 구성원 관계 설정
     */
    public void setFamilyRelationship(Long memberId, String relationship, User requester) {
        SessionMember member = sessionMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버를 찾을 수 없습니다"));
        
        // 요청자가 같은 가족 세션의 관리자인지 확인
        SessionMember requesterMember = sessionMemberRepository
                .findByFamilySessionAndUserAndStatus(member.getFamilySession(), requester, SessionMember.MemberStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("가족 세션에 참여하지 않았습니다"));
        
        if (requesterMember.getRole() != SessionMember.MemberRole.ADMIN) {
            throw new RuntimeException("관리자만 가족 관계를 설정할 수 있습니다");
        }
        
        // 관계 설정
        try {
            SessionMember.FamilyRelationship familyRelationship = SessionMember.FamilyRelationship.valueOf(relationship);
            member.setFamilyRelationship(familyRelationship);
            sessionMemberRepository.save(member);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 가족 관계입니다: " + relationship);
        }
    }
    
    /**
     * 가족별 신년 다짐 조회
     */
    public List<ResolutionResponse> getFamilyResolutions(User user) {
        // 사용자가 참여한 가족 세션 조회
        SessionMember sessionMember = sessionMemberRepository
                .findByUserAndStatus(user, SessionMember.MemberStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("참여한 가족 세션이 없습니다"));
        
        FamilySession familySession = sessionMember.getFamilySession();
        
        // 해당 가족 세션의 모든 메모 조회 (신년 다짐으로 사용)
        List<Memo> memos = memoRepository.findByFamilySessionAndDeletedFalseOrderByCreatedAtDesc(familySession);
        
        return memos.stream()
                .map(memo -> {
                    // 대상자 정보 생성
                    ResolutionResponse.TargetUserInfo targetUserInfo = new ResolutionResponse.TargetUserInfo(
                            memo.getTargetUser().getId(),
                            memo.getTargetUser().getUsername(),
                            getFamilyRelationshipForUser(memo.getTargetUser(), familySession),
                            memo.getTargetUser().getGeneration() != null ? memo.getTargetUser().getGeneration().name() : null
                    );
                    
                    // 작성자 정보 생성
                    ResolutionResponse.WriterInfo writerInfo = new ResolutionResponse.WriterInfo(
                            memo.getWriter().getId(),
                            memo.getWriter().getUsername(),
                            getFamilyRelationshipForUser(memo.getWriter(), familySession)
                    );
                    
                    return new ResolutionResponse(
                            memo.getId(),
                            memo.getTitle(),
                            memo.getContent(),
                            targetUserInfo,
                            writerInfo,
                            memo.getMemoColor() != null ? memo.getMemoColor().name() : null,
                            memo.getIsAnonymous(),
                            memo.getCreatedAt(),
                            memo.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자의 가족 관계 조회
     */
    private String getFamilyRelationshipForUser(User user, FamilySession familySession) {
        return sessionMemberRepository
                .findByFamilySessionAndUserAndStatus(familySession, user, SessionMember.MemberStatus.ACTIVE)
                .map(member -> member.getFamilyRelationship() != null ? member.getFamilyRelationship().name() : null)
                .orElse(null);
    }
}
