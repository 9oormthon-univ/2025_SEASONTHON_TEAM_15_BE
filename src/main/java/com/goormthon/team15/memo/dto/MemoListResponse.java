package com.goormthon.team15.memo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "메모 목록 응답")
public class MemoListResponse {
    
    @Schema(description = "메모 목록")
    private List<MemoResponse> memos;
    
    @Schema(description = "전체 메모 수", example = "10")
    private long totalCount;
    
    @Schema(description = "현재 페이지", example = "1")
    private int currentPage;
    
    @Schema(description = "전체 페이지 수", example = "5")
    private int totalPages;
    
    @Schema(description = "페이지당 메모 수", example = "10")
    private int pageSize;
    
    public MemoListResponse() {}
    
    public MemoListResponse(List<MemoResponse> memos, long totalCount, int currentPage, int totalPages, int pageSize) {
        this.memos = memos;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
    }
    
    // Getters and Setters
    public List<MemoResponse> getMemos() {
        return memos;
    }
    
    public void setMemos(List<MemoResponse> memos) {
        this.memos = memos;
    }
    
    public long getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}


