package com.paparazzi.antifakenews.dto;

import com.paparazzi.antifakenews.model.enums.NewsStatus;

public class VotesSummaryDto {
    private long fakeCount;
    private long notFakeCount;
    private NewsStatus status;

    public long getFakeCount() { return fakeCount; }
    public void setFakeCount(long fakeCount) { this.fakeCount = fakeCount; }
    public long getNotFakeCount() { return notFakeCount; }
    public void setNotFakeCount(long notFakeCount) { this.notFakeCount = notFakeCount; }
    public NewsStatus getStatus() { return status; }
    public void setStatus(NewsStatus status) { this.status = status; }
}
