package com.example.hotrohoctapbackend.DTO.AdminV3.Comment;

import java.util.List;

public class BulkStatusUpdateRequest {
    private List<Integer> ids;  // Danh sách các ID bình luận
    private String status;

    public BulkStatusUpdateRequest() {
    }

    public BulkStatusUpdateRequest(List<Integer> ids, String status) {
        this.ids = ids;
        this.status = status;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
