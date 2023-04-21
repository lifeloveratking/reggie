package com.lin.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 多条件分页查询
 */
@Data
public class PageQueryDto {

    int page;
    int pageSize;
    String number;
    String beginTime;
    String endTime;
}