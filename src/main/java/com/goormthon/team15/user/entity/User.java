package com.goormthon.team15.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false)
    private String username;
    
    @NotBlank
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다 (예: 010-1234-5678)")
    @Column(unique = true, nullable = false, name = "phone_number")
    private String phoneNumber;
    
    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    private String password;
    
    @Size(max = 50)
    @Column(name = "nickname")
    private String nickname;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "generation")
    private Generation generation;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;
    
//    @Enumerated(EnumType.STRING)
//    @Column(name = "memo_color", nullable = false)
//    private MemoColor memoColor = MemoColor.YELLOW;

    @Column(name = "has_family", nullable = false)
    private Boolean hasFamily = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // UserDetails 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public Generation getGeneration() {
        return generation;
    }
    
    public void setGeneration(Generation generation) {
        this.generation = generation;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
//    public MemoColor getMemoColor() {
//        return memoColor;
//    }
//
//    public void setMemoColor(MemoColor memoColor) {
//        this.memoColor = memoColor;
//    }
    
    public Boolean getHasFamily() {
        return hasFamily;
    }
    
    public void setHasFamily(Boolean hasFamily) {
        this.hasFamily = hasFamily;
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
    
    public enum Role {
        USER, ADMIN
    }
    
    public enum Generation {
        TEENS("10대", "#FFD700"),
        TWENTIES("20대", "#40E0D0"),
        THIRTIES("30대", "#000080"),
        FORTIES_PLUS("40대+", "#FFD700");
        
        private final String displayName;
        private final String colorCode;
        
        Generation(String displayName, String colorCode) {
            this.displayName = displayName;
            this.colorCode = colorCode;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getColorCode() {
            return colorCode;
        }
    }
    
//    public enum MemoColor {
//        YELLOW("노란색", "#FFD700"),
//        PINK("분홍색", "#FFB6C1"),
//        BLUE("파란색", "#87CEEB"),
//        GREEN("초록색", "#98FB98"),
//        ORANGE("주황색", "#FFA500"),
//        PURPLE("보라색", "#DDA0DD"),
//        RED("빨간색", "#FFB6C1"),
//        LIGHT_BLUE("하늘색", "#E0F6FF");
//
//        private final String displayName;
//        private final String hexCode;
//
//        MemoColor(String displayName, String hexCode) {
//            this.displayName = displayName;
//            this.hexCode = hexCode;
//        }
        
//        public String getDisplayName() {
//            return displayName;
//        }
        
//        public String getHexCode() {
//            return hexCode;
//        }
//    }
}