package com.fantechs.common.base.general.dto.mes.sfc;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class MesSfcRepairOrderDto extends MesSfcRepairOrder implements Serializable {
    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="6")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="2")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    private String materialCode;


    /**
     * 产品描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "产品描述")
    private String materialDesc;

    /**
     * 物料属性(0.半成品，1.成品)
     */
    @Transient
    @ApiModelProperty(name = "materialProperty",value = "物料属性(0.半成品，1.成品)")
    private Byte materialProperty;

    /**
     * 产线名称
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "产线名称")
    private String proName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name = "routeName",value = "工艺路线名称")
    private String routeName;

    /**
     * 不良工序名称
     */
    @Transient
    @ApiModelProperty(name = "badProcessName",value = "不良工序名称")
    private String badProcessName;

    /**
     * 当前工序名称
     */
    @Transient
    @ApiModelProperty(name = "currentProcessName",value = "当前工序名称")
    private String currentProcessName;

    /**
     * 转接确认人
     */
    @Transient
    @ApiModelProperty(name = "transferComfirmUserName",value = "转接确认人")
    private String transferComfirmUserName;

    /**
     * 转出确认人
     */
    @Transient
    @ApiModelProperty(name = "transferOutComfirmUserName",value = "转出确认人")
    private String transferOutComfirmUserName;
}
