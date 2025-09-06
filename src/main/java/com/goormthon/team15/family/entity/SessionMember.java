package com.goormthon.team15.family.entity;

import com.goormthon.team15.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_members")
public class SessionMember {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_session_id", nullable = false)
    private FamilySession familySession;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role = MemberRole.MEMBER;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status = MemberStatus.ACTIVE;
    
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    
    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;
    
    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
        lastActiveAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastActiveAt = LocalDateTime.now();
    }
    
    // 생성자
    public SessionMember() {}
    
    public SessionMember(FamilySession familySession, User user, MemberRole role) {
        this.familySession = familySession;
        this.user = user;
        this.role = role;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public FamilySession getFamilySession() {
        return familySession;
    }
    
    public void setFamilySession(FamilySession familySession) {
        this.familySession = familySession;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public MemberRole getRole() {
        return role;
    }
    
    public void setRole(MemberRole role) {
        this.role = role;
    }
    
    public MemberStatus getStatus() {
        return status;
    }
    
    public void setStatus(MemberStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
    
    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }
    
    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
    
    // 비즈니스 메서드
    public boolean isAdmin() {
        return role == MemberRole.ADMIN;
    }
    
    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }
    
    public enum MemberRole {
        ADMIN("관리자"),
        MEMBER("멤버");
        
        private final String displayName;
        
        MemberRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum MemberStatus {
        ACTIVE("활성"),
        INACTIVE("비활성"),
        LEFT("탈퇴");
        
        private final String displayName;
        
        MemberStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
