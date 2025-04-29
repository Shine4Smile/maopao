package com.simple.maopao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 推出队伍请求体
 */
@Data
public class TeamQuitRequest implements Serializable {


    private static final long serialVersionUID = 1064097485609212200L;
    /**
     * 队伍id
     */
    private Long teamId;

}
