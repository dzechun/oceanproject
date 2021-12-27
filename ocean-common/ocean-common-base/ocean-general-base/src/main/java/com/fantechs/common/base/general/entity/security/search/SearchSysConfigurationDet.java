package com.fantechs.common.base.general.entity.security.search;

import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lfz on 2021/12/9.
 */
@Data
public class SearchSysConfigurationDet extends BaseQuery implements Serializable {
    /**
     * 配置明细Id
     */
    @ApiModelProperty(name="configurationDetId",value = "配置明细Id")
    private Long configurationDetId;

    /**
     * 配置Id
     */
    @ApiModelProperty(name="configurationId",value = "配置Id")
    private Long configurationId;

    /**
     * 源字段
     */
    @ApiModelProperty(name="sourceField",value = "源字段")
    private String sourceField;

    /**
     * 目标字段
     */
    @ApiModelProperty(name="targetField",value = "目标字段")
    private String targetField;

    /**
     * 条件范围（0、且 1、或）
     */
    @ApiModelProperty(name="isCondition",value = "条件范围（0、且 1、或）")
    private Byte isCondition;

    /**
     * 过滤范围（0-等于 1-不等于 2-大于  3-大于等于 4-小于 5-小于等于 6-为空  7-不为空 8-包含 9-不包含 10-模糊）
     */
    @ApiModelProperty(name="isScope",value = "过滤范围（0-等于 1-不等于 2-大于  3-大于等于 4-小于 5-小于等于 6-为空  7-不为空 8-包含 9-不包含 10-模糊）")
    private Byte isScope;

    /**
     * 过滤值
     */
    @ApiModelProperty(name="filterValue",value = "过滤值")
    private String filterValue;

    /**
     * 固定值
     */
    @ApiModelProperty(name="fixedValue",value = "固定值")
    private String fixedValue;

    private static final long serialVersionUID = 1L;
}