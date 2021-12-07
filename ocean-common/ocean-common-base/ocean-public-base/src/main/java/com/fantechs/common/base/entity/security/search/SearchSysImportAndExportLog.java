package com.fantechs.common.base.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2021/1/8.
 */
@Data
public class SearchSysImportAndExportLog extends BaseQuery implements Serializable {


    /**
     * 模块名
     */
    @ApiModelProperty(name="moduleNames",value = "模块名")
    private String moduleNames;

    /**
     * 类型(1-导入EXCEL 2-导出EXCEL)
     */
    @ApiModelProperty(name="type",value = "类型(1-导入EXCEL 2-导出EXCEL)")
    private Byte type;

    /**
     * 结果(0-失败 1-成功)
     */
    @ApiModelProperty(name="result",value = "结果(0-失败 1-成功)")
    private Byte result;

    /**
     * 操作用户
     */
    @ApiModelProperty(name="operatorUserName",value = "操作用户")
    private Long operatorUserName;








}
