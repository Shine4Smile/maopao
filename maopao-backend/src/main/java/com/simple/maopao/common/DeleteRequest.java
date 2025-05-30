package com.simple.maopao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求参数
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 8335430415376850984L;

    private Long id;
}
