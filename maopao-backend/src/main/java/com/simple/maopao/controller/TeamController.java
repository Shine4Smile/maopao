package com.simple.maopao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simple.maopao.common.BaseResponse;
import com.simple.maopao.common.ErrorCode;
import com.simple.maopao.common.ResultUtils;
import com.simple.maopao.exception.BusinessException;
import com.simple.maopao.model.domain.Team;
import com.simple.maopao.model.domain.User;
import com.simple.maopao.model.dto.TeamQuery;
import com.simple.maopao.model.request.TeamAddRequest;
import com.simple.maopao.model.request.TeamJoinRequest;
import com.simple.maopao.model.request.TeamQuitRequest;
import com.simple.maopao.model.request.TeamUpdateRequest;
import com.simple.maopao.model.vo.TeamUserVO;
import com.simple.maopao.service.TeamService;
import com.simple.maopao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author Simple
 */
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@Slf4j
public class TeamController {

    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    /**
     * 创建队伍
     *
     * @param teamAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    /**
     * 更新队伍信息
     *
     * @param updateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest updateRequest, HttpServletRequest request) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean save = teamService.updateTeam(updateRequest, loginUser);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍失败！");
        }
        return ResultUtils.success(true);
    }

    /**
     * 删除队伍
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean res = teamService.removeById(id);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍失败！");
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据队伍id查询队伍信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getById")
    public BaseResponse<Team> getTeamById(@RequestParam long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

    /**
     * 查询队伍列表
     *
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> resList = teamService.listTeams(teamQuery, isAdmin);
        return ResultUtils.success(resList);
    }

    /**
     * 分页查询队伍列表
     *
     * @param teamQuery
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        Page<Team> page = new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(resPage);
    }

    /**
     * 加入队伍
     *
     * @param joinRequest
     * @return
     */
    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest joinRequest, HttpServletRequest request) {
        if (joinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.joinTeam(joinRequest, loginUser);
        return ResultUtils.success(res);
    }

    /**
     * 退出队伍
     *
     * @param quitRequest
     * @return
     */
    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest quitRequest, HttpServletRequest request) {
        if (quitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean res = teamService.quitTeam(quitRequest, loginUser);
        return ResultUtils.success(res);
    }
}
