package com.simple.maopao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 解散队伍请求体
 */
@Data
public class TeamDelRequest implements Serializable {


    private static final long serialVersionUID = -4515407914752033284L;
    /**
     * 队伍id
     */
    private Long teamId;

}
