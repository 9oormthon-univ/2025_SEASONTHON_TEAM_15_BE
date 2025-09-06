package com.goormthon.team15.family.entity;

import com.goormthon.team15.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "family_sessions")
public class FamilySession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String name;
    
    @Size(max = 500)
    @Column(length = 500)
    private String description;
    
    @NotBlank
    @Size(min = 6, max = 20)
    @Column(unique = true, nullable = false, name = "invite_code")
    private String inviteCode;
    
    @Size(min = 4, max = 20)
    @Column(name = "session_password")
    private String sessionPassword;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.ACTIVE;
    
    @Column(name = "max_members")
    private Integer maxMembers = 10; // 기본 최대 10명
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "familySession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SessionMember> members = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 생성자
    public FamilySession() {}
    
    public FamilySession(String name, String description, String inviteCode, String sessionPassword, User creator) {
        this.name = name;
        this.description = description;
        this.inviteCode = inviteCode;
        this.sessionPassword = sessionPassword;
        this.creator = creator;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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
    
    public String getInviteCode() {
        return inviteCode;
    }
    
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
    
    public String getSessionPassword() {
        return sessionPassword;
    }
    
    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    public SessionStatus getStatus() {
        return status;
    }
    
    public void setStatus(SessionStatus status) {
        this.status = status;
    }
    
    public Integer getMaxMembers() {
        return maxMembers;
    }
    
    public void setMaxMembers(Integer maxMembers) {
        this.maxMembers = maxMembers;
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
    
    public List<SessionMember> getMembers() {
        return members;
    }
    
    public void setMembers(List<SessionMember> members) {
        this.members = members;
    }
    
    // 비즈니스 메서드
    public boolean isFull() {
        return members.size() >= maxMembers;
    }
    
    public boolean isCreator(User user) {
        return creator.getId().equals(user.getId());
    }
    
    public boolean hasMember(User user) {
        return members.stream()
                .anyMatch(member -> member.getUser().getId().equals(user.getId()));
    }
    
    public enum SessionStatus {
        ACTIVE("활성"),
        INACTIVE("비활성"),
        COMPLETED("완료");
        
        private final String displayName;
        
        SessionStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
