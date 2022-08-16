package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamJigMaterial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class EamJigMaterialDto extends EamJigMaterial implements Serializable {

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
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="8")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 仓库库位
     */
    @Transient
    @ApiModelProperty(name = "warehouseStorage",value = "仓库库位")
    private String warehouseStorage;

    /**
     * 设备名称
     */
    @Transient
    @ApiModelProperty(name = "equipmentName",value = "设备名称")
    private String equipmentName;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="1")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="2")
    private String jigName;

    /**
     * 治具描述
     */
    @Transient
    @ApiModelProperty(name = "jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="3")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Transient
    @ApiModelProperty(name = "jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="4")
    private String jigModel;

    /**
     * 使用数量
     */
    @Transient
    @ApiModelProperty(name="usageQty",value = "使用数量")
    private Integer usageQty;

    /**
     * 记录数量
     */
    @Transient
    @ApiModelProperty(name="recordQty",value = "记录数量")
    private Integer recordQty;

    /**
     * 归还数量
     */
    @Transient
    @ApiModelProperty(name="returnQty",value = "归还数量")
    private Integer returnQty;

    /**
     * 旧工单id
     */
    @Transient
    @ApiModelProperty(name="oldWorkOrderId",value = "旧工单id")
    private Long oldWorkOrderId;

    /**
     * 治具绑定产品明细
     */
    @ApiModelProperty(name = "list",value = "治具绑定产品明细")
    private List<EamJigMaterialListDto> list = new ArrayList<>();
}
