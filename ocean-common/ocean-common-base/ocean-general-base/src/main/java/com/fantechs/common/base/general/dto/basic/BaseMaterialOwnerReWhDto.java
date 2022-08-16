package com.fantechs.common.base.general.dto.basic;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author liangzhongwu
 * @create 2021-04-23 9:30
 */
@Data
public class BaseMaterialOwnerReWhDto extends BaseMaterialOwnerReWh implements Serializable {

    private static final long serialVersionUID = -6382738605980739609L;

    /**
     * 仓库编号
     */
    @Transient
    @ApiModelProperty(name="warehouseCode" ,value="仓库编号")
    private String warehouseCode;

    /**
     * 仓库名称
     */
    @Transient
    @ApiModelProperty(name="warehouseName" ,value="仓库名称")
    private String warehouseName;

    /**
     * 仓库描述
     */
    @Transient
    @ApiModelProperty(name="warehouseDesc" ,value="仓库描述")
    private String warehouseDesc;

    /**
     * 货主编码
     */
    @Transient
    @ApiModelProperty(name="materialOwnerCode" ,value="货主编码")
    private String materialOwnerCode;

    /**
     * 货主名称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerName" ,value="货主名称")
    private String materialOwnerName;

    /**
     * 货主简称
     */
    @Transient
    @ApiModelProperty(name="materialOwnerShortName" ,value="货主简称")
    private String materialOwnerShortName;

    /**
     * 联系人
     */
    @Transient
    @ApiModelProperty(name="linkManName" ,value="联系人")
    private String linkManName;

    /**
     * 联系电话
     */
    @Transient
    @ApiModelProperty(name="linkManPhone" ,value="联系电话")
    private String linkManPhone;

    /**
     * 传真
     */
    @Transient
    @ApiModelProperty(name="faxNumber" ,value="传真")
    private String faxNumber;

    /**
     * 邮箱
     */
    @Transient
    @ApiModelProperty(name="emailAddress" ,value="邮箱")
    private String emailAddress;

    /**
     * 详细地址
     */
    @Transient
    @ApiModelProperty(name="address" ,value="详细地址")
    private String address;

    /**
     * 描述
     */
    @Transient
    @ApiModelProperty(name="materialOwnerDesc" ,value="描述")
    private String materialOwnerDesc;

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

    /**
     * 组织名称
     */
    @Transient
    @ApiModelProperty(name = "organizationName",value = "组织名称")
    private String organizationName;
}
