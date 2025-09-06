package com.goormthon.team15.family.dto;

import com.goormthon.team15.family.entity.FamilySession;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "가족 세션 응답")
public class FamilySessionResponse {
    
    @Schema(description = "세션 ID", example = "1")
    private Long id;
    
    @Schema(description = "세션 이름", example = "우리 가족의 2025년 다짐")
    private String name;
    
    @Schema(description = "세션 설명", example = "가족 모두의 새해 다짐을 공유해보세요!")
    private String description;
    
    @Schema(description = "초대 코드", example = "ABC123")
    private String inviteCode;
    
    @Schema(description = "생성자 정보")
    private CreatorInfo creator;
    
    @Schema(description = "세션 상태", example = "ACTIVE")
    private String status;
    
    @Schema(description = "최대 참여자 수", example = "10")
    private Integer maxMembers;
    
    @Schema(description = "현재 참여자 수", example = "3")
    private Integer currentMemberCount;
    
    @Schema(description = "참여자 목록")
    private List<MemberInfo> members;
    
    @Schema(description = "생성일시", example = "2025-01-01T00:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일시", example = "2025-01-01T12:00:00")
    private LocalDateTime updatedAt;
    
    public FamilySessionResponse() {}
    
    public FamilySessionResponse(Long id, String name, String description, String inviteCode,
                                CreatorInfo creator, String status, Integer maxMembers,
                                Integer currentMemberCount, List<MemberInfo> members,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.inviteCode = inviteCode;
        this.creator = creator;
        this.status = status;
        this.maxMembers = maxMembers;
        this.currentMemberCount = currentMemberCount;
        this.members = members;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getInviteCode() {
        return inviteCode;
    }
    
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
    
    public CreatorInfo getCreator() {
        return creator;
    }
    
    public void setCreator(CreatorInfo creator) {
        this.creator = creator;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
    }
    
    public Integer getCurrentMemberCount() {
        return currentMemberCount;
    }
    
    public void setCurrentMemberCount(Integer currentMemberCount) {
        this.currentMemberCount = currentMemberCount;
    }
    
    public List<MemberInfo> getMembers() {
        return members;
    }
    
    public void setMembers(List<MemberInfo> members) {
        this.members = members;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Schema(description = "생성자 정보")
    public static class CreatorInfo {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;
        
        @Schema(description = "사용자명", example = "testuser")
        private String username;
        
        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;
        
        @Schema(description = "세대", example = "TWENTIES")
        private String generation;
        
        public CreatorInfo() {}
        
        public CreatorInfo(Long id, String username, String phoneNumber, String generation) {
            this.id = id;
            this.username = username;
            this.phoneNumber = phoneNumber;
            this.generation = generation;
        }
        
        // Getters and Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getPhoneNumber() {
            return phoneNumber;
        }
        
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        
        public String getGeneration() {
            return generation;
        }
        
        public void setGeneration(String generation) {
            this.generation = generation;
        }
    }
    
    @Schema(description = "멤버 정보")
    public static class MemberInfo {
        @Schema(description = "멤버 ID", example = "1")
        private Long id;
        
        @Schema(description = "사용자 정보")
        private CreatorInfo user;
        
        @Schema(description = "역할", example = "MEMBER")
        private String role;
        
        @Schema(description = "상태", example = "ACTIVE")
        private String status;
        
        @Schema(description = "참여일시", example = "2025-01-01T00:00:00")
        private LocalDateTime joinedAt;
        
        public MemberInfo() {}
        
        public MemberInfo(Long id, CreatorInfo user, String role, String status, LocalDateTime joinedAt) {
            this.id = id;
            this.user = user;
            this.role = role;
            this.status = status;
            this.joinedAt = joinedAt;
        }
        
        // Getters and Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public CreatorInfo getUser() {
            return user;
        }
        
        public void setUser(CreatorInfo user) {
            this.user = user;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public LocalDateTime getJoinedAt() {
            return joinedAt;
        }
        
        public void setJoinedAt(LocalDateTime joinedAt) {
            this.joinedAt = joinedAt;
        }
    }
}
