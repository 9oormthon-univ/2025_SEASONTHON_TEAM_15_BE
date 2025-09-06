package com.goormthon.team15.family.controller;

import com.goormthon.team15.family.dto.*;
import com.goormthon.team15.family.service.FamilySessionService;
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
@RequestMapping("/v1/api/family-sessions")
@CrossOrigin(origins = "*")
@Tag(name = "가족 세션", description = "가족 세션 관련 API")
public class FamilySessionController {
    
    @Autowired
    private FamilySessionService familySessionService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Operation(
        summary = "가족 세션 생성",
        description = "새로운 가족 세션을 생성합니다. 생성자는 자동으로 관리자 권한을 가집니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "세션 생성 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.class)
            )),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/create")
    public ResponseEntity<FamilySessionResponse> createFamilySession(
            @Parameter(description = "세션 이름", required = true, example = "우리 가족의 2025년 다짐")
            @RequestParam("name") String name,
            @Parameter(description = "세션 설명", example = "가족 모두의 새해 다짐을 공유해보세요!")
            @RequestParam(value = "description", required = false) String description,
            @Parameter(description = "세션 비밀번호 (선택사항)", example = "family2025")
            @RequestParam(value = "sessionPassword", required = false) String sessionPassword,
            @Parameter(description = "최대 참여자 수", example = "10")
            @RequestParam(value = "maxMembers", required = false, defaultValue = "10") Integer maxMembers,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        CreateFamilySessionRequest request = new CreateFamilySessionRequest(name, description, sessionPassword, maxMembers);
        FamilySessionResponse response = familySessionService.createFamilySession(request, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "가족 세션 참여",
        description = "초대 코드를 사용하여 가족 세션에 참여합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "세션 참여 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.class)
            )),
        @ApiResponse(responseCode = "400", description = "잘못된 초대 코드 또는 이미 참여한 세션"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/join")
    public ResponseEntity<FamilySessionResponse> joinFamilySession(
            @Parameter(description = "초대 코드", required = true, example = "ABC123")
            @RequestParam("inviteCode") String inviteCode,
            @Parameter(description = "세션 비밀번호 (세션에 비밀번호가 설정된 경우 필수)", example = "family2025")
            @RequestParam(value = "sessionPassword", required = false) String sessionPassword,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        JoinSessionRequest request = new JoinSessionRequest(inviteCode, sessionPassword);
        FamilySessionResponse response = familySessionService.joinFamilySession(request, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "사용자 가족 세션 목록 조회",
        description = "현재 사용자가 참여한 모든 가족 세션 목록을 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "세션 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.class)
            )),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/my-sessions")
    public ResponseEntity<List<FamilySessionResponse>> getMyFamilySessions(
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        List<FamilySessionResponse> responses = familySessionService.getUserFamilySessions(user);
        return ResponseEntity.ok(responses);
    }
    
    @Operation(
        summary = "가족 세션 상세 조회",
        description = "특정 가족 세션의 상세 정보를 조회합니다. 세션 참여자만 조회 가능합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "세션 상세 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.class)
            )),
        @ApiResponse(responseCode = "403", description = "세션 접근 권한 없음"),
        @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/{sessionId}")
    public ResponseEntity<FamilySessionResponse> getFamilySession(
            @Parameter(description = "세션 ID", required = true, example = "1")
            @PathVariable Long sessionId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        FamilySessionResponse response = familySessionService.getFamilySession(sessionId, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "가족 세션 탈퇴",
        description = "가족 세션에서 탈퇴합니다. 세션 생성자는 탈퇴할 수 없습니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "세션 탈퇴 성공"),
        @ApiResponse(responseCode = "400", description = "세션 생성자는 탈퇴할 수 없음"),
        @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/{sessionId}/leave")
    public ResponseEntity<String> leaveFamilySession(
            @Parameter(description = "세션 ID", required = true, example = "1")
            @PathVariable Long sessionId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        familySessionService.leaveFamilySession(sessionId, user);
        return ResponseEntity.ok("세션에서 탈퇴했습니다");
    }
    
    @Operation(
        summary = "가족 세션 삭제",
        description = "가족 세션을 삭제합니다. 세션 생성자만 삭제할 수 있습니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "세션 삭제 성공"),
        @ApiResponse(responseCode = "403", description = "세션 삭제 권한 없음"),
        @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<String> deleteFamilySession(
            @Parameter(description = "세션 ID", required = true, example = "1")
            @PathVariable Long sessionId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        familySessionService.deleteFamilySession(sessionId, user);
        return ResponseEntity.ok("세션이 삭제되었습니다");
    }
    
    @Operation(
        summary = "초대 URL 생성",
        description = "가족 세션의 초대 URL을 생성합니다. 생성자나 멤버만 생성할 수 있습니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "초대 URL 생성 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InviteUrlResponse.class)
            )),
        @ApiResponse(responseCode = "403", description = "세션 접근 권한 없음"),
        @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/{sessionId}/invite-url")
    public ResponseEntity<InviteUrlResponse> generateInviteUrl(
            @Parameter(description = "세션 ID", required = true, example = "1")
            @PathVariable Long sessionId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        InviteUrlResponse response = familySessionService.generateInviteUrl(sessionId, user);
        return ResponseEntity.ok(response);
    }
}
