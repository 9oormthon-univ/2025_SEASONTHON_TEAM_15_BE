package com.goormthon.team15.family.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "D-Day 계산 응답")
public class DdayResponse {
    
    @Schema(description = "목표 날짜", example = "2026-01-01")
    private LocalDate targetDate;
    
    @Schema(description = "남은 일수", example = "24")
    private long daysLeft;
    
    @Schema(description = "D-Day 메시지", example = "2026년 D-24")
    private String ddayMessage;
    
    @Schema(description = "목표 연도", example = "2026")
    private int targetYear;
    
    public DdayResponse() {}
    
    public DdayResponse(LocalDate targetDate, long daysLeft, String ddayMessage, int targetYear) {
        this.targetDate = targetDate;
        this.daysLeft = daysLeft;
        this.ddayMessage = ddayMessage;
        this.targetYear = targetYear;
    }
    
    // Getters and Setters
    public LocalDate getTargetDate() {
        return targetDate;
    }
    
    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
    
    public long getDaysLeft() {
        return daysLeft;
    }
    
    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }
    
    public String getDdayMessage() {
        return ddayMessage;
    }
    
    public void setDdayMessage(String ddayMessage) {
        this.ddayMessage = ddayMessage;
    }
    
    public int getTargetYear() {
        return targetYear;
    }
    
    public void setTargetYear(int targetYear) {
        this.targetYear = targetYear;
    }
}
