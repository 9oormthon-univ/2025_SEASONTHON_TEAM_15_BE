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
@RequestMapping("/v1/api/users")
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
            @Parameter(description = "전화번호 (선택사항)", example = "010-9876-5432")
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @Parameter(description = "비밀번호 (선택사항)", example = "newpassword123")
            @RequestParam(value = "password", required = false) String password,
            @Parameter(description = "세대 (선택사항)", example = "THIRTIES")
            @RequestParam(value = "generation", required = false) String generation,
            @Parameter(description = "메모지 색깔 (선택사항)", example = "PINK")
            @RequestParam(value = "memoColor", required = false) String memoColor) {
        String username = authentication.getName();
        UpdateUserRequest request = new UpdateUserRequest(phoneNumber, password, generation, memoColor);
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
    
    @Operation(
        summary = "사용 가능한 메모지 색깔 목록 조회",
        description = "메모지에 사용할 수 있는 색깔 목록을 조회합니다. 공개 API로 인증이 필요하지 않습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "색깔 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "성공 응답",
                    value = "{\n  \"YELLOW\": {\n    \"displayName\": \"노란색\",\n    \"hexCode\": \"#FFD700\"\n  },\n  \"PINK\": {\n    \"displayName\": \"분홍색\",\n    \"hexCode\": \"#FFB6C1\"\n  }\n}"
                )
            ))
    })
    @GetMapping("/memo-colors")
    public ResponseEntity<Map<String, Object>> getMemoColors() {
        Map<String, Object> colors = new HashMap<>();
        for (User.MemoColor color : User.MemoColor.values()) {
            Map<String, String> colorInfo = new HashMap<>();
            colorInfo.put("displayName", color.getDisplayName());
            colorInfo.put("hexCode", color.getHexCode());
            colors.put(color.name(), colorInfo);
        }
        return ResponseEntity.ok(colors);
    }
    
    @Operation(
        summary = "가족 관계 목록 조회",
        description = "사용 가능한 가족 관계 목록을 조회합니다. 공개 API로 인증이 필요하지 않습니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "가족 관계 목록 조회 성공",
            content = @Content(
                mediaType = "application/json",
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    name = "성공 응답",
                    value = "{\n  \"FATHER\": {\n    \"displayName\": \"아빠\",\n    \"description\": \"가족의 아버지\"\n  },\n  \"MOTHER\": {\n    \"displayName\": \"엄마\",\n    \"description\": \"가족의 어머니\"\n  }\n}"
                )
            ))
    })
    @GetMapping("/family-relationships")
    public ResponseEntity<Map<String, Object>> getFamilyRelationships() {
        Map<String, Object> relationships = new HashMap<>();
        
        // SessionMember.FamilyRelationship enum을 사용하기 위해 임시로 하드코딩
        Map<String, String> father = new HashMap<>();
        father.put("displayName", "아빠");
        father.put("description", "가족의 아버지");
        relationships.put("FATHER", father);
        
        Map<String, String> mother = new HashMap<>();
        mother.put("displayName", "엄마");
        mother.put("description", "가족의 어머니");
        relationships.put("MOTHER", mother);
        
        Map<String, String> son = new HashMap<>();
        son.put("displayName", "아들");
        son.put("description", "가족의 아들");
        relationships.put("SON", son);
        
        Map<String, String> daughter = new HashMap<>();
        daughter.put("displayName", "딸");
        daughter.put("description", "가족의 딸");
        relationships.put("DAUGHTER", daughter);
        
        Map<String, String> brother = new HashMap<>();
        brother.put("displayName", "형/오빠");
        brother.put("description", "형제");
        relationships.put("BROTHER", brother);
        
        Map<String, String> sister = new HashMap<>();
        sister.put("displayName", "누나/언니");
        sister.put("description", "자매");
        relationships.put("SISTER", sister);
        
        Map<String, String> grandfather = new HashMap<>();
        grandfather.put("displayName", "할아버지");
        grandfather.put("description", "가족의 할아버지");
        relationships.put("GRANDFATHER", grandfather);
        
        Map<String, String> grandmother = new HashMap<>();
        grandmother.put("displayName", "할머니");
        grandmother.put("description", "가족의 할머니");
        relationships.put("GRANDMOTHER", grandmother);
        
        Map<String, String> uncle = new HashMap<>();
        uncle.put("displayName", "삼촌");
        uncle.put("description", "가족의 삼촌");
        relationships.put("UNCLE", uncle);
        
        Map<String, String> aunt = new HashMap<>();
        aunt.put("displayName", "이모/고모");
        aunt.put("description", "가족의 이모/고모");
        relationships.put("AUNT", aunt);
        
        Map<String, String> cousin = new HashMap<>();
        cousin.put("displayName", "사촌");
        cousin.put("description", "가족의 사촌");
        relationships.put("COUSIN", cousin);
        
        Map<String, String> other = new HashMap<>();
        other.put("displayName", "기타");
        other.put("description", "기타 가족 관계");
        relationships.put("OTHER", other);
        
        return ResponseEntity.ok(relationships);
    }
}
