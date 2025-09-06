package com.goormthon.team15.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "사용자 정보 수정 요청")
public class UpdateUserRequest {
    
    @Schema(description = "전화번호", example = "010-9876-5432")
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다 (예: 010-1234-5678)")
    private String phoneNumber;
    
    @Schema(description = "비밀번호", example = "newpassword123")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다")
    private String password;
    
    @Schema(description = "세대", example = "THIRTIES", allowableValues = {"TEENS", "TWENTIES", "THIRTIES", "FORTIES_PLUS"})
    private String generation;
    
    public UpdateUserRequest() {}
    
    public UpdateUserRequest(String phoneNumber, String password, String generation) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.generation = generation;
    }
    
    // Getters and Setters
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
