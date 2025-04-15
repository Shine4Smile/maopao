package com.simple.maopao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.maopao.model.domain.Tag;
import com.simple.maopao.service.TagService;
import com.simple.maopao.mapper.TagMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author Simple
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2025-04-10 23:59:26
*/
@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




