package com.goormthon.team15.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public class RegisterRequest {
    
    @Schema(description = "사용자명", example = "testuser", required = true)
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 50, message = "사용자명은 3자 이상 50자 이하여야 합니다")
    private String username;
    
    @Schema(description = "전화번호", example = "010-1234-5678", required = true)
    @NotBlank(message = "전화번호는 필수입니다")
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다 (예: 010-1234-5678)")
    private String phoneNumber;
    
    @Schema(description = "비밀번호", example = "password123", required = true)
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    private String password;
    
    
    @Schema(description = "세대", example = "TWENTIES", allowableValues = {"TEENS", "TWENTIES", "THIRTIES", "FORTIES_PLUS"})
    private String generation;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String username, String phoneNumber, String password, String generation) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.generation = generation;
    }
    
    // Getters and Setters
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public String getGeneration() {
        return generation;
    }
    
    public void setGeneration(String generation) {
        this.generation = generation;
    }
}
