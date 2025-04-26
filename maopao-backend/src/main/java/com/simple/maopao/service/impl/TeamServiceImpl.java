package com.simple.maopao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.maopao.mapper.TeamMapper;
import com.simple.maopao.model.domain.Team;
import com.simple.maopao.service.TeamService;
import org.springframework.stereotype.Service;

/**
 * @author Simple
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2025-04-26 17:02:56
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

}




