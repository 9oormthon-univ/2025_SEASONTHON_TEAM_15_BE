package com.goormthon.team15.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "신년 다짐 응답")
public class ResolutionResponse {
    
    @Schema(description = "다짐 ID", example = "1")
    private Long id;
    
    @Schema(description = "다짐 제목", example = "칭찬하기")
    private String title;
    
    @Schema(description = "다짐 내용", example = "매일 가족에게 칭찬 한 마디씩 하기")
    private String content;
    
    @Schema(description = "다짐 대상자 정보")
    private TargetUserInfo targetUser;
    
    @Schema(description = "작성자 정보")
    private WriterInfo writer;
    
    @Schema(description = "메모 색상", example = "LIGHT_ORANGE")
    private String memoColor;
    
    @Schema(description = "익명 여부", example = "false")
    private Boolean isAnonymous;
    
    @Schema(description = "작성일시", example = "2025-01-01T00:00:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정일시", example = "2025-01-01T12:00:00")
    private LocalDateTime updatedAt;
    
    public ResolutionResponse() {}
    
    public ResolutionResponse(Long id, String title, String content, TargetUserInfo targetUser,
                             WriterInfo writer, String memoColor, Boolean isAnonymous,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.targetUser = targetUser;
        this.writer = writer;
        this.memoColor = memoColor;
        this.isAnonymous = isAnonymous;
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public TargetUserInfo getTargetUser() {
        return targetUser;
    }
    
    public void setTargetUser(TargetUserInfo targetUser) {
        this.targetUser = targetUser;
    }
    
    public WriterInfo getWriter() {
        return writer;
    }
    
    public void setWriter(WriterInfo writer) {
        this.writer = writer;
    }
    
    public String getMemoColor() {
        return memoColor;
    }
    
    public void setMemoColor(String memoColor) {
        this.memoColor = memoColor;
    }
    
    public Boolean getIsAnonymous() {
        return isAnonymous;
    }
    
    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
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
    
    @Schema(description = "다짐 대상자 정보")
    public static class TargetUserInfo {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;
        
        @Schema(description = "사용자명", example = "아빠")
        private String username;
        
        @Schema(description = "가족 관계", example = "FATHER")
        private String familyRelationship;
        
        @Schema(description = "세대", example = "FORTIES_PLUS")
        private String generation;
        
        public TargetUserInfo() {}
        
        public TargetUserInfo(Long id, String username, String familyRelationship, String generation) {
            this.id = id;
            this.username = username;
            this.familyRelationship = familyRelationship;
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
        
        public String getFamilyRelationship() {
            return familyRelationship;
        }
        
        public void setFamilyRelationship(String familyRelationship) {
            this.familyRelationship = familyRelationship;
        }
        
        public String getGeneration() {
            return generation;
        }
        
        public void setGeneration(String generation) {
            this.generation = generation;
        }
    }
    
    @Schema(description = "작성자 정보")
    public static class WriterInfo {
        @Schema(description = "사용자 ID", example = "2")
        private Long id;
        
        @Schema(description = "사용자명", example = "엄마")
        private String username;
        
        @Schema(description = "가족 관계", example = "MOTHER")
        private String familyRelationship;
        
        public WriterInfo() {}
        
        public WriterInfo(Long id, String username, String familyRelationship) {
            this.id = id;
            this.username = username;
            this.familyRelationship = familyRelationship;
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
        
        public String getFamilyRelationship() {
            return familyRelationship;
        }
        
        public void setFamilyRelationship(String familyRelationship) {
            this.familyRelationship = familyRelationship;
        }
    }
}
