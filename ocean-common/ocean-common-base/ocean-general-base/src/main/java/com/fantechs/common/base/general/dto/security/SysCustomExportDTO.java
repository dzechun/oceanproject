package com.fantechs.common.base.general.dto.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysCustomExportDTO implements Serializable {


    /**
     * 导出列
     */
    private String exportIndex;

    /**
     * 字段名
     */
    private String itemKey;

    /**
     * 字段中文名
     */
    private String itemName;

    /**
     * 导出列宽
     */
    private Integer exportColumnWidth;

}
