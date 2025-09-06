package com.goormthon.team15.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 정보 수정 요청")
public class UpdateUserRequest {
    
    @Schema(description = "이메일 주소", example = "newemail@example.com")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    @Schema(description = "비밀번호", example = "newpassword123")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    private String password;
    
    @Schema(description = "세대", example = "THIRTIES", allowableValues = {"TEENS", "TWENTIES", "THIRTIES", "FORTIES_PLUS"})
    private String generation;
    
    public UpdateUserRequest() {}
    
    public UpdateUserRequest(String email, String password, String generation) {
        this.email = email;
        this.password = password;
        this.generation = generation;
    }
    
    // Getters and Setters
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
