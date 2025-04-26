package com.simple.maopao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.maopao.mapper.UserTeamMapper;
import com.simple.maopao.model.domain.UserTeam;
import com.simple.maopao.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
 * @author Simple
 * @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
 * @createDate 2025-04-26 17:02:56
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




