package com.simple.maopao.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.maopao.common.ErrorCode;
import com.simple.maopao.exception.BusinessException;
import com.simple.maopao.mapper.TeamMapper;
import com.simple.maopao.model.domain.Team;
import com.simple.maopao.model.domain.User;
import com.simple.maopao.model.domain.UserTeam;
import com.simple.maopao.model.dto.TeamQuery;
import com.simple.maopao.model.enums.TeamStatusEnum;
import com.simple.maopao.model.request.TeamJoinRequest;
import com.simple.maopao.model.request.TeamQuitRequest;
import com.simple.maopao.model.request.TeamUpdateRequest;
import com.simple.maopao.model.vo.TeamUserVO;
import com.simple.maopao.model.vo.UserVO;
import com.simple.maopao.service.TeamService;
import com.simple.maopao.service.UserService;
import com.simple.maopao.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Simple
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2025-04-26 17:02:56
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamService userTeamService;
    @Resource
    private UserService userService;

    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        // 请求参数是否为空
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 是否登录，未登录不能创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final long userId = loginUser.getId();
        // 队伍人数 > 1 且 <= 12
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 12) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求！");
        }
        // 标题长度小于20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题长度过长！");
        }
        // 队伍描述长度不超过512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述长度过长！");
        }
        // 队伍状态
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum enumByValue = TeamStatusEnum.getEnumByValue(status);
        if (enumByValue == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求！");
        }
        // 如果队伍是加密状态，密码长度不能超过32
        if (enumByValue.equals(TeamStatusEnum.SECRET) && (StringUtils.isBlank(team.getPassword()) || team.getPassword().length() > 32)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不满足要求！");
        }
        // 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍过期时间小于当前时间！");
        }
        // 每个用户最多创建5个队伍，todo 可能同时创建100个队伍，加锁
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建5个队伍！");
        }
        // 插入队伍信息到队伍表。将id设为null，使用自增id
        team.setUserId(userId);
        boolean res = this.save(team);
        Long teamId = team.getId();
        if (!res || teamId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败！");
        }
        // 插入信息到用户队伍关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        res = userTeamService.save(userTeam);
        if (!res) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败！");
        }
        return teamId;
    }

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        // 组合查询条件
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            Integer status = teamQuery.getStatus();
            TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
            if (teamStatusEnum == null) {
                teamStatusEnum = TeamStatusEnum.PUBLIC;
            }
            if (!isAdmin && !teamStatusEnum.equals(TeamStatusEnum.PUBLIC)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status", teamStatusEnum.getValue());

            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
        }
        // 不展示已过期的队伍
        queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(queryWrapper);
        if (CollUtil.isEmpty(teamList)) {
            return new ArrayList<>();
        }
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        // 关联查询用户信息
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        // todo 关联已加入队伍用户信息
        return teamUserVOList;
    }

    /**
     * 更新队伍信息
     *
     * @param updateRequest
     * @param loginUser
     * @return
     */
    @Override
    public boolean updateTeam(TeamUpdateRequest updateRequest, User loginUser) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = updateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team oldTeam = this.getById(id);
        if (oldTeam == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 管理员或队伍创建者可修改
        if (!oldTeam.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 如果修改之后队伍状态是加密，必须有密码，且密码长度不能超过32
        TeamStatusEnum enumByValue = TeamStatusEnum.getEnumByValue(updateRequest.getStatus());
        if (!TeamStatusEnum.getEnumByValue(oldTeam.getStatus()).equals(TeamStatusEnum.SECRET) && enumByValue.equals(TeamStatusEnum.SECRET) && (StringUtils.isBlank(updateRequest.getPassword()) || updateRequest.getPassword().length() > 32)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍密码不满足要求！");
        }
        // 如果修改后队伍状态是公开，那么密码必须为空
        if (enumByValue.equals(TeamStatusEnum.PUBLIC)) {
            updateRequest.setPassword("");
        }
        // todo 若修改值和原值一致，则不创建数据库连接进行更新
        Team team = new Team();
        BeanUtils.copyProperties(updateRequest, team);
        return this.updateById(team);
    }

    /**
     * 加入队伍
     *
     * @param joinRequest
     * @return
     */
    @Override
    public boolean joinTeam(TeamJoinRequest joinRequest, User loginUser) {
        if (joinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = joinRequest.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取当前队伍信息
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在！");
        }
        // 只能加入未过期的队伍
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期！");
        }
        // 不能加入私有队伍
        Integer status = team.getStatus();
        TeamStatusEnum enumByValue = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(enumByValue)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "禁止加入私有队伍！");
        }
        // 加入加密队伍，密码必须要正确
        String joinPwd = joinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(enumByValue) && (StringUtils.isBlank(joinPwd) || !team.getPassword().equals(joinPwd))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入队伍密码错误！");
        }
        long userId = loginUser.getId();
        // 用户已加入队伍数量，最多只能加入5个队伍
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        long joinedTeamNum = userTeamService.count(queryWrapper);
        if (joinedTeamNum >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建和加入5个队伍！");
        }
        // 不能重复加入已加入的队伍
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("teamId", teamId);
        long hasUserJoin = userTeamService.count(queryWrapper);
        if (hasUserJoin > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已在队伍中，禁止重复加入！");
        }
        // 队伍中已加入用户数量，只能加入人数未满的队伍
        long teamUserNum = this.countTeamUserByTeamId(teamId);
        if (teamUserNum >= team.getMaxNum()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数已满！");
        }
        // 用户-队伍关系表插入新纪录
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        return userTeamService.save(userTeam);
    }

    /**
     * 退出队伍
     *
     * @param quitRequest
     * @return
     */
    @Override
    public boolean quitTeam(TeamQuitRequest quitRequest, User loginUser) {
        if (quitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = quitRequest.getTeamId();
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在！");
        }
        long userId = loginUser.getId();
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setUserId(userId);
        queryUserTeam.setTeamId(teamId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(queryUserTeam);
        long count = userTeamService.count(queryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未加入队伍，退出失败！");
        }
        long teamHasJoinNum = countTeamUserByTeamId(teamId);
        // 队伍只剩一人，解散队伍
        if (teamHasJoinNum == 1) {
            // 删除队伍信息
            this.removeById(teamId);
        } else {
            // 判断是否为队长(创建人)，如果是则退出后将队长顺位转给下一个加入队伍的人
            if (team.getUserId().equals(userId)) {
                //
                QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                userTeamQueryWrapper.eq("teamId", teamId);
                userTeamQueryWrapper.last(" order by id desc limit 2");
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                if (CollUtil.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                UserTeam nextUser = userTeamList.get(0);
                Long nextTeamLeadUserId = nextUser.getUserId();
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeadUserId);
                boolean res = this.updateById(updateTeam);
                if (!res) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
            }
        }
        // 删除用户-队伍关系
        return userTeamService.remove(queryWrapper);
    }

    /**
     * 队伍队伍id统计对应队伍中加入的用户数量
     *
     * @param teamId
     * @return
     */
    private long countTeamUserByTeamId(long teamId) {
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        return userTeamService.count(queryWrapper);
    }
}




