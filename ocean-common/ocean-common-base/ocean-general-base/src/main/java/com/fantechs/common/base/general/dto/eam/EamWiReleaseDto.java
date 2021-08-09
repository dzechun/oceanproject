package com.fantechs.common.base.general.dto.eam;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.eam.EamWiRelease;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Data
public class EamWiReleaseDto extends EamWiRelease implements Serializable {

    /**
     * 创建用户名称
     */
    @Transient
    @ApiModelProperty(name = "createUserName",value = "创建用户名称")
    @Excel(name = "创建用户名称", height = 20, width = 30,orderNum="12")
    private String createUserName;

    /**
     * 修改用户名称
     */
    @Transient
    @ApiModelProperty(name = "modifiedUserName",value = "修改用户名称")
    @Excel(name = "修改用户名称", height = 20, width = 30,orderNum="14")
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
    @ApiModelProperty(name="workOrderCode",value = "工单号")
    @Excel(name = "工单号", height = 20, width = 30,orderNum="2")
    @Transient
    private String workOrderCode;

    /**
     * 产品料号
     */
    @ApiModelProperty(name="materialCode",value = "产品料号")
    @Excel(name = "产品料号", height = 20, width = 30,orderNum="3")
    @Transient
    private String materialCode;

    /**
     * 产品描述
     */
    @ApiModelProperty(name="productModelName",value = "产品描述")
    @Excel(name = "产品描述", height = 20, width = 30,orderNum="4")
    @Transient
    private String productModelName;

    /**
     * 产品型号
     */
    @ApiModelProperty(name="productModelCode",value = "产品型号")
    @Excel(name = "产品型号", height = 20, width = 30,orderNum="5")
    @Transient
    private String productModelCode;

    /**
     * 工艺路线名称
     */
    @ApiModelProperty(name="routeName" ,value="工艺路线名称")
    @Excel(name = "工艺路线名称", height = 20, width = 30,orderNum="6")
    @Transient
    private String routeName;

    /**
     * 工厂名称
     */
    @ApiModelProperty(name="factoryName",value = "工厂名称")
    @Excel(name = "工厂", height = 20, width = 30,orderNum="7")
    @Transient
    private String factoryName;

    /**
     * 车间名称
     */
    @ApiModelProperty(name="workShopName",value = "车间名称")
    @Excel(name = "车间", height = 20, width = 30,orderNum="8")
    @Transient
    private String workShopName;

    /**
     * 产线名称
     */
    @ApiModelProperty(name="proLineName",value = "产线名称")
    @Excel(name = "产线", height = 20, width = 30,orderNum="9")
    @Transient
    private String proLineName;

    /**
     * 工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)
     */
    @ApiModelProperty(name="workOrderStatus",value = "工单状态(1:Initial：下载或手动创建；2:Release：条码打印完成;3:WIP:生产中，4:Hold：异常挂起5:Cancel：取消6:Complete：完工7:Delete：删除)")
    @Excel(name = "工单状态", height = 20, width = 30,orderNum="10",replace = {"Initial_1","Release_2","WIP_3","Hold_4","Cancel_5","Complete_6","Delete_7"})
    @Transient
    private Byte workOrderStatus;

    List<EamWiReleaseDetDto> eamWiReleaseDetDtos;
}
