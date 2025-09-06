package com.goormthon.team15.family.controller;

import com.goormthon.team15.family.dto.InviteUrlResponse;
import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.family.entity.SessionMember;
import com.goormthon.team15.family.repository.FamilySessionRepository;
import com.goormthon.team15.family.repository.SessionMemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/invite")
@CrossOrigin(origins = "*")
@Tag(name = "초대", description = "초대 URL 관련 API")
public class InviteController {
    
    @Autowired
    private FamilySessionRepository familySessionRepository;
    
    @Autowired
    private SessionMemberRepository sessionMemberRepository;
    
    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    
    @Operation(
        summary = "초대 정보 조회",
        description = "초대 코드로 세션 정보를 조회합니다. 공개 API로 인증이 필요하지 않습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "초대 정보 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InviteUrlResponse.class)
            )),
        @ApiResponse(responseCode = "404", description = "유효하지 않은 초대 코드")
    })
    @GetMapping("/{inviteCode}")
    public ResponseEntity<InviteUrlResponse> getInviteInfo(
            @Parameter(description = "초대 코드", required = true, example = "ABC123")
            @PathVariable String inviteCode) {
        
        FamilySession familySession = familySessionRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 코드입니다"));
        
        // 세션이 활성 상태인지 확인
        if (familySession.getStatus() != FamilySession.SessionStatus.ACTIVE) {
            throw new IllegalStateException("비활성 상태인 세션입니다");
        }
        
        // 현재 멤버 수 조회
        int currentMemberCount = (int) sessionMemberRepository.countByFamilySessionAndStatus(
                familySession, SessionMember.MemberStatus.ACTIVE);
        
        // 초대 URL 생성
        String inviteUrl = baseUrl + "/v1/api/invite/" + familySession.getInviteCode();
        
        InviteUrlResponse response = new InviteUrlResponse(
                inviteUrl,
                familySession.getSessionPassword() != null && !familySession.getSessionPassword().isEmpty(),
                familySession.getInviteCode(),
                familySession.getName(),
                familySession.getDescription(),
                currentMemberCount,
                familySession.getMaxMembers()
        );
        
        return ResponseEntity.ok(response);
    }
}
