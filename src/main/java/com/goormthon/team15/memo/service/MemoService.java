package com.goormthon.team15.memo.service;

import com.goormthon.team15.memo.dto.*;
import com.goormthon.team15.memo.entity.Memo;
import com.goormthon.team15.memo.repository.MemoRepository;
import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.family.repository.FamilySessionRepository;
import com.goormthon.team15.user.entity.User;
import com.goormthon.team15.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemoService {
    
    @Autowired
    private MemoRepository memoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FamilySessionRepository familySessionRepository;
    
    /**
     * 메모 생성
     */
    public MemoResponse createMemo(CreateMemoRequest request, User user) {
        // 가족 세션 조회
        FamilySession familySession = familySessionRepository.findById(request.getFamilySessionId())
                .orElseThrow(() -> new IllegalArgumentException("가족 세션을 찾을 수 없습니다"));
        
        // 대상 사용자 조회
        User targetUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new IllegalArgumentException("대상 사용자를 찾을 수 없습니다"));
        
        // 메모 색상 설정
        Memo.MemoColor memoColor = Memo.MemoColor.LIGHT_PINK; // 기본값
        if (request.getMemoColor() != null) {
            try {
                memoColor = Memo.MemoColor.valueOf(request.getMemoColor());
            } catch (IllegalArgumentException e) {
                // 잘못된 색상이면 기본값 사용
            }
        }
        
        Memo memo = new Memo(request.getTitle(), request.getContent(), user, familySession, targetUser);
        memo.setIsAnonymous(request.getIsAnonymous());
        memo.setMemoColor(memoColor);
        
        Memo savedMemo = memoRepository.save(memo);
        return convertToResponse(savedMemo);
    }
    
    /**
     * 사용자의 메모 목록 조회
     */
    @Transactional(readOnly = true)
    public MemoListResponse getUserMemos(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Memo> memoPage = memoRepository.findByUserAndStatusOrderByCreatedAtDesc(
                user, Memo.MemoStatus.ACTIVE, pageable);
        
        List<MemoResponse> memoResponses = memoPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        
        return new MemoListResponse(
                memoResponses,
                memoPage.getTotalElements(),
                memoPage.getNumber(),
                memoPage.getTotalPages(),
                memoPage.getSize()
        );
    }
    
    /**
     * 사용자의 모든 메모 목록 조회 (페이징 없음)
     */
    @Transactional(readOnly = true)
    public List<MemoResponse> getAllUserMemos(User user) {
        List<Memo> memos = memoRepository.findByUserAndStatusOrderByCreatedAtDesc(
                user, Memo.MemoStatus.ACTIVE);
        
        return memos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 메모 상세 조회
     */
    @Transactional(readOnly = true)
    public MemoResponse getMemo(Long memoId, User user) {
        Memo memo = memoRepository.findByIdAndUserAndStatus(memoId, user, Memo.MemoStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다"));
        
        return convertToResponse(memo);
    }
    
    /**
     * 메모 수정
     */
    public MemoResponse updateMemo(Long memoId, UpdateMemoRequest request, User user) {
        Memo memo = memoRepository.findByIdAndUserAndStatus(memoId, user, Memo.MemoStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다"));
        
        // 제목 수정
        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            memo.setTitle(request.getTitle().trim());
        }
        
        // 내용 수정
        if (request.getContent() != null) {
            memo.setContent(request.getContent());
        }
        
        
        Memo updatedMemo = memoRepository.save(memo);
        return convertToResponse(updatedMemo);
    }
    
    /**
     * 메모 삭제 (소프트 삭제)
     */
    public void deleteMemo(Long memoId, User user) {
        Memo memo = memoRepository.findByIdAndUserAndStatus(memoId, user, Memo.MemoStatus.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다"));
        
        memo.setStatus(Memo.MemoStatus.DELETED);
        memoRepository.save(memo);
    }
    
    
    /**
     * 사용자의 메모 수 조회
     */
    @Transactional(readOnly = true)
    public long getUserMemoCount(User user) {
        return memoRepository.countByUserAndStatus(user, Memo.MemoStatus.ACTIVE);
    }
    
    /**
     * 가족 세션별 메모 조회
     */
    @Transactional(readOnly = true)
    public List<MemoResponse> getFamilySessionMemos(Long familySessionId, User user) {
        // 가족 세션 조회 및 권한 확인
        FamilySession familySession = familySessionRepository.findById(familySessionId)
                .orElseThrow(() -> new IllegalArgumentException("가족 세션을 찾을 수 없습니다"));
        
        // 사용자가 해당 가족 세션의 멤버인지 확인
        if (!familySession.hasMember(user) && !familySession.isCreator(user)) {
            throw new IllegalArgumentException("해당 가족 세션에 접근할 권한이 없습니다");
        }
        
        // 가족 세션의 모든 메모 조회 (사용자가 볼 수 있는 메모만)
        List<Memo> memos = memoRepository.findByFamilySessionAndStatusOrderByCreatedAtDesc(
                familySession, Memo.MemoStatus.ACTIVE);
        
        // 익명 메모 필터링 (작성자나 대상자만 볼 수 있음)
        List<Memo> visibleMemos = memos.stream()
                .filter(memo -> memo.canView(user))
                .collect(Collectors.toList());
        
        return visibleMemos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    
    /**
     * Memo 엔티티를 Response DTO로 변환
     */
    private MemoResponse convertToResponse(Memo memo) {
        MemoResponse.UserInfo userInfo = new MemoResponse.UserInfo(
                memo.getUser().getId(),
                memo.getUser().getUsername(),
                memo.getUser().getPhoneNumber(),
                memo.getUser().getGeneration() != null ? 
                        memo.getUser().getGeneration().name() : null
        );
        
        MemoResponse.UserInfo targetUserInfo = new MemoResponse.UserInfo(
                memo.getTargetUser().getId(),
                memo.getTargetUser().getUsername(),
                memo.getTargetUser().getPhoneNumber(),
                memo.getTargetUser().getGeneration() != null ? 
                        memo.getTargetUser().getGeneration().name() : null
        );
        
        return new MemoResponse(
                memo.getId(),
                memo.getTitle(),
                memo.getContent(),
                userInfo,
                targetUserInfo,
                memo.getIsAnonymous(),
                memo.getStatus().name(),
                memo.getMemoColor().name(),
                memo.getCreatedAt(),
                memo.getUpdatedAt()
        );
    }
    
}
