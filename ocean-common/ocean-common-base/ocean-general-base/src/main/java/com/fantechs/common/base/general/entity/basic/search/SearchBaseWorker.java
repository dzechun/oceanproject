package com.fantechs.common.base.general.entity.basic.search;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fantechs.common.base.dto.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
public class SearchBaseWorker extends BaseQuery implements Serializable {
    /**
     * 用户帐号
     */
    @ApiModelProperty(name = "userName", value = "用户帐号")
    private String userName;
    /**
     * 工号
     */
    @ApiModelProperty(name="workerCode",value = "工号")
    private String workerCode;
    /**
     * 仓库名称
     */
    @ApiModelProperty(name = "warehouseName",value = "仓库名称")
    private String warehouseName;
    /**
     * 状态(0无效，1有效)
     */
    @ApiModelProperty(name="status",value = "状态(0无效，1有效)")
    private Byte status;
    /**
     * 创建时间
     */
    @ApiModelProperty(name="createTime",value = "创建时间")
    @JSONField(format ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建用户名称
     */
    @ApiModelProperty(name = "createUserName", value = "创建用户名称")
    private String createUserName;
}
