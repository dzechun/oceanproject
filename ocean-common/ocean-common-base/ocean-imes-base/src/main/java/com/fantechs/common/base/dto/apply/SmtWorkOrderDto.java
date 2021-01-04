package com.fantechs.common.base.dto.apply;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtWorkOrderDto extends SmtWorkOrder implements Serializable {

    private static final long serialVersionUID = 5293503265879392765L;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="materialCode" ,value="物料编码")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="2")
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
    @ApiModelProperty(name="version" ,value="版本")
    @Excel(name = "产品料号版本", height = 20, width = 30,orderNum="3")
    private String version;

    /**
     * 物料描述
     */
    @Transient
    @ApiModelProperty(name="materialDesc" ,value="物料描述")
    @Excel(name = "产品料号描述", height = 20, width = 30,orderNum="4")
    private String materialDesc;

    /**
     * 转移批量
     */
    @Transient
    @ApiModelProperty(name="transferQuantity" ,value="转移批量")
    private Integer transferQuantity;

    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "生产线", height = 20, width = 30,orderNum = "9")
    private String proName;
    /**
     * 工艺路线名称
     */
    @Transient
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="10")
    private String routeName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建账号", height = 20, width = 30,orderNum="17")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    @Excel(name = "修改账号", height = 20, width = 30,orderNum="19")
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
     * 流转卡规则解析码
     */
    @Transient
    @ApiModelProperty(name="analysisCode" ,value="流转卡规则解析码")
    private String analysisCode;

    /**
     * 订单号
     */
    @Transient
    @ApiModelProperty(name="orderCode" ,value="订单号")
    private String orderCode;

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
