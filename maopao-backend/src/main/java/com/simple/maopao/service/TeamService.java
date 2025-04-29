package com.simple.maopao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.maopao.model.domain.Team;
import com.simple.maopao.model.domain.User;
import com.simple.maopao.model.dto.TeamQuery;
import com.simple.maopao.model.request.TeamJoinRequest;
import com.simple.maopao.model.request.TeamQuitRequest;
import com.simple.maopao.model.request.TeamUpdateRequest;
import com.simple.maopao.model.vo.TeamUserVO;

import java.util.List;

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

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍信息
     *
     * @param updateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest updateRequest, User loginUser);

    /**
     * 加入队伍
     *
     * @param joinRequest
     * @return
     */
    boolean joinTeam(TeamJoinRequest joinRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param quitRequest
     * @return
     */
    boolean quitTeam(TeamQuitRequest quitRequest, User loginUser);
}
