package com.ivmiku.w6r1.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("todo")
public class Todo implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String userId;
    private String content;
    private String status;
}
