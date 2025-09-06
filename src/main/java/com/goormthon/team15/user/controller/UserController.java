package com.goormthon.team15.user.controller;

import com.goormthon.team15.user.dto.UpdateUserRequest;
import com.goormthon.team15.user.dto.UpdateUserResponse;
import com.goormthon.team15.user.dto.UserProfileResponse;
import com.goormthon.team15.user.service.UserService;
import com.goormthon.team15.user.entity.User;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "사용자", description = "사용자 관련 API")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Operation(
        summary = "현재 사용자 프로필 조회", 
        description = "로그인한 사용자의 프로필 정보를 조회합니다. JWT 토큰 인증이 필요합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "프로필 조회 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserProfileResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "성공 응답",
                    value = "{\n  \"id\": 1,\n  \"username\": \"testuser\",\n  \"email\": \"test@example.com\",\n  \"generation\": \"TWENTIES\",\n  \"role\": \"USER\",\n  \"createdAt\": \"2025-01-01T00:00:00\",\n  \"updatedAt\": \"2025-01-01T00:00:00\"\n}"
                )
            ))
    })
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(Authentication authentication) {
        String username = authentication.getName();
        UserProfileResponse response = userService.getUserProfile(username);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "사용자 정보 수정", 
        description = "로그인한 사용자의 정보를 수정합니다. JWT 토큰 인증이 필요합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "정보 수정 성공",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UpdateUserResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "성공 응답",
                    value = "{\n  \"message\": \"사용자 정보가 성공적으로 수정되었습니다\",\n  \"success\": true,\n  \"user\": {\n    \"id\": 1,\n    \"username\": \"testuser\",\n    \"email\": \"newemail@example.com\",\n    \"generation\": \"THIRTIES\",\n    \"role\": \"USER\",\n    \"createdAt\": \"2025-01-01T00:00:00\",\n    \"updatedAt\": \"2025-01-01T12:00:00\"\n  }\n}"
                )
            ))
    })
    @PutMapping("/profile")
    public ResponseEntity<UpdateUserResponse> updateUserProfile(
            Authentication authentication,
            @Parameter(description = "이메일 주소 (선택사항)", example = "newemail@example.com")
            @RequestParam(value = "email", required = false) String email,
            @Parameter(description = "비밀번호 (선택사항)", example = "newpassword123")
            @RequestParam(value = "password", required = false) String password,
            @Parameter(description = "세대 (선택사항)", example = "THIRTIES")
            @RequestParam(value = "generation", required = false) String generation) {
        String username = authentication.getName();
        UpdateUserRequest request = new UpdateUserRequest(email, password, generation);
        UpdateUserResponse response = userService.updateUser(username, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "세대별 정보 조회", 
        description = "사용 가능한 세대별 정보를 조회합니다. 공개 API로 인증이 필요하지 않습니다."
    )
    @ApiResponse(responseCode = "200", description = "세대별 정보 조회 성공",
        content = @Content(
            mediaType = "application/json",
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                name = "성공 응답",
                value = "{\n  \"generations\": {\n    \"TEENS\": {\n      \"displayName\": \"10대\",\n      \"colorCode\": \"#FFD700\"\n    },\n    \"TWENTIES\": {\n      \"displayName\": \"20대\",\n      \"colorCode\": \"#40E0D0\"\n    },\n    \"THIRTIES\": {\n      \"displayName\": \"30대\",\n      \"colorCode\": \"#000080\"\n    },\n    \"FORTIES_PLUS\": {\n      \"displayName\": \"40대+\",\n      \"colorCode\": \"#FFD700\"\n    }\n  }\n}"
            )
        ))
    @GetMapping("/generations")
    public ResponseEntity<Map<String, Object>> getGenerations() {
        Map<String, Object> response = new HashMap<>();
        
        Map<String, Object> generations = new HashMap<>();
        for (User.Generation generation : User.Generation.values()) {
            Map<String, String> genInfo = new HashMap<>();
            genInfo.put("displayName", generation.getDisplayName());
            genInfo.put("colorCode", generation.getColorCode());
            generations.put(generation.name(), genInfo);
        }
        
        response.put("generations", generations);
        return ResponseEntity.ok(response);
    }
}
