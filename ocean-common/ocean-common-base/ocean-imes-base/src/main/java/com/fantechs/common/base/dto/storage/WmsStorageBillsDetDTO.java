package com.fantechs.common.base.dto.storage;

import com.fantechs.common.base.entity.storage.WmsStorageBillsDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;

@Data
public class WmsStorageBillsDetDTO extends WmsStorageBillsDet implements Serializable {
    /**
    * 创建用户名称
    */
    @Transient
    @ApiModelProperty(value = "创建用户名称",example = "创建用户名称")
    @Excel(name = "创建用户名称")
    private String createUserName;
    /**
    * 修改用户名称
    */
    @Transient
    @ApiModelProperty(value = "修改用户名称",example = "修改用户名称")
    @Excel(name = "修改用户名称")
    private String modifiedUserName;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(value = "物料编码",example = "物料编码")
    @Excel(name = "物料编码")
    private String materialCode;
    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(value = "物料描述",example = "物料描述")
    @Excel(name = "物料描述")
    private String materialDesc;
    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(value = "产品型号",example = "产品型号")
    @Excel(name = "产品型号")
    private String productModelName;
    /**
     * 单位
     */
    @Transient
    @ApiModelProperty(value = "单位",example = "单位")
    @Excel(name = "单位")
    private String unit;
    /**
     * 存放位置
     */
    @Transient
    @ApiModelProperty(value = "存放位置",example = "存放位置")
    @Excel(name = "存放位置")
    private String storageName;
}