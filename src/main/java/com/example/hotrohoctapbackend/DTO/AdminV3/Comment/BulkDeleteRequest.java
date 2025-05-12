package com.example.hotrohoctapbackend.DTO.AdminV3.Comment;

import java.util.List;

public class BulkDeleteRequest {
    private List<Integer> ids;

    public BulkDeleteRequest() {
    }

    public BulkDeleteRequest(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
