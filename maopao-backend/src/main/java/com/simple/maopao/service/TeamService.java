package com.simple.maopao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.maopao.model.domain.Team;
import com.simple.maopao.model.domain.User;

/**
 * @author Simple
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2025-04-26 17:02:56
 */
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     *
     * @param team
     * @return
     */
    long addTeam(Team team, User loginUser);

}
