package com.goormthon.team15.memo.entity;

import com.goormthon.team15.family.entity.FamilySession;
import com.goormthon.team15.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "memos")
public class Memo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 1, max = 200)
    @Column(nullable = false)
    private String title;
    
    @Size(max = 2000)
    @Column(length = 2000)
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_session_id", nullable = false)
    private FamilySession familySession;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;
    
    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "memo_color")
    private MemoColor memoColor = MemoColor.LIGHT_PINK;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemoStatus status = MemoStatus.ACTIVE;
    
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
    
    // 생성자
    public Memo() {}
    
    public Memo(String title, String content, User user, FamilySession familySession, User targetUser) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.familySession = familySession;
        this.targetUser = targetUser;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public FamilySession getFamilySession() {
        return familySession;
    }
    
    public void setFamilySession(FamilySession familySession) {
        this.familySession = familySession;
    }
    
    public User getTargetUser() {
        return targetUser;
    }
    
    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
    
    public Boolean getIsAnonymous() {
        return isAnonymous;
    }
    
    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
    
    public MemoColor getMemoColor() {
        return memoColor;
    }
    
    public void setMemoColor(MemoColor memoColor) {
        this.memoColor = memoColor;
    }
    
    public MemoStatus getStatus() {
        return status;
    }
    
    public void setStatus(MemoStatus status) {
        this.status = status;
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
    
    // 비즈니스 메서드
    public boolean isActive() {
        return status == MemoStatus.ACTIVE;
    }
    
    public boolean isOwner(User user) {
        return this.user.getId().equals(user.getId());
    }
    
    public boolean isTarget(User user) {
        return this.targetUser.getId().equals(user.getId());
    }
    
    public boolean canView(User user) {
        return isOwner(user) || isTarget(user) || !isAnonymous;
    }
    
    public enum MemoStatus {
        ACTIVE("활성"),
        DELETED("삭제됨");
        
        private final String displayName;
        
        MemoStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum MemoColor {
        LIGHT_PINK("#FFB6C1"),
        PEACH("#FFCCCB"),
        LIGHT_YELLOW("#FFFFE0"),
        LIGHT_GREEN("#90EE90"),
        LIGHT_BLUE_GREEN("#AFEEEE"),
        TEAL("#20B2AA"),
        LIGHT_BLUE("#ADD8E6"),
        LAVENDER("#E6E6FA"),
        PURPLE("#DDA0DD");
        
        private final String hexCode;
        
        MemoColor(String hexCode) {
            this.hexCode = hexCode;
        }
        
        public String getHexCode() {
            return hexCode;
        }
    }
    
}
