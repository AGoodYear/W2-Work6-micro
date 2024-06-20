package com.ivmiku.w6r1.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 用户实体类
 * @author Aurora
 */
@Data
@JSONType(alphabetic = false)
@TableName("user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String username;
    private String password;
    private String salt;
    private String avatarUrl;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
