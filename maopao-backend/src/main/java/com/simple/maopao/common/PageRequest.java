package com.simple.maopao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {


    private static final long serialVersionUID = -5935719292911832150L;
    /**
     * 分页大小
     */
    protected int pageSize = 10;

    /**
     * 页数
     */
    protected int pageNum = 1;
}
