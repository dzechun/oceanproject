package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamJig;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class EamJigRequisitionDto extends EamJigRequisition implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="8")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="10")
    private String modifiedUserName;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 治具编码
     */
    @Transient
    @ApiModelProperty(name = "jigCode",value = "治具编码")
    @Excel(name = "治具编码", height = 20, width = 30,orderNum="5")
    private String jigCode;

    /**
     * 治具名称
     */
    @Transient
    @ApiModelProperty(name = "jigName",value = "治具名称")
    @Excel(name = "治具名称", height = 20, width = 30,orderNum="5")
    private String jigName;

    /**
     * 治具描述
     */
    @Transient
    @ApiModelProperty(name = "jigDesc",value = "治具描述")
    @Excel(name = "治具描述", height = 20, width = 30,orderNum="5")
    private String jigDesc;

    /**
     * 治具型号
     */
    @Transient
    @ApiModelProperty(name = "jigModel",value = "治具型号")
    @Excel(name = "治具型号", height = 20, width = 30,orderNum="5")
    private String jigModel;

    /**
     * 治具类别
     */
    @Transient
    @ApiModelProperty(name = "jigCategoryName",value = "治具类别")
    @Excel(name = "治具类别", height = 20, width = 30,orderNum="5")
    private String jigCategoryName;

    /**
     * 治具条码
     */
    @Transient
    @ApiModelProperty(name = "jigBarcode",value = "治具条码")
    @Excel(name = "治具条码", height = 20, width = 30,orderNum="5")
    private String jigBarcode;

    /**
     * 当前使用次数
     */
    @Transient
    @ApiModelProperty(name = "currentUsageTime",value = "当前使用次数")
    @Excel(name = "当前使用次数", height = 20, width = 30,orderNum="5")
    private Integer currentUsageTime;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name = "workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="5")
    private String workOrderCode;

    /**
     * 产品料号
     */
    @Transient
    @ApiModelProperty(name = "materialCode",value = "产品料号")
    private String materialCode;

    /**
     * 料号描述
     */
    @Transient
    @ApiModelProperty(name = "materialDesc",value = "料号描述")
    private String materialDesc;

    /**
     * 生产线名
     */
    @Transient
    @ApiModelProperty(name = "proName",value = "生产线名")
    private String proName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name = "warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 库区
     */
    @Transient
    @ApiModelProperty(name = "warehouseAreaName",value = "库区")
    private String warehouseAreaName;

    /**
     * 推荐库位
     */
    @Transient
    @ApiModelProperty(name = "storageCode",value = "推荐库位")
    private String storageCode;

}
