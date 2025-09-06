package com.goormthon.team15.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메모지 템플릿 응답")
public class MemoTemplateResponse {
    
    @Schema(description = "템플릿 ID", example = "HOUSE_PATTERN")
    private String id;
    
    @Schema(description = "템플릿 이름", example = "집 패턴")
    private String name;
    
    @Schema(description = "템플릿 설명", example = "집 모양이 그려진 메모지")
    private String description;
    
    @Schema(description = "색상 정보")
    private ColorInfo colors;
    
    @Schema(description = "패턴 타입", example = "HOUSE")
    private String patternType;
    
    @Schema(description = "미리보기 이미지 URL", example = "/images/memo-templates/house-pattern.png")
    private String previewImageUrl;
    
    public MemoTemplateResponse() {}
    
    public MemoTemplateResponse(String id, String name, String description, ColorInfo colors, 
                               String patternType, String previewImageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.colors = colors;
        this.patternType = patternType;
        this.previewImageUrl = previewImageUrl;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
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
    
    public ColorInfo getColors() {
        return colors;
    }
    
    public void setColors(ColorInfo colors) {
        this.colors = colors;
    }
    
    public String getPatternType() {
        return patternType;
    }
    
    public void setPatternType(String patternType) {
        this.patternType = patternType;
    }
    
    public String getPreviewImageUrl() {
        return previewImageUrl;
    }
    
    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }
    
    @Schema(description = "색상 정보")
    public static class ColorInfo {
        @Schema(description = "주 색상", example = "#E0F6FF")
        private String primary;
        
        @Schema(description = "보조 색상", example = "#FFD700")
        private String secondary;
        
        @Schema(description = "강조 색상", example = "#FFB6C1")
        private String accent;
        
        @Schema(description = "배경 색상", example = "#F0F8FF")
        private String background;
        
        public ColorInfo() {}
        
        public ColorInfo(String primary, String secondary, String accent, String background) {
            this.primary = primary;
            this.secondary = secondary;
            this.accent = accent;
            this.background = background;
        }
        
        // Getters and Setters
        public String getPrimary() {
            return primary;
        }
        
        public void setPrimary(String primary) {
            this.primary = primary;
        }
        
        public String getSecondary() {
            return secondary;
        }
        
        public void setSecondary(String secondary) {
            this.secondary = secondary;
        }
        
        public String getAccent() {
            return accent;
        }
        
        public void setAccent(String accent) {
            this.accent = accent;
        }
        
        public String getBackground() {
            return background;
        }
        
        public void setBackground(String background) {
            this.background = background;
        }
    }
}
