package com.fantechs.common.base.general.dto.wms.inner;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class WmsInnerJobOrderDto extends WmsInnerJobOrder implements Serializable {
    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName",value = "货主名称")
    private String materialOwnerName;

    /**
     * 仓库
     */
    @Transient
    @ApiModelProperty(name="warehouseName",value = "仓库")
    private String warehouseName;

    /**
     * 单据类型
     */
    @Transient
    @ApiModelProperty(name = "orderTypeName",value = "单据类型")
    private String orderTypeName;

    /**
     * 工作人员
     */
    @Transient
    @ApiModelProperty(name = "workName",value = "工作人员")
    private String workName;

    /**
     * 组织
     */
    @Transient
    @ApiModelProperty(name="organizationName",value = "组织")
    private String organizationName;

    /**
     * 创建人
     */
    @Transient
    @ApiModelProperty(name="createUserName",value = "创建人")
    private String createUserName;

    /**
     * 修改人
     */
    @Transient
    @ApiModelProperty(name="modifiedUserName",value = "修改人")
    private String modifiedUserName;

    /**
     * 是否栈板自动生成(1-是，0-否) PDA标识字段
     */
    @Transient
    @ApiModelProperty(name = "isPallet",value = "是否栈板自动生成(1-是，0-否) PDA标识字段")
    private Byte isPallet;

    /**
     * 收货人名称
     */
    @Transient
    @ApiModelProperty(name="consigneeName",value = "发货人名称")
    @Excel(name = "发货人名称", height = 20, width = 30,orderNum="")
    private String consigneeName;

    /**
     * 联系人名称
     */
    @ApiModelProperty(name="linkManName",value = "联系人名称")
    @Excel(name = "联系人名称", height = 20, width = 30,orderNum="")
    @Transient
    private String linkManName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name="linkManPhone",value = "联系人电话")
    @Excel(name = "联系人电话", height = 20, width = 30,orderNum="")
    @Transient
    private String linkManPhone;

    /**
     * 传真号码
     */
    @Transient
    @ApiModelProperty(name="faxNumber",value = "传真号码")
    @Excel(name = "传真号码", height = 20, width = 30,orderNum="")
    private String faxNumber;

    /**
     * 邮箱地址
     */
    @Transient
    @ApiModelProperty(name="eMailAddress",value = "邮箱地址")
    @Excel(name = "邮箱地址", height = 20, width = 30,orderNum="")
    private String eMailAddress;

    /**
     * 详细地址
     */
    @Transient
    @ApiModelProperty(name="detailedAddress",value = "详细地址")
    @Excel(name = "详细地址", height = 20, width = 30,orderNum="")
    private String detailedAddress;

    /**
     * 月台名称
     */
    @Transient
    @ApiModelProperty(name = "platformName",value = "月台")
    private String platformName;

    @Transient
    @ApiModelProperty(name = "stackingCode",value = "堆垛编码")
    private String stackingCode;

    @Transient
    @ApiModelProperty(name = "releaseName",value = "堆垛释放人")
    private String releaseName;
}
