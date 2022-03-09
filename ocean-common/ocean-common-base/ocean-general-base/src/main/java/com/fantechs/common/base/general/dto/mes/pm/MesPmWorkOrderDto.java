package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class MesPmWorkOrderDto extends MesPmWorkOrder implements Serializable {

    private static final long serialVersionUID = 5293503265879392765L;
    /**
     * 物料编码.
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30)
    private String materialCode;

    /**
     * 物料名称
     */
    @Transient
    @ApiModelProperty(name="materialName" ,value="物料名称")
    private String materialName;

    /**
     * 条码规则名称
     */
    @Transient
    @ApiModelProperty(name="barcodeRuleSetName" ,value="条码规则名称")
    private String barcodeRuleSetName;

    /**
     * 版本
     */
    @Transient
    @ApiModelProperty(name="materialVersion" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30)
    private String materialVersion;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30)
    private String materialDesc;

    /**
     * 转移批量
     */
    @Transient
    @ApiModelProperty(name="transferQuantity" ,value="转移批量")
    private Integer transferQuantity;

    /**
     * 部件用量
     */
    @Transient
    @ApiModelProperty(name="transferQuantity" ,value="部件用量")
    private Integer quantity;

    /**
     * 线别编码
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别编码")
    private String proCode;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "生产线", height = 20, width = 30)
    private String proName;

    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30)
    private String routeName;

    /**
     * 工艺路线编码
     */
    @Transient
    @ApiModelProperty(name="routeCode",value = "工艺路线编码")
    @Excel(name = "工艺路线编码", height = 20, width = 30)
    private String routeCode;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30)
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30)
    private String modifiedUserName;

    /**
     * 投入工序
     */
    @Transient
    @ApiModelProperty(name="putIntoProcessName" ,value="投入工序")
    private String putIntoProcessName;

    /**
     * 产出工序
     */
    @Transient
    @ApiModelProperty(name="productionProcessName" ,value="产出工序")
    private String productionProcessName;

    /**
     * 产出工序
     */
    @Transient
    @ApiModelProperty(name="outputProcessName" ,value="产出工序")
    private String outputProcessName;

    /**
     * 流转卡规则解析码
     */
    @Transient
    @ApiModelProperty(name="analysisCode" ,value="流转卡规则解析码")
    private String analysisCode;

    /**
     * 销售订单号
     */
    @Transient
    @ApiModelProperty(name="salesOrderCode" ,value="销售订单号")
    private String salesOrderCode;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;

    /**
     * 包装单位-ID
     */
    @Transient
    @ApiModelProperty(name = "packingUnitId",value = "包装单位-ID")
    private Long packingUnitId;

    /**
     * 包装单位-名称
     */
    @Transient
    @ApiModelProperty(name = "packingUnitName",value = "包装单位-名称")
    private String packingUnitName;

    /**
     * 包装规格-ID
     */
    @Transient
    @ApiModelProperty(name = "packageSpecificationId",value = "包装规格-ID")
    private Long packageSpecificationId;

    /**
     * 包装规格-数量
     */
    @Transient
    @ApiModelProperty(name = "packageSpecificationQuantity",value = "包装规格-数量")
    private String packageSpecificationQuantity;

    /**
     * 产品颜色
     */
    @Transient
    @ApiModelProperty(name = "color",value = "产品颜色")
    private String color;

    /**
     * 产品型号
     */
    @Transient
    @ApiModelProperty(name = "productModelName",value = "产品型号")
    private String productModelName;

    /**
     * 单位
     */
    @Transient
    @ApiModelProperty(name = "mainUnit",value = "单位")
    private String mainUnit;

    /**
     * 工序链
     */
    @Transient
    @ApiModelProperty(name = "processLink",value = "工序链")
    private String processLink;

    @Transient
    @ApiModelProperty(name = "logicName",value = "erp逻辑仓名称")
    private String logicName;

    @Transient
    List<MesPmWorkOrderBomDto> mesPmWorkOrderBomDtos;
}
