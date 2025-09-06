package com.goormthon.team15.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "에러 응답")
public class ErrorResponse {
    
    @Schema(description = "에러 코드", example = "USER_ALREADY_EXISTS")
    private String code;
    
    @Schema(description = "에러 메시지", example = "이미 존재하는 사용자입니다")
    private String message;
    
    @Schema(description = "발생 시간", example = "2025-09-06T16:51:23")
    private LocalDateTime timestamp;
    
    @Schema(description = "상세 에러 목록")
    private List<String> details;
    
    @Schema(description = "요청 경로", example = "/api/auth/register")
    private String path;
    
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponse(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public ErrorResponse(String code, String message, String path) {
        this(code, message);
        this.path = path;
    }
    
    public ErrorResponse(String code, String message, List<String> details, String path) {
        this(code, message, path);
        this.details = details;
    }
    
    // Getters and Setters
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public List<String> getDetails() {
        return details;
    }
    
    public void setDetails(List<String> details) {
        this.details = details;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
}
