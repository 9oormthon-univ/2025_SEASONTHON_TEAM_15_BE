package com.goormthon.team15.memo.controller;

import com.goormthon.team15.memo.dto.*;
import com.goormthon.team15.memo.service.MemoService;
import com.goormthon.team15.user.entity.User;
import com.goormthon.team15.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/memos")
@CrossOrigin(origins = "*")
@Tag(name = "메모", description = "메모 관련 API")
public class MemoController {
    
    @Autowired
    private MemoService memoService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Operation(
        summary = "메모 생성",
        description = "새로운 메모를 생성합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 생성 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoResponse.class)
            )),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/create")
    public ResponseEntity<MemoResponse> createMemo(
            @Parameter(description = "메모 제목", required = true, example = "오늘 할 일")
            @RequestParam("title") String title,
            @Parameter(description = "메모 내용", example = "1. 운동하기\n2. 책 읽기")
            @RequestParam(value = "content", required = false) String content,
            @Parameter(description = "가족 세션 ID", required = true, example = "1")
            @RequestParam("familySessionId") Long familySessionId,
            @Parameter(description = "다짐 대상자 ID", required = true, example = "2")
            @RequestParam("targetUserId") Long targetUserId,
            @Parameter(description = "익명 여부", example = "false")
            @RequestParam(value = "isAnonymous", required = false, defaultValue = "false") Boolean isAnonymous,
            @Parameter(description = "메모 색상", example = "LIGHT_PINK")
            @RequestParam(value = "memoColor", required = false) String memoColor,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        CreateMemoRequest request = new CreateMemoRequest();
        request.setTitle(title);
        request.setContent(content);
        request.setFamilySessionId(familySessionId);
        request.setTargetUserId(targetUserId);
        request.setIsAnonymous(isAnonymous);
        request.setMemoColor(memoColor);
        MemoResponse response = memoService.createMemo(request, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "메모 목록 조회",
        description = "현재 사용자의 메모 목록을 조회합니다. 페이징을 지원합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoListResponse.class)
            )),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/list")
    public ResponseEntity<MemoListResponse> getMemos(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지당 메모 수", example = "10")
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        MemoListResponse response = memoService.getUserMemos(user, page, size);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "모든 메모 조회",
        description = "현재 사용자의 모든 메모를 조회합니다. 페이징 없이 전체 목록을 반환합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoResponse.class)
            )),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/all")
    public ResponseEntity<List<MemoResponse>> getAllMemos(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        List<MemoResponse> response = memoService.getAllUserMemos(user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "가족 세션별 메모 조회",
        description = "특정 가족 세션의 모든 메모를 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoResponse.class)
            )),
        @ApiResponse(responseCode = "401", description = "인증 실패"),
        @ApiResponse(responseCode = "403", description = "세션 접근 권한 없음")
    })
    @GetMapping("/family-session/{familySessionId}")
    public ResponseEntity<List<MemoResponse>> getFamilySessionMemos(
            @Parameter(description = "가족 세션 ID", required = true, example = "1")
            @PathVariable Long familySessionId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        List<MemoResponse> response = memoService.getFamilySessionMemos(familySessionId, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "메모 상세 조회",
        description = "특정 메모의 상세 정보를 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 상세 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoResponse.class)
            )),
        @ApiResponse(responseCode = "404", description = "메모를 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/{memoId}")
    public ResponseEntity<MemoResponse> getMemo(
            @Parameter(description = "메모 ID", required = true, example = "1")
            @PathVariable Long memoId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        MemoResponse response = memoService.getMemo(memoId, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "메모 수정",
        description = "기존 메모를 수정합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 수정 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoResponse.class)
            )),
        @ApiResponse(responseCode = "404", description = "메모를 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PutMapping("/{memoId}")
    public ResponseEntity<MemoResponse> updateMemo(
            @Parameter(description = "메모 ID", required = true, example = "1")
            @PathVariable Long memoId,
            @Parameter(description = "메모 제목", example = "수정된 제목")
            @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "메모 내용", example = "수정된 내용")
            @RequestParam(value = "content", required = false) String content,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        UpdateMemoRequest request = new UpdateMemoRequest(title, content);
        MemoResponse response = memoService.updateMemo(memoId, request, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "메모 삭제",
        description = "메모를 삭제합니다. (소프트 삭제)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "메모를 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @DeleteMapping("/{memoId}")
    public ResponseEntity<String> deleteMemo(
            @Parameter(description = "메모 ID", required = true, example = "1")
            @PathVariable Long memoId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        memoService.deleteMemo(memoId, user);
        return ResponseEntity.ok("메모가 삭제되었습니다");
    }
    
    @Operation(
        summary = "메모 검색",
        description = "메모를 검색합니다. 제목, 내용, 또는 전체에서 검색할 수 있습니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 검색 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoResponse.class)
            )),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/search")
    public ResponseEntity<List<MemoResponse>> searchMemos(
            @Parameter(description = "검색 키워드", required = true, example = "운동")
            @RequestParam("keyword") String keyword,
            @Parameter(description = "검색 타입 (title, content, all)", example = "all")
            @RequestParam(value = "type", required = false, defaultValue = "all") String searchType,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        List<MemoResponse> response = memoService.searchMemos(user, keyword, searchType);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "메모 수 조회",
        description = "현재 사용자의 총 메모 수를 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 수 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/count")
    public ResponseEntity<Long> getMemoCount(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        long count = memoService.getUserMemoCount(user);
        return ResponseEntity.ok(count);
    }
    
    
}
