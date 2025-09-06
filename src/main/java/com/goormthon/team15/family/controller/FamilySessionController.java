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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/v1/api/family-sessions")
@CrossOrigin(origins = "*")
@Tag(name = "가족 세션", description = "가족 세션 관련 API")
public class FamilySessionController {
    
    @Autowired
    private FamilySessionService familySessionService;
    
    @Autowired
    private UserRepository userRepository;
    
    // 현재 사용자의 가족 세션 정보 조회 (단일 세션)
    @Operation(
        summary = "내 가족 세션 조회",
        description = "현재 사용자가 참여한 가족 세션 정보를 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "세션 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.class)
            )),
        @ApiResponse(responseCode = "404", description = "참여한 가족 세션이 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/my-session")
    public ResponseEntity<FamilySessionResponse> getMyFamilySession(
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        FamilySessionResponse response = familySessionService.getUserFamilySession(user);
        return ResponseEntity.ok(response);
    }
    
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
        summary = "내 가족 세션 멤버 목록 조회",
        description = "현재 사용자가 참여한 가족 세션의 참여자 목록을 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "멤버 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.MemberInfo.class)
            )),
        @ApiResponse(responseCode = "404", description = "참여한 가족 세션이 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/my-session/members")
    public ResponseEntity<List<FamilySessionResponse.MemberInfo>> getMyFamilySessionMembers(
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        List<FamilySessionResponse.MemberInfo> members = familySessionService.getMyFamilySessionMembers(user);
        return ResponseEntity.ok(members);
    }
    
    @Operation(
        summary = "가족 구성원 관계 설정",
        description = "가족 세션 멤버의 가족 관계를 설정합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "관계 설정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "403", description = "권한 없음"),
        @ApiResponse(responseCode = "404", description = "멤버를 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PutMapping("/members/{memberId}/relationship")
    public ResponseEntity<String> setFamilyRelationship(
            @Parameter(description = "멤버 ID", required = true, example = "1")
            @PathVariable Long memberId,
            @Parameter(description = "가족 관계", required = true, example = "FATHER")
            @RequestParam("relationship") String relationship,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        familySessionService.setFamilyRelationship(memberId, relationship, user);
        return ResponseEntity.ok("가족 관계가 설정되었습니다");
    }
    
    @Operation(
        summary = "D-Day 계산",
        description = "2026년까지 남은 일수를 계산합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "D-Day 계산 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DdayResponse.class)
            )),
        @ApiResponse(responseCode = "404", description = "참여한 가족 세션이 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/my-session/dday")
    public ResponseEntity<DdayResponse> getDday(Authentication authentication) {
        // 2026년 1월 1일을 목표 날짜로 설정
        LocalDate targetDate = LocalDate.of(2026, 1, 1);
        LocalDate today = LocalDate.now();
        
        long daysLeft = ChronoUnit.DAYS.between(today, targetDate);
        String ddayMessage = String.format("2026년 D-%d", daysLeft);
        
        DdayResponse response = new DdayResponse(targetDate, daysLeft, ddayMessage, 2026);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "가족별 신년 다짐 조회",
        description = "현재 가족 세션의 모든 신년 다짐을 조회합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "신년 다짐 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResolutionResponse.class)
            )),
        @ApiResponse(responseCode = "404", description = "참여한 가족 세션이 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @GetMapping("/my-session/resolutions")
    public ResponseEntity<List<ResolutionResponse>> getFamilyResolutions(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        List<ResolutionResponse> resolutions = familySessionService.getFamilyResolutions(user);
        return ResponseEntity.ok(resolutions);
    }
    
    @Operation(
        summary = "가족 설정 및 세션 생성",
        description = "가족 별명, 사용자 닉네임, 메모지 템플릿을 설정하고 가족 세션을 생성합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "가족 설정 및 세션 생성 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.class)
            )),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/setup-and-create")
    public ResponseEntity<FamilySessionResponse> setupFamilyAndCreateSession(
            @RequestBody FamilySetupRequest request,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        FamilySessionResponse response = familySessionService.setupFamilyAndCreateSession(request, user);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "메모지 템플릿 목록 조회",
        description = "사용 가능한 메모지 템플릿 목록을 조회합니다. 공개 API로 인증이 필요하지 않습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모지 템플릿 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MemoTemplateResponse.class)
            ))
    })
    @GetMapping("/memo-templates")
    public ResponseEntity<List<MemoTemplateResponse>> getMemoTemplates() {
        List<MemoTemplateResponse> templates = familySessionService.getMemoTemplates();
        return ResponseEntity.ok(templates);
    }
    
    @Operation(
        summary = "가족 세션 정보 업데이트",
        description = "가족 별명과 메모지 템플릿을 업데이트합니다. 관리자만 가능합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "가족 세션 정보 업데이트 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FamilySessionResponse.class)
            )),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "403", description = "권한 없음"),
        @ApiResponse(responseCode = "404", description = "세션을 찾을 수 없음"),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PutMapping("/my-session/update")
    public ResponseEntity<FamilySessionResponse> updateFamilySessionInfo(
            @Parameter(description = "가족 별명", example = "조버드네")
            @RequestParam(value = "familyNickname", required = false) String familyNickname,
            @Parameter(description = "메모지 템플릿 ID", example = "HOUSE_PATTERN")
            @RequestParam(value = "memoTemplateId", required = false) String memoTemplateId,
            Authentication authentication) {
        
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));
        
        FamilySessionResponse response = familySessionService.updateFamilySessionInfo(user, familyNickname, memoTemplateId);
        return ResponseEntity.ok(response);
    }
    
}
