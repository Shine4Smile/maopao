package com.simple.maopao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 加入队伍请求体
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 2504335177090204127L;

    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 加入队伍密码
     */
    private String password;
}
