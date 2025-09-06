package com.goormthon.team15.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "메모 생성 요청")
public class CreateMemoRequest {
    
    @Schema(description = "메모 제목", example = "오늘 할 일", required = true)
    @NotBlank(message = "메모 제목은 필수입니다")
    @Size(min = 1, max = 200, message = "메모 제목은 1자 이상 200자 이하여야 합니다")
    private String title;
    
    @Schema(description = "메모 내용", example = "1. 운동하기\n2. 책 읽기\n3. 친구 만나기")
    @Size(max = 2000, message = "메모 내용은 2000자 이하여야 합니다")
    private String content;
    
    public CreateMemoRequest() {}
    
    public CreateMemoRequest(String title, String content) {
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
