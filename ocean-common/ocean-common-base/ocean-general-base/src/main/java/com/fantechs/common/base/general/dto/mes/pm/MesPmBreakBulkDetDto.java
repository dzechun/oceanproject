package com.fantechs.common.base.general.dto.mes.pm;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Mr.Lei
 * @create 2021/1/18
 */
@Data
public class MesPmBreakBulkDetDto extends MesPmBreakBulkDet implements Serializable {

    /**
     * 工单id
     */
    @Column(name = "work_order_id")
    @ApiModelProperty(name="workOrderId" ,value="工单id")
    private String workOrderId;
    /**
     * 工单号
     */
    @Column(name = "work_order_code")
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="1")
    private String workOrderCode;
    /**
     * 物料编码.
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
     * 投产数量
     */
    @Column(name = "production_quantity")
    @ApiModelProperty(name="productionQuantity" ,value="投产数量")
    @Excel(name = "投产数量", height = 20, width = 30,orderNum="6")
    private BigDecimal productionQuantity;
    /**
     * 线别名称
     */
    @Transient
    @ApiModelProperty(name="proName" ,value="线别名称")
    @Excel(name = "生产线", height = 20, width = 30,orderNum = "9")
    private String proName;
    /**
     * 工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)
     */
    @Column(name = "work_order_status")
    @ApiModelProperty(name="workOrderStatus" ,value="工单状态(0、待生产 1、生产中 2、暂停生产 3、生产完成)")
    @Excel(name = "工单状态", height = 20, width = 30 ,orderNum="8",replace = {"待生产_0", "生产中_1","暂停生产_2","生产完成_3"})
    private Integer workOrderStatus;
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
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name = "processName",value = "工序名称")
    private String processName;

    /**
     * 工序id
     */
    @Transient
    @ApiModelProperty(name = "processId",value = "工序id")
    private String processId;
}
