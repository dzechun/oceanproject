package com.fantechs.common.base.dto.apply;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class SmtWorkOrderBomDto extends SmtWorkOrder implements Serializable {

    private static final long serialVersionUID = 9163386553195751648L;

    /**
     * 工单号
     */
    @Transient
    @ApiModelProperty(name="workOrderCode" ,value="工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="1")
    private String workOrderCode;
    /**
     * 物料编码
     */
    @Transient
    @ApiModelProperty(name="partMaterialCode" ,value="零件料号")
    @Excel(name = "零件料号", height = 20, width = 30,orderNum="2")
    private String partMaterialCode;

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
     * 零件替代料号
     */
    @Transient
    @ApiModelProperty(name="subMaterialCode" ,value="零件替代料号")
    private String subMaterialCode;

    /**
     * 工序名称
     */
    @Transient
    @ApiModelProperty(name="processName" ,value="工序名称")
    @Excel(name = "工序名称", height = 20, width = 30,orderNum="6")
    private String processName;

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "修改用户名称")
    private String modifiedUserName;
}
