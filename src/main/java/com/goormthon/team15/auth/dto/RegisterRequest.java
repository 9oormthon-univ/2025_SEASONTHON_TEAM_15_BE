package com.goormthon.team15.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public class RegisterRequest {
    
    @Schema(description = "사용자명", example = "testuser", required = true)
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 50, message = "사용자명은 3자 이상 50자 이하여야 합니다")
    private String username;
    
    @Schema(description = "이메일 주소", example = "test@example.com", required = true)
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    @Schema(description = "비밀번호", example = "password123", required = true)
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    private String password;
    
    
    @Schema(description = "세대", example = "TWENTIES", allowableValues = {"TEENS", "TWENTIES", "THIRTIES", "FORTIES_PLUS"})
    private String generation;
    
    public RegisterRequest() {}
    
    public RegisterRequest(String username, String email, String password, String generation) {
        this.username = username;
        this.email = email;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
