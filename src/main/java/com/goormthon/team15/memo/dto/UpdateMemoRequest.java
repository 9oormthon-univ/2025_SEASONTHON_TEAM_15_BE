package com.goormthon.team15.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "메모 수정 요청")
public class UpdateMemoRequest {
    
    @Schema(description = "메모 제목", example = "수정된 제목")
    @Size(min = 1, max = 200, message = "메모 제목은 1자 이상 200자 이하여야 합니다")
    private String title;
    
    @Schema(description = "메모 내용", example = "수정된 내용")
    @Size(max = 2000, message = "메모 내용은 2000자 이하여야 합니다")
    private String content;
    
    public UpdateMemoRequest() {}
    
    public UpdateMemoRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
    
    // Getters and Setters
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
}
