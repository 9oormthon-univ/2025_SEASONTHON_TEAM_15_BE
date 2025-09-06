package com.goormthon.team15.memo.service;

import com.goormthon.team15.memo.dto.*;
import com.goormthon.team15.memo.entity.Memo;
import com.goormthon.team15.memo.repository.MemoRepository;
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
    
    /**
     * 메모 생성
     */
    public MemoResponse createMemo(CreateMemoRequest request, User user) {
        Memo memo = new Memo(request.getTitle(), request.getContent(), user);
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
     * 메모 검색
     */
    @Transactional(readOnly = true)
    public List<MemoResponse> searchMemos(User user, String keyword, String searchType) {
        List<Memo> memos;
        
        switch (searchType.toLowerCase()) {
            case "title":
                memos = memoRepository.findByUserAndStatusAndTitleContaining(
                        user, Memo.MemoStatus.ACTIVE, keyword);
                break;
            case "content":
                memos = memoRepository.findByUserAndStatusAndContentContaining(
                        user, Memo.MemoStatus.ACTIVE, keyword);
                break;
            case "all":
            default:
                memos = memoRepository.findByUserAndStatusAndTitleOrContentContaining(
                        user, Memo.MemoStatus.ACTIVE, keyword);
                break;
        }
        
        return memos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자의 메모 수 조회
     */
    @Transactional(readOnly = true)
    public long getUserMemoCount(User user) {
        return memoRepository.countByUserAndStatus(user, Memo.MemoStatus.ACTIVE);
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
        
        return new MemoResponse(
                memo.getId(),
                memo.getTitle(),
                memo.getContent(),
                userInfo,
                memo.getStatus().name(),
                memo.getCreatedAt(),
                memo.getUpdatedAt()
        );
    }
}
