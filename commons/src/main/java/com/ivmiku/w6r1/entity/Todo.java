package com.ivmiku.w6r1.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Todo implements Serializable {
    private String id;
    private String userId;
    private String content;
    private String status = "pending";
}
